package cz.vutbr.fit;

import common.properties.Properties;
import communication.KafkaRequest;
import communication.command.Command;
import cz.vutbr.fit.common.properties.PropertyConstants;
import cz.vutbr.fit.communication.producer.KafkaProducer;
import cz.vutbr.fit.stats.CollectStats;
import cz.vutbr.fit.stats.FileStats;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;
import java.io.IOException;
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
        CollectStats.getInstance().setCountOfFiles(directory.listFiles().length);
        Arrays.stream(directory.listFiles())
                .filter(file -> file.getName().contains(PCAP_FILE) || file.getName().contains(CAP_FILE))
                .forEach(file -> runProducer(directoryName + "/" + file.getName()));
    }

    public void runProducer(String file) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file));
            KafkaRequest request = new KafkaRequest.Builder().command(Command.STORE_PCAP)
                    .awaitsResponse(Boolean.TRUE).responseTopic(outputTopic).id(UUID.randomUUID()).build();

            FileStats fileStats = new FileStats();
            fileStats.setFilename(file);
            fileStats.setStartTime(new Date());
            CollectStats.getInstance().appendFile(request.getId(), fileStats);

            producer.produce(new ProducerRecord<>(inputTopic, request, bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(PcapProducerSpringBoot.class)
                .web(WebApplicationType.NONE).bannerMode(Banner.Mode.OFF).build().run(args);
    }

    @Override
    public void run(String... args) {
        if (args.length != 1) {
            System.exit(99);
        }
        //runProducer("target/classes/PCAP/1_youtube.pcap");
        runMultipleProducer(args[0]);
    }

}
