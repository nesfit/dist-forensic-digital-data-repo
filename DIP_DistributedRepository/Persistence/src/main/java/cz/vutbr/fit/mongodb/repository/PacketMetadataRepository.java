package cz.vutbr.fit.mongodb.repository;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PacketMetadataRepository extends ReactiveCrudRepository<PacketMetadata, String> {

    Flux<PacketMetadata> findBySrcIpAddress(String srcIpAddress);

    Flux<PacketMetadata> findByDstIpAddress(String dstIpAddress);

    Flux<PacketMetadata> findBySrcIpAddressAndDstIpAddress(String srcIpAddress, String dstIpAddress);

}
