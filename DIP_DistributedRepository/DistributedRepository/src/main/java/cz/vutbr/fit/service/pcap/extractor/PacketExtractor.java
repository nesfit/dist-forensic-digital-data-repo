package cz.vutbr.fit.service.pcap.extractor;

public abstract class PacketExtractor<T, B> {

    protected PacketExtractor<T, B> successor;

    public abstract void extractMetadata(T packet, B packetMetadataBuilder);

    public void setSuccessor(PacketExtractor<T, B> successor) {
        this.successor = successor;
    }
}
