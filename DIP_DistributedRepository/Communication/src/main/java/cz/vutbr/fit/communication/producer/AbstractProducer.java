package cz.vutbr.fit.communication.producer;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class AbstractProducer<K, V> {

    protected Properties setUpProperties(Class<?> keySerializerClass, Class<?> valueSerializerClass) {
        Properties props = new Properties();
        setProperty(props, ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);
        setProperty(props, ProducerConfig.ACKS_CONFIG);
        setProperty(props, ProducerConfig.RETRIES_CONFIG);
        setProperty(props, ProducerConfig.BATCH_SIZE_CONFIG);
        setProperty(props, ProducerConfig.BUFFER_MEMORY_CONFIG);
        setProperty(props, ProducerConfig.MAX_REQUEST_SIZE_CONFIG);
        setProperty(props, ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass.getCanonicalName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass.getCanonicalName());
        return props;
    }

    protected void setProperty(Properties props, String property) {
        props.put(property, cz.vutbr.fit.properties.Properties.getInstance().loadProperty(property));
    }

}
