import common.properties.PathResolver;
import io.pkts.PacketHandler;
import io.pkts.Pcap;
import io.pkts.PcapOutputStream;
import io.pkts.packet.Packet;
import io.pkts.sdp.RTPInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CoreExample002 {

    public CoreExample002() {
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        Pcap pcap = Pcap.openStream(PathResolver.resolveFilePathName("dns.pcap"));

        File outputFile = new File("udp.pcap");
        final PcapOutputStream out = pcap.createOutputStream(new FileOutputStream(outputFile));
        final List<RTPInfo> rtpInfo = new ArrayList();
        pcap.loop(new PacketHandler() {
            public boolean nextPacket(Packet packet) throws IOException {
                //if (packet.hasProtocol(Protocol.TCP)) {
                //UDPPacket udp = (UDPPacket)packet.getPacket(Protocol.UDP);
                System.out.println(packet.toString());
                out.write(packet);
                //}
                return true;
            }
        });
        out.flush();
        out.close();
    }
}
