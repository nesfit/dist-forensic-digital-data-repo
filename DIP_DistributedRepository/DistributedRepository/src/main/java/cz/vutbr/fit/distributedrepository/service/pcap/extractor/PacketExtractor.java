package cz.vutbr.fit.distributedrepository.service.pcap.extractor;

@FunctionalInterface
public interface PacketExtractor<T, B> {

    public void extractMetadata(T packet, B packetMetadataBuilder);

}
