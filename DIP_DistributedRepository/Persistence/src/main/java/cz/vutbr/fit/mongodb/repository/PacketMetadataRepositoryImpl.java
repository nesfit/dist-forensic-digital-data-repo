package cz.vutbr.fit.mongodb.repository;

import cz.vutbr.fit.mongodb.entity.PacketMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;

public class PacketMetadataRepositoryImpl implements DynamicCriteria {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<PacketMetadata> findByDynamicCriteria(CriteriaDefinition criteriaDefinition) {
        Query query = new Query();
        query.addCriteria(criteriaDefinition);
        return reactiveMongoTemplate.find(query, PacketMetadata.class);
    }

    @Override
    public Criteria appendCriteria(Criteria criteriaBuilder, String field, String operationName, boolean arrayRequired, Object value, List<Object> values, Consumer<? super Throwable> onError) {
        String and = "and";
        try {
            Method whichField = criteriaBuilder.getClass().getMethod(and, String.class);
            criteriaBuilder = (Criteria) whichField.invoke(criteriaBuilder, field);
            Method operation = getMethodOperation(criteriaBuilder, operationName, arrayRequired);
            criteriaBuilder = invokeMethodOperation(operation, criteriaBuilder, arrayRequired, value, values);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            onError.accept(exception);
        }
        return criteriaBuilder;
    }

    private Method getMethodOperation(Criteria criteriaBuilder, String operationName, boolean arrayRequired) throws NoSuchMethodException {
        Class<?> paramType = arrayRequired ? Object[].class : Object.class;
        return criteriaBuilder.getClass().getMethod(operationName, paramType);
    }

    private Criteria invokeMethodOperation(Method operation, Criteria criteriaBuilder, boolean arrayRequired, Object value, List<Object> values)
            throws InvocationTargetException, IllegalAccessException {
        if (arrayRequired) {
            return (Criteria) operation.invoke(criteriaBuilder, new Object[]{values.toArray()});
        } else {
            return (Criteria) operation.invoke(criteriaBuilder, value);
        }
    }

}
