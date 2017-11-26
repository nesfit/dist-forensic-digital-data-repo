import common.properties.Properties;
import common.properties.PropertyConstants;
import communication.consumer.Consumer;
import communication.consumer.ConsumerHandler;
import communication.request.KafkaRequest;
import communication.request.serialization.KafkaRequestDeserializer;
import io.pkts.packet.Packet;
import org.apache.kafka.common.serialization.StringDeserializer;
import service.database.DatabaseSvc;
import service.database.storage.store.PacketSave;
import service.pcap.IPcapParser;
import service.pcap.io.pkts.PcapParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class ConsumerMain {

    public static void main(String[] args) {

        Consumer<String, KafkaRequest<String>> consumer = new Consumer<>(StringDeserializer.class, KafkaRequestDeserializer.class);
        consumer.subscribe(Properties.getInstance().loadProperty(PropertyConstants.PACKET_TOPIC));
        consumer.consume(new PacketConsumerHandler());

    }

    private static class PacketConsumerHandler implements ConsumerHandler<String, KafkaRequest<String>> {

        private DatabaseSvc<PacketSave> databaseSvc = new DatabaseSvc<>();

        @Override
        public void handleRequest(String key, KafkaRequest<String> value) {
            try {
                IPcapParser pcapParser = new PcapParser();
                byte[] b = Base64.getDecoder().decode(value.getValue());
                List<Packet> packets = (List<Packet>) pcapParser.parseInput(new ByteArrayInputStream(b));

                for (Packet packet : packets) {
                    PacketSave packetSave = new PacketSave(packet);
                    databaseSvc.store(value.getOperation(), value.getDataType(), packetSave);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
