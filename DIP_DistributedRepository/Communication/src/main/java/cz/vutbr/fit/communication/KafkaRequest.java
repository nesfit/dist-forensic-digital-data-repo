package cz.vutbr.fit.communication;

import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.command.DataSource;
import cz.vutbr.fit.communication.command.DataType;
import cz.vutbr.fit.communication.command.Operation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.UUID;

public class KafkaRequest {

    @Deprecated
    private Operation operation;
    @Deprecated
    private DataType dataType;

    private Command command;
    private DataSource dataSource;

    private Boolean awaitsResponse;
    private String responseTopic;
    private String errorTopic;

    private UUID id;
    // TODO: Criteria

    @Deprecated
    public Operation getOperation() {
        return operation;
    }

    @Deprecated
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Deprecated
    public DataType getDataType() {
        return dataType;
    }

    @Deprecated
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
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

    public String getErrorTopic() {
        return errorTopic;
    }

    public void setErrorTopic(String errorTopic) {
        this.errorTopic = errorTopic;
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

        @Deprecated
        public Builder operation(Operation operation) {
            request.setOperation(operation);
            return this;
        }

        @Deprecated
        public Builder dataType(DataType dataType) {
            request.setDataType(dataType);
            return this;
        }

        public Builder command(Command command) {
            request.setCommand(command);
            return this;
        }

        public Builder dataSource(DataSource dataSource) {
            request.setDataSource(dataSource);
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

        public Builder errorTopic(String errorTopic) {
            request.setErrorTopic(errorTopic);
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
