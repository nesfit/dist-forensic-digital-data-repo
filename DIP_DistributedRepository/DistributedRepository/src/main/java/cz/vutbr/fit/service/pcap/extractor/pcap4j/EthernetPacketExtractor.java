package cz.vutbr.fit.service.pcap.extractor.pcap4j;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.service.pcap.extractor.PacketExtractor;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;
import org.springframework.stereotype.Component;

@Component
public class EthernetPacketExtractor implements PacketExtractor<PcapPacket, PacketMetadata.Builder> {

    @Override
    public void extractMetadata(PcapPacket packet, PacketMetadata.Builder packetMetadataBuilder) {
        if (packet.contains(EthernetPacket.class)) {
            EthernetPacket ethernetPacket = packet.get(EthernetPacket.class);
            EtherType type = ethernetPacket.getHeader().getType();
            MacAddress srcAddr = ethernetPacket.getHeader().getSrcAddr();
            MacAddress dstAddr = ethernetPacket.getHeader().getDstAddr();

            packetMetadataBuilder.ethernetTypeName(type.name()).ethernetTypeValue(type.valueAsString())
                    .srcLinkLayerAddress(srcAddr.toString()).dstLinkLayerAddress(dstAddr.toString());
        }
    }

}
