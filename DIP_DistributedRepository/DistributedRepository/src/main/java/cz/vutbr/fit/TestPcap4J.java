package cz.vutbr.fit;

import cz.vutbr.fit.service.pcap.parser.pcap4j.ParserImpl;
import org.pcap4j.core.*;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;

/**
 * Checks if packet.getRawData() works as expected.
 * The method loadAndSavePcapFile loops all packets inside pcap file, each packet
 * is dumped inside output pcap file using dumper.dumpRaw(packet.getRawData()).
 */
//@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class TestPcap4J implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestPcap4J.class);

    private static final String JAVA_VERSION = "java.version";
    private static final String JAVA_VERSION_PROPERTY_VALUE_DEFAULT = "9";
    private static final String JAVA_VERSION_PROPERTY_VALUE_CUSTOM = "1.9";

    static {
        if (JAVA_VERSION_PROPERTY_VALUE_DEFAULT.equals(System.getProperty(JAVA_VERSION))) {
            System.setProperty(JAVA_VERSION, JAVA_VERSION_PROPERTY_VALUE_CUSTOM);
            LOGGER.info(JAVA_VERSION + "=" + System.getProperty(JAVA_VERSION));
        }
    }

    private PcapDumper dumper;
    private PcapHandle outputHandle;

    public void initDumper(String output) {
        try {
            outputHandle = Pcaps.openDead(DataLinkType.EN10MB, 65536);
            dumper = outputHandle.dumpOpen(output);
        } catch (PcapNativeException | NotOpenException e) {
            handleError(e);
        }
    }

    public void loadAndSavePcapFile(String input) throws IOException {
        ParserImpl pcapParser = new ParserImpl();
        pcapParser.parseInput(input, this::doOnPacket, () -> {
            LOGGER.debug("Completed");
        }, TestPcap4J::handleError);
    }

    public void doOnPacket(PcapPacket packet) {
        try {
            dumper.dumpRaw(packet.getRawData(), packet.getTimestamp());
            //dumper.dump(packet);
        } catch (NotOpenException e) {
            handleError(e);
        }
    }

    public static void handleError(Throwable throwable) {
        LOGGER.error(throwable.getMessage(), throwable);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestPcap4J.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length != 2) {
            System.exit(1);
        }
        try {
            initDumper(args[1]);
            loadAndSavePcapFile(args[0]);
        } catch (IOException e) {
            handleError(e);
        }
    }

}
