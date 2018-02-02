package service.pcap.io.pkts;

import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.packet.Packet;
import service.pcap.IPcapParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PcapParser implements IPcapParser {

    @Override
    public List<?> parseInput(String path) throws IOException {
        Pcap pcap = Pcap.openStream(path);
        CollectPacketHandler handler = new CollectPacketHandler();
        pcap.loop(handler);
        return handler.getPackets();
    }

    private class CollectPacketHandler implements PacketHandler {

        List<Packet> packets = new ArrayList<>();

        @Override
        public boolean nextPacket(Packet packet) throws IOException {
            packets.add(packet);
            return true;
        }

        public List<Packet> getPackets() {
            return packets;
        }

    }

}
