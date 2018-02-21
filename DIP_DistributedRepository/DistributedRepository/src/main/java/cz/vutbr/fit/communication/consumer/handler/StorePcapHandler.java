package cz.vutbr.fit.communication.consumer.handler;

import com.datastax.driver.core.utils.UUIDs;
import cz.vutbr.fit.cassandra.repository.PacketRepository;
import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.KafkaResponse;
import cz.vutbr.fit.communication.producer.AcknowledgementProducer;
import cz.vutbr.fit.mongodb.repository.PacketMetadataRepository;
import cz.vutbr.fit.service.pcap.IPcapParser;
import cz.vutbr.fit.service.pcap.OnPacketCallback;
import cz.vutbr.fit.util.FileManager;
import org.pcap4j.packet.Packet;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

public class StorePcapHandler implements ICommandHandler<KafkaRequest, byte[]> {

    @Autowired
    private PacketRepository packetRepository;
    @Autowired
    private PacketMetadataRepository packetMetadataRepository;
    @Autowired
    private IPcapParser pcapParser;

    private String tmpFile;
    private int count;

    @Override
    public void handleRequest(KafkaRequest request, byte[] value) {
        try {

            storePayload(value);
            processPackets();
            removePayload();

            // TODO: Remove hardcoded values
            sendAcknowledgement(buildResponse(request), "Response OK".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storePayload(byte[] value) throws IOException {
        tmpFile = FileManager.GenerateTmpPath();
        FileManager.SaveContent(tmpFile, value);
    }

    private void processPackets() throws IOException {
        count = 0;
        Date startTime = new Date();
        pcapParser.parseInput(tmpFile, new OnPacketCallbackImpl());
        Date endTime = new Date();
        System.out.println(count + " packets processed in " + ((endTime.getTime() - startTime.getTime()) / 1000) + " seconds");
    }

    private class OnPacketCallbackImpl implements OnPacketCallback<Packet> {
        @Override
        public void processPacket(Packet packet) {
            count++;

            cz.vutbr.fit.cassandra.entity.Packet p = new cz.vutbr.fit.cassandra.entity.Packet.Builder()
                    .id(UUIDs.timeBased()).packet(ByteBuffer.wrap(packet.getRawData())).build();
            packetRepository.insertAsync(p);
        }
    }

    private void removePayload() {
        FileManager.RemoveFile(tmpFile);
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

    public void setPacketMetadataRepository(PacketMetadataRepository packetMetadataRepository) {
        this.packetMetadataRepository = packetMetadataRepository;
    }

    public void setPcapParser(IPcapParser pcapParser) {
        this.pcapParser = pcapParser;
    }

}
