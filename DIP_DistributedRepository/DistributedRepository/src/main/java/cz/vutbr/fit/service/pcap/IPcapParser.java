package cz.vutbr.fit.service.pcap;

import java.io.IOException;

@FunctionalInterface
public interface IPcapParser<T> {

    public void parseInput(String path, OnPacketCallback<T> onPacketCallback) throws IOException;

}
