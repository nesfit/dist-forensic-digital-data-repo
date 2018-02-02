package communication.consumer.handler;

import common.util.FileManager;
import communication.KafkaRequest;
import communication.KafkaResponse;
import communication.producer.AcknowledgementProducer;
import database.factory.DatabaseAbstractFactory;
import database.factory.FactoryCreator;
import database.interfaces.IStore;
import org.pcap4j.packet.Packet;
import service.pcap.IPcapParser;
import service.pcap.org.pcap4j.PcapParser;

import java.io.IOException;
import java.util.List;

public class StorePcapHandler implements ICommandHandler<KafkaRequest, byte[]> {

    private IStore packetStore;
    private IPcapParser pcapParser;
    private String tmpFile;
    private int count;

    @Override
    public void handleRequest(KafkaRequest request, byte[] value) {
        try {

            initHandlers(request);
            processPayload(value);
            processPackets(tmpFile);
            FileManager.RemoveFile(tmpFile);

            // TODO: Remove hardcoded values
            sendAcknowledgement(buildResponse(request), "Response OK".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initHandlers(KafkaRequest key) {
        DatabaseAbstractFactory storeFactory = FactoryCreator.getFactory(key.getOperation());
        packetStore = storeFactory.getStore(key.getDataType());
        pcapParser = new PcapParser();
    }

    private void processPayload(byte[] value) throws IOException {
        tmpFile = FileManager.GenerateTmpPath();
        FileManager.SaveContent(tmpFile, value);
    }

    private void processPackets(String filepath) throws IOException {
        List<Packet> packets = (List<Packet>) pcapParser.parseInput(filepath);
        count = 0;
        for (Packet packet : packets) {
            packetStore.store(packet);
            count++;
        }
    }

    private KafkaResponse buildResponse(KafkaRequest request) {
        return new KafkaResponse.Builder()
                .id(request.getId()).responseTopic(request.getResponseTopic()).responseCode(200)
                .status("OK").detailMessage("Successfully stored " + count + " packets").build();
    }

    private void sendAcknowledgement(KafkaResponse response, byte[] value) {
        AcknowledgementProducer acknowledgementProducer = new AcknowledgementProducer(response, value);
        acknowledgementProducer.acknowledge();
    }

}
