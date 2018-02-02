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

    public Producer(Class<?> keySerializerClass, Class<?> valueSerializerClass) {
        Properties properties = setUpProperties(keySerializerClass, valueSerializerClass);
        producer = new KafkaProducer<>(properties);
    }

    public void produce(ProducerRecord<K, V> record, Callback callback) {
        debug(record);
        producer.send(record, callback);
    }

    public void produce(ProducerRecord<K, V> record) {
        debug(record);
        producer.send(record);
    }

    public void close() {
        producer.close();
    }

    private void debug(ProducerRecord<K, V> record) {
        String msg = String.format("key = %s | value = %s\n", record.key(), record.value());
        LOGGER.error(msg);
        System.out.println(msg);
    }

}
