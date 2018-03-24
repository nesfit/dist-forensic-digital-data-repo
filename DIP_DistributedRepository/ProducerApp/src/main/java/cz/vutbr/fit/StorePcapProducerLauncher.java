package cz.vutbr.fit;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.command.DataSource;
import cz.vutbr.fit.communication.command.DataSourceStorage;
import cz.vutbr.fit.demo.BaseProducerDemo;
import cz.vutbr.fit.util.JavaEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
public class StorePcapProducerLauncher extends BaseProducerDemo implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorePcapProducerLauncher.class);

    static {
        JavaEnvironment.SetUp();
    }

    private static final String PCAP_FILE = ".pcap";
    private static final String CAP_FILE = ".cap";

    private final Lock mutex = new ReentrantLock(Boolean.TRUE);

    @Value("${cz.vutbr.fit.StorePcapProducerLauncher.dataSourceStorage}")
    private DataSourceStorage dataSourceStorage;

    public void runMultipleProducer(String directoryName) {
        File directory = new File(directoryName);
        Assert.notNull(directory, "Directory doesn't exist");

        // TODO: Will be removed
        initStatsForDirectory(directory);

        Arrays.stream(directory.listFiles())
                .filter(this::isPcapOrCapFile)
                .forEach(file -> runProducer(directoryName + "/" + file.getName()));
    }

    private boolean isPcapOrCapFile(File file) {
        return file.getName().endsWith(PCAP_FILE) || file.getName().endsWith(CAP_FILE);
    }

    public void runProducer(String filename) {
        try {

            lockMutexForFile(filename);

            UUID requestId = UUID.randomUUID();
            DataSource dataSource = createDataSource(dataSourceStorage, requestId.toString(), Boolean.TRUE);
            byte[] bytes = preparePayload(dataSourceStorage, filename, requestId.toString());
            KafkaRequest request = buildKafkaRequest(dataSource, Command.STORE_PCAP, requestId);

            // TODO: Will be removed
            initStatsForRequest(filename, request.getId());

            producer.produce(inputTopic, request, bytes,
                    result -> unlockMutexForFile(filename),
                    BaseProducerDemo::handleError);

        } catch (Exception exception) {
            handleError(exception);
        }
    }

    private void lockMutexForFile(String filename) {
        mutex.lock();
        LOGGER.debug("Locked: sending file " + filename);
    }

    private void unlockMutexForFile(String filename) {
        mutex.unlock();
        LOGGER.debug("Unlocked: file " + filename + " sent successfully");
    }

    @Override
    public void run(String... args) {
        if (args.length != 1) {
            System.exit(99);
        }
        String directoryWithPcaps = args[0];
        runMultipleProducer(directoryWithPcaps);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(StorePcapProducerLauncher.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

}
