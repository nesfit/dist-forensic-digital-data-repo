package cz.vutbr.fit.service.pcap;

@FunctionalInterface
public interface OnPacketCallback<T> {

    void processPacket(T packet);

}