package database.impl;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import database.interfaces.CassandraStore;
import database.interfaces.IStore;
import org.pcap4j.packet.Packet;
import service.serialize.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class PacketStore implements IStore<Packet> {

    @Override
    public void store(Packet value) {
        try {
            Session session = CassandraStore.getInstance().getSession();
            byte[] packetString = Serializer.Serialize(value);
            ByteBuffer bb = ByteBuffer.wrap(packetString);

            String query = "INSERT INTO pcap (id, packet) VALUES(now(), ?);";
            PreparedStatement statement = session.prepare(query);
            BoundStatement boundStatement = statement.bind(bb);

            session.executeAsync(boundStatement);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
