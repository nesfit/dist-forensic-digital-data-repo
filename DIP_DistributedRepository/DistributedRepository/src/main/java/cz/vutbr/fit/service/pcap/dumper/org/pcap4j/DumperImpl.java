package cz.vutbr.fit.service.pcap.dumper.org.pcap4j;

import cz.vutbr.fit.service.pcap.dumper.PcapDumper;
import cz.vutbr.fit.service.pcap.parser.OnFailureCallback;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.namednumber.DataLinkType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DumperImpl implements PcapDumper<byte[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumperImpl.class);

    private PcapHandle outputHandle;
    private org.pcap4j.core.PcapDumper dumper;

    @Override
    public void initDumper(String path, OnFailureCallback onFailureCallback) {
        try {
            outputHandle = Pcaps.openDead(DataLinkType.EN10MB, 65536);
            dumper = outputHandle.dumpOpen(path);
        } catch (PcapNativeException | NotOpenException exception) {
            LOGGER.error(exception.getMessage(), exception);
            onFailureCallback.doOnFailure(exception);
        }
    }

    @Override
    public void dumpOutput(byte[] packet, OnFailureCallback onFailureCallback) {
        try {
            // TODO: Dumper logs packets, disable it.
            // TODO: Add timestamp, call dumpRaw(byte[] packet, Instant timestamp).
            dumper.dumpRaw(packet);
        } catch (NotOpenException exception) {
            LOGGER.error(exception.getMessage(), exception);
            onFailureCallback.doOnFailure(exception);
        }
    }

}
