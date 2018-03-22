package cz.vutbr.fit.service.pcap.parser.org.pcap4j.beans;

import cz.vutbr.fit.service.pcap.parser.PcapParser;
import cz.vutbr.fit.service.pcap.parser.org.pcap4j.ParserImpl;
import org.pcap4j.core.PcapPacket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserBeans {

    @Bean
    public PcapParser<PcapPacket> pcapParser() {
        return new ParserImpl();
    }

}
