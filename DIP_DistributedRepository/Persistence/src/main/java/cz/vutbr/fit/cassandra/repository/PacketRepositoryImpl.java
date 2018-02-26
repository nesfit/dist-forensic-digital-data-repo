package cz.vutbr.fit.cassandra.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import cz.vutbr.fit.cassandra.entity.CassandraPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.Semaphore;

public class PacketRepositoryImpl implements InsertAsync {

    @Autowired
    private Cluster cluster;
    @Autowired
    private Session session;
    private AsyncCassandraTemplate asyncCassandraTemplate;

    private static final String query = "INSERT INTO packet (id, packet) VALUES(?, ?);";
    private PreparedStatement statement;

    private int maxRequestsPerConnection = 0;
    private Semaphore semaphore;

    @PostConstruct
    private void init() {
        postConstructValidation();
        postConstructInitialization();
    }

    private void postConstructValidation() {
        Assert.notNull(cluster, "Cluster must be initialized");
        Assert.notNull(session, "Session must be initialized");
    }

    private void postConstructInitialization() {
        asyncCassandraTemplate = new AsyncCassandraTemplate(session);
        statement = session.prepare(query);

        LoadBalancingPolicy loadBalancingPolicy = cluster.getConfiguration().getPolicies().getLoadBalancingPolicy();
        PoolingOptions poolingOptions = cluster.getConfiguration().getPoolingOptions();

        session.getState().getConnectedHosts().forEach(host -> {
            HostDistance distance = loadBalancingPolicy.distance(host);
            // TODO: Take maxRequestsPerConnection for the current host
            maxRequestsPerConnection += poolingOptions.getMaxRequestsPerConnection(distance);
        });

        // TODO: Initialize semaphore to < maxConnectionsPerHost * maxRequestsPerConnection >
        semaphore = new Semaphore(maxRequestsPerConnection);
    }

    @Override
    public void insertAsync(CassandraPacket cassandraPacket) {
        // TODO: Add callback as a parameter
        //insertAsyncSpringData(cassandraPacket);
        insertAsync(cassandraPacket.getId(), cassandraPacket.getPacket());
    }

    private void insertAsync(UUID id, ByteBuffer packet) {
        lockSemaphore();

        // This approach with cached prepared statement is much faster
        BoundStatement boundStatement = statement.bind(id, packet);
        ResultSetFuture resultSetFuture = session.executeAsync(boundStatement);

        Futures.addCallback(resultSetFuture, new FutureCallback<ResultSet>() {
            @Override
            public void onSuccess(ResultSet rows) {
                unlockSemaphore();
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                unlockSemaphore();
            }
        }, MoreExecutors.newDirectExecutorService());
    }

    private void lockSemaphore() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void unlockSemaphore() {
        semaphore.release();
    }

    private void insertAsyncSpringData(CassandraPacket cassandraPacket) {
        lockSemaphore();

        ListenableFuture<CassandraPacket> listenableFuture = asyncCassandraTemplate.insert(cassandraPacket);
        listenableFuture.addCallback(this::onSuccess, this::onFailure);
    }

    private void onSuccess(CassandraPacket cassandraPacket) {
        unlockSemaphore();
    }

    private void onFailure(Throwable throwable) {
        throwable.printStackTrace();
        unlockSemaphore();
    }

}
