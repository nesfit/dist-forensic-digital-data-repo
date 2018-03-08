package cz.vutbr.fit.service.pcap.extractor.beans;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.service.pcap.extractor.*;
import org.pcap4j.core.PcapPacket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExtractorBeans {

    @Bean
    public PacketExtractor<PcapPacket, PacketMetadata.Builder> pcap4JBaseExtractor() {
        return new Pcap4JBaseExtractor();
    }

    @Bean
    public PacketExtractor<PcapPacket, PacketMetadata.Builder> pcap4JEthernetExtractor() {
        return new Pcap4JEthernetExtractor();
    }

    @Bean
    public PacketExtractor<PcapPacket, PacketMetadata.Builder> pcap4JIpExtractor() {
        return new Pcap4JIpExtractor();
    }

    @Bean
    public PacketExtractor<PcapPacket, PacketMetadata.Builder> pcap4JTransportExtractor() {
        return new Pcap4JTransportExtractor();
    }

}
