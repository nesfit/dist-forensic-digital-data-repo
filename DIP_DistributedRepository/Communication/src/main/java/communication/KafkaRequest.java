package communication;

import communication.command.DataType;
import communication.command.Operation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

public class KafkaRequest {

    private Operation operation;
    private DataType dataType;
    private Boolean awaitsResponse;
    private String responseTopic;
    private UUID id;
    // TODO: Criteria

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Boolean getAwaitsResponse() {
        return awaitsResponse;
    }

    public void setAwaitsResponse(Boolean awaitsResponse) {
        this.awaitsResponse = awaitsResponse;
    }

    public String getResponseTopic() {
        return responseTopic;
    }

    public void setResponseTopic(String responseTopic) {
        this.responseTopic = responseTopic;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public static class Builder {

        private KafkaRequest request;

        public Builder() {
            request = new KafkaRequest();
        }

        public Builder operation(Operation operation) {
            request.setOperation(operation);
            return this;
        }

        public Builder dataType(DataType dataType) {
            request.setDataType(dataType);
            return this;
        }

        public Builder awaitsResponse(Boolean awaitsResponse) {
            request.setAwaitsResponse(awaitsResponse);
            return this;
        }

        public Builder responseTopic(String responseTopic) {
            request.setResponseTopic(responseTopic);
            return this;
        }

        public Builder id(UUID id) {
            request.setId(id);
            return this;
        }

        public KafkaRequest build() {
            return request;
        }

    }

}
