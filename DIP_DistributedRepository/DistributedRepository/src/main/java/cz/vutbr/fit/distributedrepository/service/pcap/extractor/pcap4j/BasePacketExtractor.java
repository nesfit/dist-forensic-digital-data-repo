package cz.vutbr.fit.distributedrepository.service.pcap.extractor.pcap4j;

import cz.vutbr.fit.distributedrepository.service.pcap.extractor.PacketExtractor;
import cz.vutbr.fit.persistence.mongodb.entity.PacketMetadata;
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
