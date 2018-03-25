package cz.vutbr.fit.persistence.cassandra.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import cz.vutbr.fit.persistence.cassandra.entity.CassandraPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.Semaphore;

public class PacketRepositoryImpl implements AsyncOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketRepositoryImpl.class);

    @Autowired
    private Cluster cluster;
    @Autowired
    private Session session;
    private AsyncCassandraTemplate asyncCassandraTemplate;

    private static final String INSERT_QUERY = "INSERT INTO packet (id, packet) VALUES(?, ?);";
    private PreparedStatement insertPreparedStatement;

    private static final String SELECT_QUERY = "SELECT * FROM packet WHERE id = ?";
    private PreparedStatement selectPreparedStatement;

    private int maxRequestsPerConnection = 0;
    private int maxConnectionsPerHost = 0;
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
        initAsyncCassandraTemplate();
        initQueries();
        initSemaphore();
    }

    private void initAsyncCassandraTemplate() {
        asyncCassandraTemplate = new AsyncCassandraTemplate(session);
    }

    private void initQueries() {
        insertPreparedStatement = session.prepare(INSERT_QUERY);
        selectPreparedStatement = session.prepare(SELECT_QUERY);
    }

    private void initSemaphore() {
        LoadBalancingPolicy loadBalancingPolicy = cluster.getConfiguration().getPolicies().getLoadBalancingPolicy();
        PoolingOptions poolingOptions = cluster.getConfiguration().getPoolingOptions();

        session.getState().getConnectedHosts().forEach(host -> {
            HostDistance distance = loadBalancingPolicy.distance(host);
            // TODO: Take maxRequestsPerConnection for the current host
            maxRequestsPerConnection = poolingOptions.getMaxRequestsPerConnection(distance);
            maxConnectionsPerHost = poolingOptions.getMaxConnectionsPerHost(distance);
        });

        // Initialize semaphore to < maxConnectionsPerHost * maxRequestsPerConnection >
        int semaphorePermits = maxConnectionsPerHost * maxRequestsPerConnection;
        semaphore = new Semaphore(semaphorePermits);
        LOGGER.info(String.format("Semaphore initialized with %d permits.", semaphorePermits));
    }

    @Override
    public ResultSetFuture insertAsync(CassandraPacket cassandraPacket) {
        return insertAsync(cassandraPacket.getId(), cassandraPacket.getPacket());
    }

    private ResultSetFuture insertAsync(UUID id, ByteBuffer packet) {
        lockSemaphore();

        // This approach with cached prepared statement is much faster
        BoundStatement boundStatement = insertPreparedStatement.bind(id, packet);
        ResultSetFuture resultSetFuture = session.executeAsync(boundStatement);

        Futures.addCallback(resultSetFuture, new FutureCallback<ResultSet>() {
            @Override
            public void onSuccess(ResultSet rows) {
                unlockSemaphore();
            }

            @Override
            public void onFailure(Throwable throwable) {
                unlockSemaphore();
            }
        }, MoreExecutors.newDirectExecutorService());

        return resultSetFuture;
    }

    @Override
    public ResultSetFuture selectAsync(UUID id, OnSuccessCallback onSuccessCallback) {
        lockSemaphore();

        BoundStatement boundStatement = selectPreparedStatement.bind(id);
        ResultSetFuture resultSetFuture = session.executeAsync(boundStatement);

        /*Futures.addCallback(resultSetFuture, new FutureCallback<ResultSet>() {
            @Override
            public void onSuccess(ResultSet rows) {
                unlockSemaphore();
            }

            @Override
            public void onFailure(Throwable throwable) {
                unlockSemaphore();
            }
        }, MoreExecutors.newDirectExecutorService());*/

        Futures.addCallback(resultSetFuture,
                new FutureCallback<ResultSet>() {
                    @Override
                    public void onSuccess(ResultSet result) {
                        Row row = result.one();
                        UUID id = row.get("id", UUID.class);
                        ByteBuffer rawPacket = row.get("packet", ByteBuffer.class);

                        unlockSemaphore();

                        CassandraPacket packet = new CassandraPacket();
                        packet.setId(id);
                        packet.setPacket(rawPacket);
                        onSuccessCallback.onSuccess(packet);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        unlockSemaphore();
                    }
                },
                MoreExecutors.newDirectExecutorService());

        return resultSetFuture;
    }

    private void lockSemaphore() {
        try {
            semaphore.acquire();
        } catch (InterruptedException exception) {
            LOGGER.error(exception.getMessage(), exception);
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
        LOGGER.error(throwable.getMessage(), throwable);
        unlockSemaphore();
    }

}
