package cz.vutbr.fit.distributedrepository.service.pcap.parser;

@FunctionalInterface
public interface OnPacketCallback<T> {

    void doOnPacket(T packet);

}