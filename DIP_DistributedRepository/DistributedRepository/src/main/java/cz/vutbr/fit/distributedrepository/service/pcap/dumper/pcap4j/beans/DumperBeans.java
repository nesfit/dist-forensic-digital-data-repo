package cz.vutbr.fit.distributedrepository.service.pcap.dumper.pcap4j.beans;

import cz.vutbr.fit.distributedrepository.service.pcap.dumper.PcapDumper;
import cz.vutbr.fit.distributedrepository.service.pcap.dumper.pcap4j.DumperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DumperBeans {

    @Bean
    public PcapDumper<byte[]> pcapDumper() {
        return new DumperImpl();
    }

}
