package communication.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

public abstract class AbstractConsumer<K, V> {

    protected Properties setUpProperties(Class<?> keyDeserializerClass, Class<?> valueDeserializerClass) {
        Properties props = new Properties();
        setProperty(props, ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG);
        setProperty(props, ConsumerConfig.GROUP_ID_CONFIG);
        setProperty(props, ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG);
        setProperty(props, ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG);
        setProperty(props, ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG);
        setProperty(props, ConsumerConfig.FETCH_MAX_BYTES_CONFIG);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerClass.getCanonicalName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializerClass.getCanonicalName());
        return props;
    }

    protected void setProperty(Properties props, String property) {
        props.put(property, common.properties.Properties.getInstance().loadProperty(property));
    }

}
