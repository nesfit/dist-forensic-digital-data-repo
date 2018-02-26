package cz.vutbr.fit;

import cz.vutbr.fit.service.pcap.parser.Pcap4JParser;
import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.DataLinkType;

import java.io.IOException;

/**
 * Checks if packet.getRawData() works as expected.
 * The method loadAndSavePcapFile loops all packets inside pcap file, each packet
 * is dumped inside output pcap file using dumper.dumpRaw(packet.getRawData()).
 */
public class TestPcap4J {

    private PcapDumper dumper;
    private PcapHandle outputHandle;

    public TestPcap4J(String output) {
        try {
            outputHandle = Pcaps.openDead(DataLinkType.EN10MB, 65536);
            dumper = outputHandle.dumpOpen(output);
        } catch (PcapNativeException | NotOpenException e) {
            e.printStackTrace();
        }
    }

    public void loadAndSavePcapFile(String input) throws IOException {
        Pcap4JParser pcapParser = new Pcap4JParser();
        pcapParser.parseInput(input, this::doOnPacket, () -> {
        });
    }

    public void doOnPacket(Packet packet) {
        try {
            dumper.dumpRaw(packet.getRawData());
        } catch (NotOpenException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

}
