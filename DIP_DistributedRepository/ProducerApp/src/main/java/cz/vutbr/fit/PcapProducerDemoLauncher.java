package cz.vutbr.fit;

import cz.vutbr.fit.demo.LoadPcapProducerDemo;
import cz.vutbr.fit.demo.StorePcapProducerDemo;
import cz.vutbr.fit.util.JavaEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class PcapProducerDemoLauncher implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(PcapProducerDemoLauncher.class);

    static {
        JavaEnvironment.SetUp();
    }

    @Autowired
    private StorePcapProducerDemo storePcapProducerDemo;
    @Autowired
    private LoadPcapProducerDemo loadPcapProducerDemo;

    @Override
    public void run(String... args) throws Exception {
        if (args.length != 1) {
            System.exit(99);
        }
        String directoryWithPcaps = args[0];
        storePcapProducerDemo.runMultipleProducer(directoryWithPcaps);

        loadPcapProducerDemo.runProducer();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(PcapProducerDemoLauncher.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

}
