package cz.vutbr.fit.persistence.mongodb.repository;

import cz.vutbr.fit.persistence.mongodb.entity.PacketMetadata;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacketMetadataRepository extends ReactiveCrudRepository<PacketMetadata, String>, DynamicCriteria {

}
