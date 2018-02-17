package cz.vutbr.fit.cassandra.repository;

import com.datastax.driver.core.Session;
import cz.vutbr.fit.cassandra.entity.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;

public class PacketRepositoryImpl implements InsertAsync {

    @Autowired
    Session session;

    private AsyncCassandraTemplate asyncCassandraTemplate;

    @PostConstruct
    private void init() {
        asyncCassandraTemplate = new AsyncCassandraTemplate(session);
    }

    @Override
    public void insertAsync(Packet packet) {
        ListenableFuture<Packet> listenableFuture = asyncCassandraTemplate.insert(packet);
        listenableFuture.addCallback(PacketRepositoryImpl::onSuccess, PacketRepositoryImpl::onFailure);
    }

    private static void onSuccess(Packet packet) {

    }

    private static void onFailure(Throwable throwable) {
        throwable.printStackTrace();
    }

}
