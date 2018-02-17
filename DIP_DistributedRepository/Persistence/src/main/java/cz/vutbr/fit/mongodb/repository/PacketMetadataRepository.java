package cz.vutbr.fit.mongodb.repository;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.springframework.data.repository.CrudRepository;

public interface PacketMetadataRepository extends CrudRepository<PacketMetadata, String> {

}
