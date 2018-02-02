import common.properties.Properties;
import common.properties.PropertyConstants;
import communication.consumer.Consumer;
import communication.consumer.handler.ConsumerHandlerFactory;
import communication.request.KafkaRequest;
import communication.request.serialization.KafkaRequestDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class DistributedRepository {

    public static void main(String[] args) {
        Consumer<KafkaRequest, String> consumer = new Consumer<>(KafkaRequestDeserializer.class, StringDeserializer.class);
        consumer.subscribe(Properties.getInstance().loadProperty(PropertyConstants.INPUT_TOPIC));
        consumer.setConsumerHandlerFactory(new ConsumerHandlerFactory());
        consumer.consume();
    }

}
