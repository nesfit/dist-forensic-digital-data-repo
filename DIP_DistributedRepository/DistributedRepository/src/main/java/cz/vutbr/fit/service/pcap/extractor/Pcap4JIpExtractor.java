package cz.vutbr.fit.service.pcap.extractor;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.springframework.stereotype.Component;

@Component
public class Pcap4JIpExtractor extends PacketExtractor<Packet, PacketMetadata.Builder> {

    @Override
    public void extractMetadata(Packet packet, PacketMetadata.Builder packetMetadataBuilder) {
        if (packet.contains(IpPacket.class)) {
            IpPacket ipPacket = packet.get(IpPacket.class);
            IpNumber protocol = ipPacket.getHeader().getProtocol();
            IpVersion version = ipPacket.getHeader().getVersion();
            String srcAddr = ipPacket.getHeader().getSrcAddr().getHostAddress();
            String dstAddr = ipPacket.getHeader().getDstAddr().getHostAddress();

            packetMetadataBuilder.ipProtocolName(protocol.name()).ipProtocolValue(protocol.value())
                    .ipVersionName(version.name()).ipVersionValue(version.value())
                    .srcIpAddress(srcAddr).dstIpAddress(dstAddr);
        }

        if (successor != null) {
            successor.extractMetadata(packet, packetMetadataBuilder);
        }
    }

}
