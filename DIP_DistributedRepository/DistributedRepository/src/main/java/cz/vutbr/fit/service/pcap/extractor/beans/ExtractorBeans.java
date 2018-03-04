package cz.vutbr.fit.service.pcap.extractor.beans;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import cz.vutbr.fit.service.pcap.extractor.PacketExtractor;
import cz.vutbr.fit.service.pcap.extractor.Pcap4JEthernetExtractor;
import cz.vutbr.fit.service.pcap.extractor.Pcap4JIpExtractor;
import cz.vutbr.fit.service.pcap.extractor.Pcap4JTransportExtractor;
import org.pcap4j.packet.Packet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExtractorBeans {

    @Bean
    public PacketExtractor<Packet, PacketMetadata.Builder> pcap4JTransportExtractor() {
        return new Pcap4JTransportExtractor();
    }

    @Bean
    public PacketExtractor<Packet, PacketMetadata.Builder> pcap4JIpExtractor() {
        return new Pcap4JIpExtractor();
    }

    @Bean
    public PacketExtractor<Packet, PacketMetadata.Builder> pcap4JEthernetExtractor() {
        return new Pcap4JEthernetExtractor();
    }

}
