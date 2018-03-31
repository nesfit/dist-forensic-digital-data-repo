package cz.vutbr.fit.distributedrepository.service.pcap.extractor.pcap4j;

import cz.vutbr.fit.distributedrepository.service.pcap.extractor.PacketExtractor;
import cz.vutbr.fit.persistence.mongodb.entity.PacketMetadata;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.namednumber.IpNumber;
import org.pcap4j.packet.namednumber.IpVersion;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class IpPacketExtractor implements PacketExtractor<PcapPacket, PacketMetadata.Builder> {

    @Override
    public void extractMetadata(PcapPacket packet, PacketMetadata.Builder packetMetadataBuilder) {
        if (packet.contains(IpPacket.class)) {
            IpPacket ipPacket = packet.get(IpPacket.class);
            IpNumber protocol = ipPacket.getHeader().getProtocol();
            IpVersion version = ipPacket.getHeader().getVersion();
            InetAddress srcAddr = ipPacket.getHeader().getSrcAddr();
            InetAddress dstAddr = ipPacket.getHeader().getDstAddr();

            packetMetadataBuilder
                    .ipProtocolName(protocol.name())
                    .ipProtocolValue(protocol.value())
                    .ipVersionName(version.name())
                    .ipVersionValue(version.value())
                    .srcIpAddress(srcAddr.toString())
                    .dstIpAddress(dstAddr.toString());
        }
    }

}
