package cz.vutbr.fit.service.pcap.extractor;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.springframework.stereotype.Component;

@Component
public class Pcap4JIpExtractor implements PacketExtractor<PcapPacket, PacketMetadata.Builder> {

    @Override
    public void extractMetadata(PcapPacket packet, PacketMetadata.Builder packetMetadataBuilder) {
        if (packet.contains(IpPacket.class)) {
            IpPacket ipPacket = packet.get(IpPacket.class);
            IpNumber protocol = ipPacket.getHeader().getProtocol();
            IpVersion version = ipPacket.getHeader().getVersion();
            String srcAddr = ipPacket.getHeader().getSrcAddr().getHostAddress();
            String dstAddr = ipPacket.getHeader().getDstAddr().getHostAddress();

            // TODO: Is format of IP address suitable (IPv6)?
            packetMetadataBuilder.ipProtocolName(protocol.name()).ipProtocolValue(protocol.valueAsString())
                    .ipVersionName(version.name()).ipVersionValue(version.valueAsString())
                    .srcIpAddress(srcAddr).dstIpAddress(dstAddr);
        }
    }

}
