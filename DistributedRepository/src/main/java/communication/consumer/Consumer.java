package communication.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class Consumer<K, V> extends AbstractConsumer<K, V> {

    private KafkaConsumer<K, V> consumer;

    public Consumer(Class<?> keyDeserializerClass, Class<?> valueDeserializerClass) {
        Properties properties = setUpProperties(keyDeserializerClass, valueDeserializerClass);
        consumer = new KafkaConsumer<>(properties);
    }

    public void subscribe(String topicName) {
        consumer.subscribe(Arrays.asList(topicName));
    }

    public void consume(ConsumerHandler<K, V> handler) {
        while (true) {
            ConsumerRecords<K, V> records = consumer.poll(100);
            for (ConsumerRecord<K, V> record : records) {
                System.out.println("partition = " + record.partition() + ", offset = " + record.offset() +
                        ", key = " + record.key() + ", value = " + record.value());
                handler.handleRequest(record.key(), record.value());
            }
        }
    }

}
