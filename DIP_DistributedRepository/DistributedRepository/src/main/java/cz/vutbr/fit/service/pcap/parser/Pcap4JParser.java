package cz.vutbr.fit.service.pcap.parser;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Pcap4JParser implements PcapParser<Packet> {

    @Override
    public void parseInput(String path, OnPacketCallback<Packet> onPacketCallback, OnCompleteCallback onCompleteCallback) throws IOException {

        PcapHandle handle;

        try {
            handle = Pcaps.openOffline(path);
        } catch (PcapNativeException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                Packet packet = handle.getNextPacketEx();
                onPacketCallback.doOnPacket(packet);
            } catch (EOFException e) {
                onCompleteCallback.doOnComplete();
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
    }

}
