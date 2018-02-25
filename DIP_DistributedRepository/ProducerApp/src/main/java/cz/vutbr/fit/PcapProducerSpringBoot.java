package cz.vutbr.fit;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.producer.KafkaProducer;
import cz.vutbr.fit.properties.Properties;
import cz.vutbr.fit.properties.PropertyConstants;
import cz.vutbr.fit.stats.CollectStats;
import cz.vutbr.fit.stats.FileStats;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
public class PcapProducerSpringBoot implements CommandLineRunner {

    private static final String PCAP_FILE = ".pcap";
    private static final String CAP_FILE = ".cap";
    private String outputTopic = Properties.getInstance().loadProperty(PropertyConstants.OUTPUT_TOPIC);
    private String inputTopic = Properties.getInstance().loadProperty(PropertyConstants.INPUT_TOPIC);

    private final Lock _mutex = new ReentrantLock(Boolean.TRUE);

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

            _mutex.lock();
            System.out.println("\tLocked: sending file " + file);

            byte[] bytes = Files.readAllBytes(Paths.get(file));
            KafkaRequest request = new KafkaRequest.Builder().command(Command.STORE_PCAP)
                    .awaitsResponse(Boolean.TRUE).responseTopic(outputTopic).id(UUID.randomUUID()).build();

            FileStats fileStats = new FileStats.Builder().filename(file).startTime(new Date()).build();
            CollectStats.getInstance().appendFile(request.getId(), fileStats);

            //producer.produce(inputTopic, request, bytes);

            producer.produce(inputTopic, request, bytes, result -> {
                _mutex.unlock();
                System.out.println("\t Unlocked: file " + file + " sent successfully");
            }, Throwable::printStackTrace);

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
