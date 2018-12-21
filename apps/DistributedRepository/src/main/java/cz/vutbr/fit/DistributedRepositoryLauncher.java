package cz.vutbr.fit;

import cz.vutbr.fit.distributedrepository.util.JavaEnvironment;
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

    static {
        JavaEnvironment.SetUp();
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
