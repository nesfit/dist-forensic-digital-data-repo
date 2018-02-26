package cz.vutbr.fit.service.pcap.parser;

@FunctionalInterface
public interface OnPacketCallback<T> {

    void doOnPacket(T packet);

}