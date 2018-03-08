package cz.vutbr.fit.service.pcap.extractor;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

public class Pcap4JTransportExtractor implements PacketExtractor<PcapPacket, PacketMetadata.Builder> {

    @Override
    public void extractMetadata(PcapPacket packet, PacketMetadata.Builder packetMetadataBuilder) {
        extractTcpMetadata(packet, packetMetadataBuilder);
        extractUdpMetadata(packet, packetMetadataBuilder);
    }

    private void extractTcpMetadata(PcapPacket packet, PacketMetadata.Builder packetMetadataBuilder) {
        if (packet.contains(TcpPacket.class)) {
            TcpPacket tcpPacket = packet.get(TcpPacket.class);
            String srcPort = tcpPacket.getHeader().getSrcPort().valueAsString();
            String dstPort = tcpPacket.getHeader().getDstPort().valueAsString();

            packetMetadataBuilder.srcPort(srcPort).dstPort(dstPort);
        }
    }

    private void extractUdpMetadata(PcapPacket packet, PacketMetadata.Builder packetMetadataBuilder) {
        if (packet.contains(UdpPacket.class)) {
            UdpPacket udpPacket = packet.get(UdpPacket.class);
            String srcPort = udpPacket.getHeader().getSrcPort().valueAsString();
            String dstPort = udpPacket.getHeader().getDstPort().valueAsString();

            packetMetadataBuilder.srcPort(srcPort).dstPort(dstPort);
        }
    }

}
