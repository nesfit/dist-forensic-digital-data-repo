package communication;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class KafkaValue<T> {

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public static class Builder<T> {

        private KafkaValue<T> kafkaValue;

        public Builder() {
            kafkaValue = new KafkaValue<>();
        }

        public Builder<T> value(T value) {
            kafkaValue.setValue(value);
            return this;
        }

        public KafkaValue<T> build() {
            return kafkaValue;
        }

    }

}
