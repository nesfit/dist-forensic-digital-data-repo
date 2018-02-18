package cz.vutbr.fit;

import common.properties.Properties;
import communication.KafkaRequest;
import communication.command.Command;
import communication.command.builder.KafkaRequestCommandBuilder;
import communication.consumer.Consumer;
import communication.consumer.handler.HandlerManager;
import communication.serialization.KafkaRequestDeserializer;
import cz.vutbr.fit.common.properties.PropertyConstants;
import cz.vutbr.fit.communication.consumer.handler.StorePcapHandler;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

public class DistributedRepository {

    public static void main(String[] args) {

        HandlerManager<KafkaRequest, byte[]> handlerManager = new HandlerManager<>();
        handlerManager.attachHandler(Command.STORE_PCAP, new StorePcapHandler());

        String topic = Properties.getInstance().loadProperty(PropertyConstants.INPUT_TOPIC);
        Consumer<KafkaRequest, byte[]> consumer =
                new Consumer.Builder<KafkaRequest, byte[]>(KafkaRequestDeserializer.class, ByteArrayDeserializer.class)
                        .handlerManager(handlerManager).commandBuilder(new KafkaRequestCommandBuilder())
                        .topic(topic).debug(true).build();
        consumer.subscribe();
        consumer.consume();
    }

}