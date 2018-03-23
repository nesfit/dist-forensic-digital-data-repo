package cz.vutbr.fit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class DistributedRepositoryLauncher implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedRepositoryLauncher.class);

    // Hadoop 2.7 is not compatible with Java 9
    // https://issues.apache.org/jira/browse/HADOOP-14586
    private static final String JAVA_VERSION = "java.version";
    private static final String JAVA_VERSION_PROPERTY_VALUE_DEFAULT = "9";
    private static final String JAVA_VERSION_PROPERTY_VALUE_CUSTOM = "1.9";

    static {
        if (JAVA_VERSION_PROPERTY_VALUE_DEFAULT.equals(System.getProperty(JAVA_VERSION))) {
            System.setProperty(JAVA_VERSION, JAVA_VERSION_PROPERTY_VALUE_CUSTOM);
            LOGGER.info(JAVA_VERSION + "=" + System.getProperty(JAVA_VERSION));
        }
    }

    @Override
    public void run(String... args) throws Exception {

    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(DistributedRepositoryLauncher.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

}
