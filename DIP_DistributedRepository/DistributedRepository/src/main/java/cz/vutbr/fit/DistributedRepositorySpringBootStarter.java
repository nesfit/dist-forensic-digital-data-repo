package cz.vutbr.fit;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
//@EnableReactiveMongoRepositories
public class DistributedRepositorySpringBootStarter implements CommandLineRunner {

    // Hadoop 2.7 is not compatible with Java 9
    // https://issues.apache.org/jira/browse/HADOOP-14586
    private static final String JAVA_VERSION = "java.version";

    static {
        if ("9".equals(System.getProperty(JAVA_VERSION))) {
            System.setProperty(JAVA_VERSION, "1.9");
            System.out.println(System.getProperty(JAVA_VERSION));
        }
    }

    @Override
    public void run(String... args) throws Exception {

    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(DistributedRepositorySpringBootStarter.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

}
