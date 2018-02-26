package cz.vutbr.fit.service.pcap.parser;

import java.io.IOException;

@FunctionalInterface
public interface PcapParser<T> {

    public void parseInput(String path, OnPacketCallback<T> onPacketCallback, OnCompleteCallback onCompleteCallback) throws IOException;

}
