package communication.consumer;

import communication.consumer.handler.IConsumerHandler;
import communication.consumer.handler.IConsumerHandlerFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class Consumer<K, V> extends AbstractConsumer<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private IConsumerHandlerFactory<K> consumerHandlerFactory;
    private KafkaConsumer<K, V> consumer;
    private IConsumerHandler<K, V> handler;

    public Consumer(Class<?> keyDeserializerClass, Class<?> valueDeserializerClass) {
        Properties properties = setUpProperties(keyDeserializerClass, valueDeserializerClass);
        consumer = new KafkaConsumer<>(properties);
    }

    public void subscribe(String topicName) {
        consumer.subscribe(Arrays.asList(topicName));
    }

    public void consume() {
        while (true) {
            ConsumerRecords<K, V> records = consumer.poll(
                    Long.parseLong(common.properties.Properties.getInstance().loadProperty("poll.timeout")));
            for (ConsumerRecord<K, V> record : records) {
                debug(record);
                handler = consumerHandlerFactory.getConsumerHandler(record.key());
                handler.handleRequest(record.key(), record.value());
            }
        }
    }

    public void setConsumerHandlerFactory(IConsumerHandlerFactory<K> consumerHandlerFactory) {
        this.consumerHandlerFactory = consumerHandlerFactory;
    }

    private void debug(ConsumerRecord<K, V> record) {
        String msg = String.format("partition = %d | offset = %d | key = %s | value = %s\n",
                record.partition(), record.offset(), record.key(), record.value());
        LOGGER.error(msg);
        System.out.println(msg);
    }

}
