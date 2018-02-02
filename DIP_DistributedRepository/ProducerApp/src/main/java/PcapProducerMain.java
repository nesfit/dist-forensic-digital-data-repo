import common.properties.PathResolver;
import common.properties.Properties;
import common.properties.PropertyConstants;
import communication.KafkaRequest;
import communication.KafkaResponse;
import communication.command.Command;
import communication.command.DataType;
import communication.command.Operation;
import communication.command.builder.KafkaResponseCommandBuilder;
import communication.consumer.Consumer;
import communication.consumer.handler.AcknowledgementConsumerHandler;
import communication.consumer.handler.HandlerManager;
import communication.producer.Producer;
import communication.serialization.KafkaRequestSerializer;
import communication.serialization.KafkaResponseDeserializer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class PcapProducerMain {

    private static class Callback implements org.apache.kafka.clients.producer.Callback {

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (exception != null) {
                exception.printStackTrace();
            }
        }

    }

    private static final String PCAP_FILE = ".pcap";
    private static final String CAP_FILE = ".cap";
    private String outputTopic = Properties.getInstance().loadProperty(PropertyConstants.OUTPUT_TOPIC);
    private String inputTopic = Properties.getInstance().loadProperty(PropertyConstants.INPUT_TOPIC);

    public void runMultipleProducer() {
        File directory = null;
        try {
            directory = PathResolver.resolveFilePath(".").toFile();
            Arrays.stream(directory.listFiles())
                    .filter(file -> file.getName().contains(PCAP_FILE) || file.getName().contains(CAP_FILE))
                    .forEach(file -> runProducer(file.getName()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void runProducer(String file) {
        try {
            byte[] bytes = Files.readAllBytes(PathResolver.resolveFilePath(file));
            System.out.println(new Date() + "\t" + file + "\t Size: " + bytes.length);

            KafkaRequest request = new KafkaRequest.Builder()
                    .operation(Operation.STORE).dataType(DataType.PCAP).awaitsResponse(Boolean.TRUE)
                    .responseTopic(outputTopic).id(UUID.randomUUID()).build();

            Producer<KafkaRequest, byte[]> producer =
                    new Producer.Builder<KafkaRequest, byte[]>(KafkaRequestSerializer.class, ByteArraySerializer.class)
                            .debug(false).build();
            producer.produce(new ProducerRecord<>(inputTopic, request, bytes), new Callback());
            producer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void runConsumer() {
        HandlerManager<KafkaResponse, byte[]> handlerManager = new HandlerManager<>();
        handlerManager.attachHandler(Command.HANDLE_RESPONSE, new AcknowledgementConsumerHandler());

        Consumer<KafkaResponse, byte[]> consumer =
                new Consumer.Builder<KafkaResponse, byte[]>(KafkaResponseDeserializer.class, ByteArrayDeserializer.class)
                        .handlerManager(handlerManager).commandBuilder(new KafkaResponseCommandBuilder())
                        .topic(outputTopic).debug(false).build();
        consumer.subscribe();
        consumer.consume();
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        PcapProducerMain pcapProducer = new PcapProducerMain();
        new Thread(pcapProducer::runMultipleProducer).start();
        //new Thread(() -> pcapProducer.runProducer("dns.pcap")).start();
        new Thread(pcapProducer::runConsumer).start();
    }

}
