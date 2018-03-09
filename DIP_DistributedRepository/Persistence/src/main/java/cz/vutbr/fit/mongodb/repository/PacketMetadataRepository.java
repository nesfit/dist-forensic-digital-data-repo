package cz.vutbr.fit.mongodb.repository;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PacketMetadataRepository extends ReactiveCrudRepository<PacketMetadata, String>, DynamicCriteria {

}
