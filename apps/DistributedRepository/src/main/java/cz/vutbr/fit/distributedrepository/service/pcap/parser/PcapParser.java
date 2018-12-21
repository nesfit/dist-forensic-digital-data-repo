package cz.vutbr.fit.distributedrepository.service.pcap.parser;

@FunctionalInterface
public interface PcapParser<T> {

    public void parseInput(String path, OnPacketCallback<T> onPacketCallback,
                           OnCompleteCallback onCompleteCallback, OnFailureCallback onFailureCallback);

}
