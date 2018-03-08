package cz.vutbr.fit.service.pcap.extractor;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.pcap4j.core.PcapPacket;

import java.time.Instant;

public class Pcap4JBaseExtractor implements PacketExtractor<PcapPacket, PacketMetadata.Builder> {

    @Override
    public void extractMetadata(PcapPacket packet, PacketMetadata.Builder packetMetadataBuilder) {
        Instant timestamp = packet.getTimestamp();
        int originalLength = packet.getOriginalLength();
        // TODO: Add timestamp and originalLength into builder
    }

}
