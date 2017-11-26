package service.database.storage.store;

import common.properties.Properties;
import common.properties.PropertyConstants;
import io.pkts.packet.Packet;

public class PacketSave implements ISave {

    protected String address;
    protected String keyspace;
    private Packet packet;

    public PacketSave(Packet packet) {
        this.packet = packet;
        address = Properties.getInstance().loadProperty(PropertyConstants.IP);
        keyspace = Properties.getInstance().loadProperty(PropertyConstants.KEYSPACE);
    }

    @Override
    public void save() {
        /*Cluster cluster = Cluster.builder().addContactPoint(address).build();
        Session session = cluster.connect(keyspace);
        String query = "INSERT INTO pcap (id, packet) VALUES(now(), ?);";
        PreparedStatement statement = session.prepare(query);
        BoundStatement boundStatement = statement.bind(packet);
        session.execute(boundStatement);
        session.close();
        cluster.close();*/
        System.out.println("Packet saved -> " + packet.toString());
    }

}
