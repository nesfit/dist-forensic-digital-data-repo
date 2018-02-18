package cz.vutbr.fit;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DistributedRepositorySpringBoot implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DistributedRepositorySpringBoot.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
