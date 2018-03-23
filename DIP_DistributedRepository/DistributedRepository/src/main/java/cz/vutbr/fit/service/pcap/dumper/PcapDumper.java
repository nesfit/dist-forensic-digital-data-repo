package cz.vutbr.fit.service.pcap.dumper;

import cz.vutbr.fit.service.pcap.parser.OnFailureCallback;

import java.time.Instant;

public interface PcapDumper<T> {

    public void initDumper(String path, OnFailureCallback onFailureCallback);

    public void dumpOutput(T packet, Instant timestamp, OnFailureCallback onFailureCallback);

    public void closeDumper();

}
