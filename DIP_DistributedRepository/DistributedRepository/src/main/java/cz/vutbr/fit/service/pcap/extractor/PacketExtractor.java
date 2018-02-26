package cz.vutbr.fit.service.pcap.extractor;

@FunctionalInterface
public interface PacketExtractor<T, B> {

    public void extractMetadata(T packet, B packetMetadataBuilder);

}
