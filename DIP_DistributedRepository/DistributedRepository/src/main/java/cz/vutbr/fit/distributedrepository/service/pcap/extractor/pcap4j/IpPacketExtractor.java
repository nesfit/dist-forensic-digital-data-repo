package cz.vutbr.fit.distributedrepository.service.pcap.extractor.pcap4j;

import cz.vutbr.fit.distributedrepository.service.pcap.extractor.PacketExtractor;
import cz.vutbr.fit.persistence.mongodb.entity.PacketMetadata;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.springframework.stereotype.Component;

@Component
public class IpPacketExtractor implements PacketExtractor<PcapPacket, PacketMetadata.Builder> {

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
