package communication.request;

import communication.command.DataType;
import communication.command.Operation;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class KafkaRequest<T> {

    private Operation operation;
    private DataType dataType;
    private T value;

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

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class Builder<T> {

        private KafkaRequest<T> request;

        public Builder() {
            request = new KafkaRequest<>();
        }

        public Builder<T> operation(Operation operation) {
            request.setOperation(operation);
            return this;
        }

        public Builder<T> dataType(DataType dataType) {
            request.setDataType(dataType);
            return this;
        }

        public Builder<T> value(T value) {
            request.setValue(value);
            return this;
        }

        public KafkaRequest<T> build() {
            return request;
        }

    }

}
