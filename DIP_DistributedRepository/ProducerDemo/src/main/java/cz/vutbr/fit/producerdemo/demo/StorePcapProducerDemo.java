package cz.vutbr.fit.producerdemo.demo;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.command.DataSource;
import cz.vutbr.fit.communication.command.DataSourceStorage;
import cz.vutbr.fit.producerdemo.util.FileExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class StorePcapProducerDemo extends BaseProducerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorePcapProducerDemo.class);

    private final Lock mutex = new ReentrantLock(Boolean.TRUE);

    @Value("${cz.vutbr.fit.producerdemo.demo.StorePcapProducerDemo.dataSourceStorage}")
    private DataSourceStorage dataSourceStorage;

    public void runMultipleProducer(String directoryName) {
        File directory = new File(directoryName);

        if (!directory.isDirectory() || directory.listFiles().length == 0) {
            LOGGER.warn(String.format("%s is not a directory or is empty.", directoryName));
            LOGGER.warn("Stopping StorePcap UseCase...");
            return;
        }

        // TODO: Will be removed
        initStatsForDirectory(directory);

        Arrays.stream(directory.listFiles())
                .filter(this::isPcapOrCapFile)
                .forEach(file -> runProducer(directoryName + "/" + file.getName()));
    }

    private boolean isPcapOrCapFile(File file) {
        return file.getName().endsWith(FileExtension.PCAP.toString())
                || file.getName().endsWith(FileExtension.CAP.toString());
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

}
