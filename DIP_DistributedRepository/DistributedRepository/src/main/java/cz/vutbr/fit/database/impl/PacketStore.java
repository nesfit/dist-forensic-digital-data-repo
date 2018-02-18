package cz.vutbr.fit.database.impl;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import cz.vutbr.fit.database.interfaces.CassandraStore;
import cz.vutbr.fit.database.interfaces.IStore;
import cz.vutbr.fit.service.serialize.Serializer;
import org.pcap4j.packet.Packet;

import java.io.IOException;
import java.nio.ByteBuffer;

public class PacketStore implements IStore<Packet> {

    private static final String query = "INSERT INTO pcap (id, packet) VALUES(now(), ?);";
    private static final Session session = CassandraStore.getInstance().getSession();
    private PreparedStatement statement;

    public PacketStore() {
        // Cache statement
        statement = session.prepare(query);
    }

    @Override
    public void store(Packet value) {
        try {
            byte[] packetString = Serializer.Serialize(value);
            ByteBuffer bb = ByteBuffer.wrap(packetString);
            BoundStatement boundStatement = statement.bind(bb);
            session.executeAsync(boundStatement);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
