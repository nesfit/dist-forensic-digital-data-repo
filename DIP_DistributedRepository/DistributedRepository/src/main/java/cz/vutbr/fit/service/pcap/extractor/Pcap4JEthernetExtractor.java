package cz.vutbr.fit.service.pcap.extractor;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.MacAddress;
import org.springframework.stereotype.Component;

@Component
public class Pcap4JEthernetExtractor extends PacketExtractor<Packet, PacketMetadata.Builder> {

    @Override
    public void extractMetadata(Packet packet, PacketMetadata.Builder packetMetadataBuilder) {
        if (packet.contains(EthernetPacket.class)) {
            EthernetPacket ethernetPacket = packet.get(EthernetPacket.class);
            EtherType type = ethernetPacket.getHeader().getType();
            MacAddress srcAddr = ethernetPacket.getHeader().getSrcAddr();
            MacAddress dstAddr = ethernetPacket.getHeader().getDstAddr();

            packetMetadataBuilder.ethernetTypeName(type.name()).ethernetTypeValue(type.value())
                    .srcLinkLayerAddress(srcAddr.toString()).dstLinkLayerAddress(dstAddr.toString());
        }

        if (successor != null) {
            successor.extractMetadata(packet, packetMetadataBuilder);
        }
    }

}
