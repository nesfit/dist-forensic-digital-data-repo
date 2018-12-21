package cz.vutbr.fit.producerdemo.demo;

import cz.vutbr.fit.communication.KafkaCriteria;
import cz.vutbr.fit.communication.KafkaRequest;
import cz.vutbr.fit.communication.MetadataOperation;
import cz.vutbr.fit.communication.command.Command;
import cz.vutbr.fit.communication.command.DataSource;
import cz.vutbr.fit.communication.command.DataSourceStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class LoadPcapProducerDemo extends BaseProducerDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadPcapProducerDemo.class);

    @Value("${cz.vutbr.fit.producerdemo.demo.LoadPcapProducerDemo.dataSourceStorage}")
    private DataSourceStorage dataSourceStorage;

    public void runProducer() {
        try {

            UUID requestId = UUID.randomUUID();
            DataSource dataSource = createDataSource(dataSourceStorage, requestId.toString(), Boolean.FALSE);
            KafkaRequest request = buildKafkaRequestWithCriterias(dataSource, Command.LOAD_PCAP, requestId, tcpAndPortCriteria());
            byte[] payload = null;

            producer.produce(inputTopic, request, payload,
                    kafkaRequestSendResult -> LOGGER.info(kafkaRequestSendResult.getRecordMetadata().toString()),
                    BaseProducerDemo::handleError);

        } catch (Exception exception) {
            handleError(exception);
        }
    }

    private List<KafkaCriteria> ipv6Criteria() {
        List<KafkaCriteria> criteria = new ArrayList<>();
        KafkaCriteria ipVersionName = new KafkaCriteria.Builder()
                .field("ipVersionName")
                .operation(MetadataOperation.EQ)
                .value("IPv6")
                .build();
        KafkaCriteria dstIpAddress = null;
        try {
            dstIpAddress = new KafkaCriteria.Builder()
                    .field("dstIpAddress")
                    .operation(MetadataOperation.EQ)
                    .value(InetAddress.getByName("ff02:0:0:0:0:0:0:c").getHostAddress())
                    .build();
        } catch (UnknownHostException exception) {
            handleError(exception);
        }
        criteria.add(ipVersionName);
        criteria.add(dstIpAddress);
        return criteria;
    }

    private List<KafkaCriteria> tcpAndPortCriteria() {
        List<KafkaCriteria> criteria = new ArrayList<>();
        KafkaCriteria tcp = new KafkaCriteria.Builder()
                .field("ipProtocolName")
                .operation(MetadataOperation.EQ)
                .value("TCP")
                .build();
        KafkaCriteria portLte443 = new KafkaCriteria.Builder()
                .field("dstPort")
                .operation(MetadataOperation.IN)
                .values(Arrays.asList(443, 80))
                .build();
        criteria.add(tcp);
        criteria.add(portLte443);
        return criteria;
    }

}
