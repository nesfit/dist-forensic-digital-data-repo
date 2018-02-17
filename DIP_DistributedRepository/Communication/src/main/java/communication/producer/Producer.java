package communication.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer<K, V> extends AbstractProducer<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    private KafkaProducer<K, V> producer;
    private boolean debug;

    public Producer(Properties properties) {
        producer = new KafkaProducer<>(properties);
    }

    public Producer(Class<?> keySerializerClass, Class<?> valueSerializerClass) {
        Properties properties = setUpProperties(keySerializerClass, valueSerializerClass);
        producer = new KafkaProducer<>(properties);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public static class Builder<K, V> {

        private Producer<K, V> producer;

        public Builder(Properties properties) {
            producer = new Producer<K, V>(properties);
        }

        public Builder(Class<?> keySerializerClass, Class<?> valueSerializerClass) {
            producer = new Producer<K, V>(keySerializerClass, valueSerializerClass);
        }

        public Builder<K, V> debug(boolean debug) {
            producer.setDebug(debug);
            return this;
        }

        public Producer<K, V> build() {
            return producer;
        }

    }

    public void produce(ProducerRecord<K, V> record, Callback callback) {
        if (debug)
            debug(record);
        producer.send(record, callback);
    }

    public void produce(ProducerRecord<K, V> record) {
        if (debug)
            debug(record);
        producer.send(record);
    }

    public void close() {
        producer.close();
    }

    private void debug(ProducerRecord<K, V> record) {
        String msg = String.format("key = %s | value = %s\n", record.key(), record.value());
        System.out.println(msg);
    }

}
