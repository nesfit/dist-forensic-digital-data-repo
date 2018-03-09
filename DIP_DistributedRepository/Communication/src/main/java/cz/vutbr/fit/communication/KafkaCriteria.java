package cz.vutbr.fit.communication;

import java.util.List;

public class KafkaCriteria {

    private String field;
    private MetadataOperation operation;
    private Object value;
    private List<Object> values;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public MetadataOperation getOperation() {
        return operation;
    }

    public void setOperation(MetadataOperation operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public static class Builder {

        private KafkaCriteria criteria;

        public Builder() {
            criteria = new KafkaCriteria();
        }

        public Builder field(String field) {
            criteria.setField(field);
            return this;
        }

        public Builder operation(MetadataOperation operation) {
            criteria.setOperation(operation);
            return this;
        }

        public Builder value(Object value) {
            criteria.setValue(value);
            return this;
        }

        public Builder values(List<Object> values) {
            criteria.setValues(values);
            return this;
        }

        public KafkaCriteria build() {
            return criteria;
        }

    }

}
