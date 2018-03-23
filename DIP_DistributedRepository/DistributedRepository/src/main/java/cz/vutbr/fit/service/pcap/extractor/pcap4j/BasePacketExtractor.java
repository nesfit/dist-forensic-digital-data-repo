package cz.vutbr.fit.service.pcap.extractor.pcap4j;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.service.pcap.extractor.PacketExtractor;
import org.pcap4j.core.PcapPacket;

import java.time.Instant;

public class BasePacketExtractor implements PacketExtractor<PcapPacket, PacketMetadata.Builder> {

    @Override
    public void extractMetadata(PcapPacket packet, PacketMetadata.Builder packetMetadataBuilder) {
        Instant timestamp = packet.getTimestamp();
        int originalLength = packet.getOriginalLength();

        packetMetadataBuilder.timestamp(timestamp).originalLength(originalLength);
    }

}
