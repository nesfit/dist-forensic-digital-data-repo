package communication.consumer.handler;

import common.util.FileManager;
import communication.producer.AcknowledgementProducer;
import communication.request.KafkaRequest;
import database.factory.DatabaseAbstractFactory;
import database.factory.FactoryCreator;
import database.interfaces.IStore;
import org.pcap4j.packet.Packet;
import service.pcap.IPcapParser;
import service.pcap.org.pcap4j.PcapParser;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class StorePacketConsumerHandler implements IConsumerHandler<KafkaRequest, String> {

    //private DatabaseSvc databaseSvc = new DatabaseSvc();
    private IStore packetStore;
    private IPcapParser pcapParser;
    private String tmpFile;

    @Override
    public void handleRequest(KafkaRequest key, String value) {
        try {

            initHandlers(key);
            processPayload(value);
            processPackets(tmpFile);
            FileManager.RemoveFile(tmpFile);

            sendAcknowledgement(key, "OK");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initHandlers(KafkaRequest key) {
        DatabaseAbstractFactory storeFactory = FactoryCreator.getFactory(key.getOperation());
        packetStore = storeFactory.getStore(key.getDataType());
        pcapParser = new PcapParser();
    }

    private void processPayload(String value) throws IOException {
        byte[] byteArray = Base64.getDecoder().decode(value);
        tmpFile = FileManager.GenerateTmpPath();
        FileManager.SaveContent(tmpFile, byteArray);
    }

    private void processPackets(String filepath) throws IOException {
        List<Packet> packets = (List<Packet>) pcapParser.parseInput(filepath);
        int cnt = 0;
        for (Packet packet : packets) {
            packetStore.store(packet);
            //databaseSvc.store(value.getOperation(), value.getDataType(), packet);
        }
    }

    private void sendAcknowledgement(KafkaRequest key, String value) {
        AcknowledgementProducer acknowledgementProducer = new AcknowledgementProducer(key, value);
        acknowledgementProducer.acknowledge();
    }

}
