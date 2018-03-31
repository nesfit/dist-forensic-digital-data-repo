package cz.vutbr.fit.distributedrepository.service.pcap.extractor.pcap4j;

import cz.vutbr.fit.distributedrepository.service.pcap.extractor.PacketExtractor;
import cz.vutbr.fit.persistence.mongodb.entity.PacketMetadata;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

public class TransportPacketExtractor implements PacketExtractor<PcapPacket, PacketMetadata.Builder> {

    @Override
    public void extractMetadata(PcapPacket packet, PacketMetadata.Builder packetMetadataBuilder) {
        extractTcpMetadata(packet, packetMetadataBuilder);
        extractUdpMetadata(packet, packetMetadataBuilder);
    }

    private void extractTcpMetadata(PcapPacket packet, PacketMetadata.Builder packetMetadataBuilder) {
        if (packet.contains(TcpPacket.class)) {
            TcpPacket tcpPacket = packet.get(TcpPacket.class);
            int srcPort = tcpPacket.getHeader().getSrcPort().valueAsInt();
            int dstPort = tcpPacket.getHeader().getDstPort().valueAsInt();

            packetMetadataBuilder
                    .srcPort(srcPort)
                    .dstPort(dstPort);
        }
    }

    private void extractUdpMetadata(PcapPacket packet, PacketMetadata.Builder packetMetadataBuilder) {
        if (packet.contains(UdpPacket.class)) {
            UdpPacket udpPacket = packet.get(UdpPacket.class);
            int srcPort = udpPacket.getHeader().getSrcPort().valueAsInt();
            int dstPort = udpPacket.getHeader().getDstPort().valueAsInt();

            packetMetadataBuilder
                    .srcPort(srcPort)
                    .dstPort(dstPort);
        }
    }

}
