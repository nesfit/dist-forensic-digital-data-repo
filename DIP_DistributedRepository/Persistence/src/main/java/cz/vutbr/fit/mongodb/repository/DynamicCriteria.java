package cz.vutbr.fit.mongodb.repository;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import reactor.core.publisher.Flux;

public interface DynamicCriteria {

    public Flux<PacketMetadata> findByDynamicCriteria(CriteriaDefinition criteriaDefinition);

}
