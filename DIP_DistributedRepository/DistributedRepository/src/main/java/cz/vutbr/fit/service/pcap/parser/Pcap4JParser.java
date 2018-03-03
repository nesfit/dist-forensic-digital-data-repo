package cz.vutbr.fit.service.pcap.parser;

import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.util.concurrent.TimeoutException;

public class Pcap4JParser implements PcapParser<Packet> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Pcap4JParser.class);

    @Override
    public void parseInput(String path, OnPacketCallback<Packet> onPacketCallback,
                           OnCompleteCallback onCompleteCallback, OnFailureCallback onFailureCallback) {

        PcapHandle handle;

        try {
            handle = Pcaps.openOffline(path);
        } catch (PcapNativeException exception) {
            LOGGER.error(exception.getMessage(), exception);
            onFailureCallback.doOnFailure(exception);
            return;
        }

        while (true) {
            try {
                Packet packet = handle.getNextPacketEx();
                onPacketCallback.doOnPacket(packet);
            } catch (EOFException exception) {
                onCompleteCallback.doOnComplete();
                break;
            } catch (TimeoutException | NotOpenException | PcapNativeException exception) {
                LOGGER.error(exception.getMessage(), exception);
                onFailureCallback.doOnFailure(exception);
                break;
            }
        }

        handle.close();
    }

}
