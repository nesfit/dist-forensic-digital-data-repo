package cz.vutbr.fit.service.pcap.dumper.beans;

import cz.vutbr.fit.service.pcap.dumper.Pcap4JDumper;
import cz.vutbr.fit.service.pcap.dumper.PcapDumper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DumperBeans {

    @Bean
    public PcapDumper<byte[]> pcapDumper() {
        return new Pcap4JDumper();
    }

}
