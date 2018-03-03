package cz.vutbr.fit;

import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.command.DataSource;
import cz.vutbr.fit.communication.command.DataSourceStorage;
import cz.vutbr.fit.communication.producer.KafkaProducer;
import cz.vutbr.fit.stats.CollectStats;
import cz.vutbr.fit.stats.FileStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.hadoop.fs.FsShell;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
public class PcapProducerSpringBoot implements CommandLineRunner {

    // Hadoop 2.7 is not compatible with Java 9
    // https://issues.apache.org/jira/browse/HADOOP-14586
    private static final String JAVA_VERSION = "java.version";

    static {
        if ("9".equals(System.getProperty(JAVA_VERSION))) {
            System.setProperty(JAVA_VERSION, "1.9");
            System.out.println(System.getProperty(JAVA_VERSION));
        }
    }

    private static final String PCAP_FILE = ".pcap";
    private static final String CAP_FILE = ".cap";

    private final Lock _mutex = new ReentrantLock(Boolean.TRUE);

    @Autowired
    private KafkaProducer producer;

    @Value("${input.topic}")
    private String inputTopic;
    @Value("${output.topic}")
    private String outputTopic;
    @Value("${error.topic}")
    private String errorTopic;

    @Value("${dataSourceStorage}")
    private DataSourceStorage dataSourceStorage;

    @Autowired
    protected FsShell hdfsShell;

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

            UUID requestId = UUID.randomUUID();
            DataSource dataSource = null;
            byte[] bytes = null;

            if (dataSourceStorage == DataSourceStorage.HADOOP) {
                hdfsShell.put(file, requestId.toString());
                dataSource = new DataSource(dataSourceStorage, requestId.toString(), true);
            } else {
                bytes = Files.readAllBytes(Paths.get(file));
                dataSource = new DataSource(dataSourceStorage, null, false);
            }

            KafkaRequest request = new KafkaRequest.Builder().command(Command.STORE_PCAP)
                    .dataSource(dataSource).awaitsResponse(Boolean.TRUE)
                    .responseTopic(outputTopic).errorTopic(errorTopic)
                    .id(requestId).build();

            FileStats fileStats = new FileStats.Builder().filename(file).startTime(new Date()).build();
            CollectStats.getInstance().appendFile(request.getId(), fileStats);

            producer.produce(inputTopic, request, bytes, result -> {
                _mutex.unlock();
                System.out.println("\t Unlocked: file " + file + " sent successfully");
            }, Throwable::printStackTrace);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) {
        if (args.length != 1) {
            System.exit(99);
        }
        runMultipleProducer(args[0]);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(PcapProducerSpringBoot.class)
                .web(WebApplicationType.NONE).bannerMode(Banner.Mode.OFF).build().run(args);
    }

}
