package cz.vutbr.fit.cassandra.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import cz.vutbr.fit.cassandra.entity.CassandraPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.util.UUID;

public class PacketRepositoryImpl implements InsertAsync {

    @Autowired
    private Session session;
    private AsyncCassandraTemplate asyncCassandraTemplate;

    private static final String query = "INSERT INTO packet (id, packet) VALUES(?, ?);";
    private PreparedStatement statement;

    @PostConstruct
    private void init() {
        asyncCassandraTemplate = new AsyncCassandraTemplate(session);
        statement = session.prepare(query);
    }

    @Override
    public void insertAsync(CassandraPacket cassandraPacket) {
        //insertAsyncSpringData(cassandraPacket);
        insertAsync(cassandraPacket.getId(), cassandraPacket.getPacket());
    }

    private void insertAsync(UUID id, ByteBuffer packet) {
        // This approach with cached prepared statement is much faster
        BoundStatement boundStatement = statement.bind(id, packet);
        session.executeAsync(boundStatement);
    }

    private void insertAsyncSpringData(CassandraPacket cassandraPacket) {
        // TODO: Add callback as a parameter
        ListenableFuture<CassandraPacket> listenableFuture = asyncCassandraTemplate.insert(cassandraPacket);
        listenableFuture.addCallback(PacketRepositoryImpl::onSuccess, PacketRepositoryImpl::onFailure);
    }

    private static void onSuccess(CassandraPacket cassandraPacket) {

    }

    private static void onFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

}
