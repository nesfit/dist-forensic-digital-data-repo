package service.pcap.org.pcap4j;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import service.pcap.IPcapParser;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class PcapParser implements IPcapParser {

    @Override
    public List<Packet> parseInput(String path) throws IOException {

        PcapHandle handle;
        List<Packet> packets = new ArrayList<>();

        try {
            handle = Pcaps.openOffline(path);
        } catch (PcapNativeException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                Packet packet = handle.getNextPacketEx();
                packets.add(packet);
            } catch (EOFException e) {
                break;
            } catch (TimeoutException e) {
                e.printStackTrace();
                break;
            } catch (PcapNativeException e) {
                e.printStackTrace();
                break;
            } catch (NotOpenException e) {
                e.printStackTrace();
                break;
            }
        }

        handle.close();

        return packets;
    }

}
