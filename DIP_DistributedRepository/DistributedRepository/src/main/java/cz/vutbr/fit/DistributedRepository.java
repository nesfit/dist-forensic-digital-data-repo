package cz.vutbr.fit;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.command.builder.KafkaRequestCommandBuilder;
import cz.vutbr.fit.communication.consumer.Consumer;
import cz.vutbr.fit.communication.consumer.handler.HandlerManager;
import cz.vutbr.fit.communication.consumer.handler.StorePcapHandler;
import cz.vutbr.fit.communication.serialization.KafkaRequestDeserializer;
import cz.vutbr.fit.properties.Properties;
import cz.vutbr.fit.properties.PropertyConstants;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

@Deprecated
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
