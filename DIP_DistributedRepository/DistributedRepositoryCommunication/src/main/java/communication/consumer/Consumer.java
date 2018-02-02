package communication.consumer;

import communication.command.Command;
import communication.command.builder.ICommandBuilder;
import communication.consumer.handler.HandlerManager;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class Consumer<K, V> extends AbstractConsumer<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private KafkaConsumer<K, V> consumer;
    private HandlerManager<K, V> handlerManager;
    private ICommandBuilder<K> commandBuilder;
    private String topic;
    private boolean debug;

    public Consumer(Properties properties) {
        consumer = new KafkaConsumer<>(properties);
    }

    public Consumer(Class<?> keyDeserializerClass, Class<?> valueDeserializerClass) {
        Properties properties = setUpProperties(keyDeserializerClass, valueDeserializerClass);
        consumer = new KafkaConsumer<>(properties);
    }

    public void setHandlerManager(HandlerManager<K, V> handlerManager) {
        this.handlerManager = handlerManager;
    }

    public void setCommandBuilder(ICommandBuilder<K> commandBuilder) {
        this.commandBuilder = commandBuilder;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public static class Builder<K, V> {

        private Consumer<K, V> consumer;

        public Builder(Properties properties) {
            consumer = new Consumer<K, V>(properties);
        }

        public Builder(Class<?> keyDeserializerClass, Class<?> valueDeserializerClass) {
            consumer = new Consumer<K, V>(keyDeserializerClass, valueDeserializerClass);
        }

        public Builder<K, V> handlerManager(HandlerManager<K, V> handlerManager) {
            consumer.setHandlerManager(handlerManager);
            return this;
        }

        public Builder<K, V> commandBuilder(ICommandBuilder<K> commandBuilder) {
            consumer.setCommandBuilder(commandBuilder);
            return this;
        }

        public Builder<K, V> topic(String topic) {
            consumer.setTopic(topic);
            return this;
        }

        public Builder<K, V> debug(boolean debug) {
            consumer.setDebug(debug);
            return this;
        }

        public Consumer<K, V> build() {
            return consumer;
        }

    }

    public void subscribe() {
        consumer.subscribe(Arrays.asList(topic));
    }

    public void consume() {
        while (true) {
            long timeout = Long.parseLong(common.properties.Properties.getInstance().loadProperty("poll.timeout"));
            ConsumerRecords<K, V> records = consumer.poll(timeout);
            for (ConsumerRecord<K, V> record : records) {
                if (debug)
                    debug(record);
                Command command = commandBuilder.buildCommand(record.key());
                handlerManager.handle(command, record.key(), record.value());
            }
        }
    }

    private void debug(ConsumerRecord<K, V> record) {
        String msg = String.format("partition = %d | offset = %d | key = %s | value = %s\n",
                record.partition(), record.offset(), record.key(), record.value());
        System.out.println(msg);
    }

}
