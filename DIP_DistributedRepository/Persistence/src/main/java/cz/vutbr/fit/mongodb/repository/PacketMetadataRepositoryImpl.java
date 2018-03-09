package cz.vutbr.fit.mongodb.repository;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

public class PacketMetadataRepositoryImpl implements DynamicCriteria {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<PacketMetadata> findByDynamicCriteria(CriteriaDefinition criteriaDefinition) {
        Query query = new Query();
        query.addCriteria(criteriaDefinition);
        return reactiveMongoTemplate.find(query, PacketMetadata.class);
    }
}
