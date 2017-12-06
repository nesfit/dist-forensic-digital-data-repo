package database.impl;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import database.interfaces.CassandraStore;
import database.interfaces.IStore;
import org.pcap4j.packet.Packet;
import service.serialize.Serializer;

import java.io.IOException;

public class PacketStore extends CassandraStore implements IStore<Packet> {

    @Override
    public void store(Packet value) {
        String packetString = null;
        try {
            packetString = Serializer.Serialize(value);
            Cluster cluster = Cluster.builder().addContactPoint(address).build();
            Session session = cluster.connect(keyspace);
            String query = "INSERT INTO pcap (id, packet) VALUES(now(), ?);";
            PreparedStatement statement = session.prepare(query);
            BoundStatement boundStatement = statement.bind(packetString);
            session.executeAsync(boundStatement);
            session.close();
            cluster.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
