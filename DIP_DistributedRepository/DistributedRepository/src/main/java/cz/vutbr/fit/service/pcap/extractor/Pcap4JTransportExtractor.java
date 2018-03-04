package cz.vutbr.fit.service.pcap.extractor;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;
import org.pcap4j.packet.UdpPacket;

public class Pcap4JTransportExtractor implements PacketExtractor<Packet, PacketMetadata.Builder> {

    @Override
    public void extractMetadata(Packet packet, PacketMetadata.Builder packetMetadataBuilder) {
        extractTcpMetadata(packet, packetMetadataBuilder);
        extractUdpMetadata(packet, packetMetadataBuilder);
    }

    private void extractTcpMetadata(Packet packet, PacketMetadata.Builder packetMetadataBuilder) {
        if (packet.contains(TcpPacket.class)) {
            TcpPacket tcpPacket = packet.get(TcpPacket.class);
            short srcPortNum = tcpPacket.getHeader().getSrcPort().value();
            short dstPortNum = tcpPacket.getHeader().getDstPort().value();
            String srcPortStr = tcpPacket.getHeader().getSrcPort().valueAsString();
            String dstPortStr = tcpPacket.getHeader().getDstPort().valueAsString();

            packetMetadataBuilder.srcPortNum(srcPortNum).dstPortNum(dstPortNum)
                    .srcPortStr(srcPortStr).dstPortStr(dstPortStr);
        }
    }

    private void extractUdpMetadata(Packet packet, PacketMetadata.Builder packetMetadataBuilder) {
        if (packet.contains(UdpPacket.class)) {
            UdpPacket udpPacket = packet.get(UdpPacket.class);
            short srcPortNum = udpPacket.getHeader().getSrcPort().value();
            short dstPortNum = udpPacket.getHeader().getDstPort().value();
            String srcPortStr = udpPacket.getHeader().getSrcPort().valueAsString();
            String dstPortStr = udpPacket.getHeader().getDstPort().valueAsString();

            packetMetadataBuilder.srcPortNum(srcPortNum).dstPortNum(dstPortNum)
                    .srcPortStr(srcPortStr).dstPortStr(dstPortStr);
        }
    }

}
