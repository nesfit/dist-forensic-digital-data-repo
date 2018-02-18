package cz.vutbr.fit.database.interfaces;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import common.properties.Properties;
import cz.vutbr.fit.common.properties.PropertyConstants;

import java.io.Closeable;
import java.io.IOException;

public class CassandraStore implements Closeable {

    private static volatile CassandraStore instance = null;

    private String address = Properties.getInstance().loadProperty(PropertyConstants.IP);
    private String keyspace = Properties.getInstance().loadProperty(PropertyConstants.KEYSPACE);

    Cluster cluster = Cluster.builder().addContactPoint(address).build();
    Session session = cluster.connect(keyspace);

    private CassandraStore() {

    }

    public static CassandraStore getInstance() {
        if (instance == null)
            synchronized (CassandraStore.class) {
                if (instance == null)
                    instance = new CassandraStore();
            }
        return instance;
    }

    @Override
    public void close() throws IOException {
        session.close();
        cluster.close();
    }

    public Session getSession() {
        return session;
    }

}
