package cz.vutbr.fit.communication.consumer.handler;

import com.datastax.driver.core.utils.UUIDs;
import communication.KafkaRequest;
import communication.KafkaResponse;
import communication.consumer.handler.ICommandHandler;
import cz.vutbr.fit.cassandra.repository.PacketRepository;
import cz.vutbr.fit.common.util.FileManager;
import cz.vutbr.fit.communication.producer.AcknowledgementProducer;
import cz.vutbr.fit.service.pcap.IPcapParser;
import cz.vutbr.fit.service.serialize.Serializer;
import org.pcap4j.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class StorePcapHandler implements ICommandHandler<KafkaRequest, byte[]> {

    @Autowired
    private PacketRepository packetRepository;
    @Autowired
    private IPcapParser pcapParser;
    private String tmpFile;
    private int count;

    @Override
    public void handleRequest(KafkaRequest request, byte[] value) {
        try {

            processPayload(value);
            processPackets(tmpFile);
            FileManager.RemoveFile(tmpFile);

            // TODO: Remove hardcoded values
            sendAcknowledgement(buildResponse(request), "Response OK".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processPayload(byte[] value) throws IOException {
        tmpFile = FileManager.GenerateTmpPath();
        FileManager.SaveContent(tmpFile, value);
    }

    private void processPackets(String filepath) throws IOException {
        List<Packet> packets = (List<Packet>) pcapParser.parseInput(filepath);
        count = 0;
        for (Packet packet : packets) {
            cz.vutbr.fit.cassandra.entity.Packet p = new cz.vutbr.fit.cassandra.entity.Packet();
            p.setId(UUIDs.timeBased());
            p.setPacket(ByteBuffer.wrap(Serializer.Serialize(packet)));
            packetRepository.save(p);
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

    public void setPacketRepository(PacketRepository packetRepository) {
        this.packetRepository = packetRepository;
    }

    public void setPcapParser(IPcapParser pcapParser) {
        this.pcapParser = pcapParser;
    }

}