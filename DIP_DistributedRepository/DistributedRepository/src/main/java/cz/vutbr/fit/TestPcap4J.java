package cz.vutbr.fit;

import cz.vutbr.fit.service.pcap.parser.Pcap4JParser;
import org.pcap4j.core.*;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Checks if packet.getRawData() works as expected.
 * The method loadAndSavePcapFile loops all packets inside pcap file, each packet
 * is dumped inside output pcap file using dumper.dumpRaw(packet.getRawData()).
 */
public class TestPcap4J {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestPcap4J.class);

    private PcapDumper dumper;
    private PcapHandle outputHandle;

    public TestPcap4J(String output) {
        try {
            outputHandle = Pcaps.openDead(DataLinkType.EN10MB, 65536);
            dumper = outputHandle.dumpOpen(output);
        } catch (PcapNativeException | NotOpenException e) {
            handleError(e);
        }
    }

    public void loadAndSavePcapFile(String input) throws IOException {
        Pcap4JParser pcapParser = new Pcap4JParser();
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

    public static void main(String[] args) {
        if (args.length != 2) {
            System.exit(1);
        }
        try {
            TestPcap4J testPcap4J = new TestPcap4J(args[1]);
            testPcap4J.loadAndSavePcapFile(args[0]);
        } catch (IOException e) {
            handleError(e);
        }
    }

    public static void handleError(Throwable throwable) {
        throwable.printStackTrace();
    }

}
