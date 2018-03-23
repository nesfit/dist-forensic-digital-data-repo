package cz.vutbr.fit;

import cz.vutbr.fit.communication.KafkaCriteria;
import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.MetadataOperation;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class LoadPcapProducerLauncher extends BaseProducerDemo implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadPcapProducerLauncher.class);

    static {
        JavaEnvironment.SetUp();
    }

    @Value("${cz.vutbr.fit.LoadPcapProducerLauncher.dataSourceStorage}")
    private DataSourceStorage dataSourceStorage;

    public void runProducer() {
        try {

            UUID requestId = UUID.randomUUID();
            DataSource dataSource = createDataSource(dataSourceStorage, requestId.toString(), Boolean.FALSE);
            KafkaRequest request = buildKafkaRequestWithCriterias(dataSource, Command.LOAD_PCAP, requestId, ipv6Criteria());
            byte[] payload = null;

            producer.produce(inputTopic, request, payload,
                    kafkaRequestSendResult -> LOGGER.info(kafkaRequestSendResult.toString()),
                    BaseProducerDemo::handleError);

        } catch (Exception exception) {
            handleError(exception);
        }
    }

    private List<KafkaCriteria> ipv6Criteria() {
        List<KafkaCriteria> criteria = new ArrayList<>();
        KafkaCriteria ipVersionName = new KafkaCriteria.Builder()
                .field("ipVersionName").operation(MetadataOperation.EQ).value("IPv6").build();
        KafkaCriteria dstIpAddress = new KafkaCriteria.Builder()
                .field("dstIpAddress").operation(MetadataOperation.EQ).value("ff02:0:0:0:0:0:0:c").build();
        criteria.add(ipVersionName);
        criteria.add(dstIpAddress);
        return criteria;
    }

    @Override
    public void run(String... args) throws Exception {
        runProducer();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(LoadPcapProducerLauncher.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

}
