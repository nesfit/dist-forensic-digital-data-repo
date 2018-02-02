import common.properties.PathResolver;
import common.properties.Properties;
import common.properties.PropertyConstants;
import communication.command.DataType;
import communication.command.Operation;
import communication.consumer.Consumer;
import communication.consumer.handler.ConsumerHandlerFactory;
import communication.producer.Producer;
import communication.request.KafkaRequest;
import communication.request.serialization.KafkaRequestDeserializer;
import communication.request.serialization.KafkaRequestSerializer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

public class ProducerMain {

    public static void main(String[] args) throws URISyntaxException, IOException {

        new Runnable() {
            @Override
            public void run() {
                byte[] bytes;
                try {
                    bytes = Files.readAllBytes(PathResolver.resolveFilePath("dns.pcap"));
                    String value = Base64.getEncoder().encodeToString(bytes);

                    KafkaRequest request = new KafkaRequest.Builder()
                            .operation(Operation.STORE).dataType(DataType.PCAP)
                            .awaitsResponse(Boolean.TRUE)
                            .responseTopic(Properties.getInstance().loadProperty(PropertyConstants.OUTPUT_TOPIC))
                            .id(UUID.randomUUID())
                            .build();

                    Producer<KafkaRequest, String> producer = new Producer<>(KafkaRequestSerializer.class, StringSerializer.class);
                    producer.produce(new ProducerRecord<>(
                            Properties.getInstance().loadProperty(PropertyConstants.INPUT_TOPIC),
                            request, value));
                    //producer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.run();

        new Runnable() {
            @Override
            public void run() {
                Consumer<KafkaRequest, String> consumer = new Consumer<>(KafkaRequestDeserializer.class, StringDeserializer.class);
                consumer.subscribe(Properties.getInstance().loadProperty(PropertyConstants.OUTPUT_TOPIC));
                consumer.setConsumerHandlerFactory(new ConsumerHandlerFactory());
                consumer.consume();
            }
        }.run();

    }

}
