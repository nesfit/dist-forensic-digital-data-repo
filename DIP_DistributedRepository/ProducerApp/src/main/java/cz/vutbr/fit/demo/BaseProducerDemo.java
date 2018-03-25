package cz.vutbr.fit.demo;

import cz.vutbr.fit.communication.KafkaCriteria;
import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.command.DataSource;
import cz.vutbr.fit.communication.command.DataSourceStorage;
import cz.vutbr.fit.communication.producer.KafkaProducer;
import cz.vutbr.fit.stats.CollectStats;
import cz.vutbr.fit.stats.FileStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.hadoop.fs.FsShell;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class BaseProducerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseProducerDemo.class);

    @Value("${input.topic}")
    protected String inputTopic;
    @Value("${output.topic}")
    protected String outputTopic;
    @Value("${error.topic}")
    protected String errorTopic;

    @Autowired
    protected KafkaProducer producer;

    @Autowired
    protected FsShell hdfsShell;

    protected KafkaRequest buildKafkaRequest(DataSource dataSource, Command command, UUID requestId) {
        return new KafkaRequest.Builder()
                .command(command)
                .dataSource(dataSource)
                .awaitsResponse(Boolean.TRUE)
                .responseTopic(outputTopic)
                .errorTopic(errorTopic)
                .id(requestId)
                .build();
    }

    protected KafkaRequest buildKafkaRequestWithCriterias(DataSource dataSource,
                                                          Command command, UUID requestId,
                                                          List<KafkaCriteria> criterias) {
        return new KafkaRequest.Builder()
                .command(command)
                .dataSource(dataSource)
                .awaitsResponse(Boolean.TRUE)
                .responseTopic(outputTopic)
                .errorTopic(errorTopic)
                .id(requestId)
                .criterias(criterias)
                .build();
    }

    protected static DataSource createDataSource(DataSourceStorage dataSourceStorage, String uri, boolean removeAfterUse) {
        return (dataSourceStorage == DataSourceStorage.HADOOP) ?
                new DataSource(dataSourceStorage, uri, removeAfterUse) :
                new DataSource(dataSourceStorage);
    }

    protected byte[] preparePayload(DataSourceStorage dataSourceStorage, String localFile, String dstFile) throws IOException {
        byte[] bytes = null;
        if (dataSourceStorage == DataSourceStorage.HADOOP) {
            hdfsShell.put(localFile, dstFile);
        } else {
            bytes = Files.readAllBytes(Paths.get(localFile));
        }
        return bytes;
    }

    protected static void handleError(Throwable throwable) {
        LOGGER.error(throwable.getMessage(), throwable);
    }

    @Deprecated
    protected static void initStatsForDirectory(File directory) {
        CollectStats.getInstance().setCountOfFiles(directory.listFiles().length);
    }

    @Deprecated
    protected static void initStatsForRequest(String file, UUID requestId) {
        FileStats fileStats = new FileStats.Builder().filename(file).startTime(new Date()).build();
        CollectStats.getInstance().appendFile(requestId, fileStats);
    }

}
