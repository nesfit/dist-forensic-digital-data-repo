package cz.vutbr.fit.service.pcap.extractor.org.pcap4j.beans;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.service.pcap.extractor.PacketExtractor;
import cz.vutbr.fit.service.pcap.extractor.org.pcap4j.BasePacketExtractor;
import cz.vutbr.fit.service.pcap.extractor.org.pcap4j.EthernetPacketExtractor;
import cz.vutbr.fit.service.pcap.extractor.org.pcap4j.IpPacketExtractor;
import cz.vutbr.fit.service.pcap.extractor.org.pcap4j.TransportPacketExtractor;
import org.pcap4j.core.PcapPacket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExtractorBeans {

    @Bean
    public PacketExtractor<PcapPacket, PacketMetadata.Builder> basePacketExtractor() {
        return new BasePacketExtractor();
    }

    @Bean
    public PacketExtractor<PcapPacket, PacketMetadata.Builder> ethernetPacketExtractor() {
        return new EthernetPacketExtractor();
    }

    @Bean
    public PacketExtractor<PcapPacket, PacketMetadata.Builder> ipPacketExtractor() {
        return new IpPacketExtractor();
    }

    @Bean
    public PacketExtractor<PcapPacket, PacketMetadata.Builder> transportPacketExtractor() {
        return new TransportPacketExtractor();
    }

}
