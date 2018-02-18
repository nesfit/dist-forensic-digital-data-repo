package cz.vutbr.fit.communication.consumer.handler;

import communication.KafkaRequest;
import communication.KafkaResponse;
import communication.consumer.handler.ICommandHandler;
import cz.vutbr.fit.common.util.FileManager;
import cz.vutbr.fit.communication.producer.AcknowledgementProducer;
import cz.vutbr.fit.database.factory.DatabaseAbstractFactory;
import cz.vutbr.fit.database.factory.FactoryCreator;
import cz.vutbr.fit.database.impl.PacketStore;
import cz.vutbr.fit.database.interfaces.IStore;
import cz.vutbr.fit.service.pcap.IPcapParser;
import cz.vutbr.fit.service.pcap.org.pcap4j.PcapParser;
import org.pcap4j.packet.Packet;

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
        // TODO: Remove comments
        // DatabaseAbstractFactory storeFactory = FactoryCreator.getFactory(key.getOperation());
        // packetStore = storeFactory.getStore(key.getDataType());
        packetStore = new PacketStore();
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
