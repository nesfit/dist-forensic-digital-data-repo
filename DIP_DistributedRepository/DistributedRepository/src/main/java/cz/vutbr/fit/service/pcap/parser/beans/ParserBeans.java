package cz.vutbr.fit.service.pcap.parser.beans;

import cz.vutbr.fit.service.pcap.parser.Pcap4JParser;
import cz.vutbr.fit.service.pcap.parser.PcapParser;
import org.pcap4j.packet.Packet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParserBeans {

    @Bean
    public PcapParser<Packet> pcapParser() {
        return new Pcap4JParser();
    }

}
