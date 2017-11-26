import common.properties.PathResolver;
import common.properties.Properties;
import common.properties.PropertyConstants;
import communication.command.DataType;
import communication.command.Operation;
import communication.producer.Producer;
import communication.request.KafkaRequest;
import communication.request.serialization.KafkaRequestSerializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Base64;

public class ProducerMain {

    private static class CallBackImpl implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                System.err.println(e);
            } else {
                System.out.println(recordMetadata.partition() + " " + recordMetadata.offset()
                        + " " + recordMetadata.timestamp());
            }
        }
    }

    public static void main(String[] args) throws URISyntaxException, IOException {

        byte[] bytes = Files.readAllBytes(PathResolver.resolveFilePath("dns.pcap"));
        String value = Base64.getEncoder().encodeToString(bytes);

        KafkaRequest<String> request = new KafkaRequest.Builder<String>()
                .operation(Operation.STORE).dataType(DataType.PCAP).value(value).build();

        Producer<String, KafkaRequest<String>> producer = new Producer<>(StringSerializer.class, KafkaRequestSerializer.class);
        producer.produce(new ProducerRecord<>(
                Properties.getInstance().loadProperty(PropertyConstants.PACKET_TOPIC),
                "req", request), new CallBackImpl()
        );

    }

}
