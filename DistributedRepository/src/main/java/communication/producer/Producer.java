package communication.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Producer<K, V> {

    private KafkaProducer<K, V> producer;

    public Producer(Class<?> keySerializerClass, Class<?> valueSerializerClass) {
        Properties properties = setUpProperties(keySerializerClass, valueSerializerClass);
        producer = new KafkaProducer<>(properties);
    }

    private Properties setUpProperties(Class<?> keySerializerClass, Class<?> valueSerializerClass) {
        Properties props = new Properties();
        setProperty(props, ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);
        setProperty(props, ProducerConfig.ACKS_CONFIG);
        setProperty(props, ProducerConfig.RETRIES_CONFIG);
        setProperty(props, ProducerConfig.BATCH_SIZE_CONFIG);
        setProperty(props, ProducerConfig.BUFFER_MEMORY_CONFIG);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass.getCanonicalName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass.getCanonicalName());
        return props;
    }

    protected void setProperty(Properties props, String property) {
        props.put(property, common.properties.Properties.getInstance().loadProperty(property));
    }

    public void produce(ProducerRecord<K, V> record, Callback callback) {
        producer.send(record, callback);
    }

    public void close() {
        producer.close();
    }

}
