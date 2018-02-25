package cz.vutbr.fit.cassandra.repository;

import com.datastax.driver.core.*;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import cz.vutbr.fit.cassandra.entity.CassandraPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
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

    private Semaphore semaphore;

    @PostConstruct
    private void init() {
        asyncCassandraTemplate = new AsyncCassandraTemplate(session);
        statement = session.prepare(query);

        PoolingOptions poolingOptions = cluster.getConfiguration().getPoolingOptions();
        int maxRequestsPerConnectionLocal = poolingOptions.getMaxRequestsPerConnection(HostDistance.LOCAL);
        int maxRequestsPerConnectionRemote = poolingOptions.getMaxRequestsPerConnection(HostDistance.REMOTE);

        // TODO: Find appropriate parameter and remove hardcoded value!
        semaphore = new Semaphore(maxRequestsPerConnectionLocal);
    }

    @Override
    public void insertAsync(CassandraPacket cassandraPacket) {
        // TODO: Add callback as a parameter
        //insertAsyncSpringData(cassandraPacket);
        insertAsync(cassandraPacket.getId(), cassandraPacket.getPacket());
    }

    private void insertAsync(UUID id, ByteBuffer packet) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // This approach with cached prepared statement is much faster
        BoundStatement boundStatement = statement.bind(id, packet);
        ResultSetFuture resultSetFuture = session.executeAsync(boundStatement);
        Futures.addCallback(resultSetFuture, new FutureCallback<ResultSet>() {
            @Override
            public void onSuccess(ResultSet rows) {
                semaphore.release();
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                semaphore.release();
            }
        }, MoreExecutors.newDirectExecutorService());
    }

    private void insertAsyncSpringData(CassandraPacket cassandraPacket) {
        ListenableFuture<CassandraPacket> listenableFuture = asyncCassandraTemplate.insert(cassandraPacket);
        listenableFuture.addCallback(PacketRepositoryImpl::onSuccess, PacketRepositoryImpl::onFailure);
    }

    private static void onSuccess(CassandraPacket cassandraPacket) {

    }

    private static void onFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

}
