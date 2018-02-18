package cz.vutbr.fit;

import common.properties.Properties;
import communication.KafkaRequest;
import communication.command.Command;
import communication.command.DataType;
import communication.command.Operation;
import cz.vutbr.fit.common.properties.PropertyConstants;
import cz.vutbr.fit.communication.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class PcapProducerSpringBoot implements CommandLineRunner {

    private static final String PCAP_FILE = ".pcap";
    private static final String CAP_FILE = ".cap";
    private String outputTopic = Properties.getInstance().loadProperty(PropertyConstants.OUTPUT_TOPIC);
    private String inputTopic = Properties.getInstance().loadProperty(PropertyConstants.INPUT_TOPIC);

    @Autowired
    KafkaProducer producer;

    public void runMultipleProducer(String directoryName) {
        File directory = new File(directoryName);
        Arrays.stream(directory.listFiles())
                .filter(file -> file.getName().contains(PCAP_FILE) || file.getName().contains(CAP_FILE))
                .forEach(file -> runProducer(directoryName + "/" + file.getName()));
    }

    public void runProducer(String file) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            System.out.println(new Date() + "\t" + file + "\t Size: " + bytes.length);
            /*.operation(Operation.STORE).dataType(DataType.PCAP)*/
            KafkaRequest request = new KafkaRequest.Builder().command(Command.STORE_PCAP)
                    .awaitsResponse(Boolean.TRUE).responseTopic(outputTopic).id(UUID.randomUUID()).build();

            producer.produce(new ProducerRecord<>(inputTopic, request, bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(PcapProducerSpringBoot.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length != 1) {
            System.exit(99);
        }
        //runProducer("target/classes/PCAP/1_youtube.pcap");
        runMultipleProducer(args[0]);
    }

}
