package cz.vutbr.fit.distributedrepository.service.pcap.parser.pcap4j;

import cz.vutbr.fit.distributedrepository.service.pcap.parser.OnCompleteCallback;
import cz.vutbr.fit.distributedrepository.service.pcap.parser.OnFailureCallback;
import cz.vutbr.fit.distributedrepository.service.pcap.parser.OnPacketCallback;
import cz.vutbr.fit.distributedrepository.service.pcap.parser.PcapParser;
import org.pcap4j.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.util.concurrent.TimeoutException;

public class ParserImpl implements PcapParser<PcapPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserImpl.class);

    @Override
    public void parseInput(String path, OnPacketCallback<PcapPacket> onPacketCallback,
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
                PcapPacket packet = handle.getNextPacketEx();
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
