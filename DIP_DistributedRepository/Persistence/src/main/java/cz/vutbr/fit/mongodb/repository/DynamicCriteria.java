package cz.vutbr.fit.mongodb.repository;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Consumer;

public interface DynamicCriteria {

    public Flux<PacketMetadata> findByDynamicCriteria(CriteriaDefinition criteriaDefinition);

    public Criteria appendCriteria(Criteria criteriaBuilder,
                                   String field,
                                   String operationName,
                                   boolean arrayRequired,
                                   Object value,
                                   List<Object> values,
                                   Consumer<? super Throwable> onError);

}
