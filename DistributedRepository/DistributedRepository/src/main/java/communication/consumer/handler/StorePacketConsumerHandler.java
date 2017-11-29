package communication.consumer.handler;

import communication.producer.AcknowledgementProducer;
import communication.request.KafkaRequest;
import service.pcap.IPcapParser;
import service.pcap.org.pcap4j.PcapParser;

import java.util.Base64;

public class StorePacketConsumerHandler implements IConsumerHandler<KafkaRequest, String> {

    //private DatabaseSvc databaseSvc = new DatabaseSvc();
    private AcknowledgementProducer acknowledgementProducer;

    @Override
    public void handleRequest(KafkaRequest key, String value) {
        try {
            IPcapParser pcapParser = new PcapParser();
            byte[] b = Base64.getDecoder().decode(value);
            //List<Packet> packets = (List<Packet>) pcapParser.parseInput(new ByteArrayInputStream(b));

            /*for (Packet packet : packets) {
                databaseSvc.store(value.getOperation(), value.getDataType(), packet);
            }*/
            String packetsSuccessfullySaved = String.valueOf(382);
            acknowledgementProducer = new AcknowledgementProducer(key, packetsSuccessfullySaved);
            acknowledgementProducer.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
