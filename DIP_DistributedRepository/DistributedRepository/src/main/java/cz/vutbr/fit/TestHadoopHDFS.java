package cz.vutbr.fit;

import org.apache.hadoop.fs.FileStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.hadoop.fs.FsShell;

import java.util.UUID;

//@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class TestHadoopHDFS implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestHadoopHDFS.class);

    // Hadoop 2.7 is not compatible with Java 9
    // https://issues.apache.org/jira/browse/HADOOP-14586
    private static final String JAVA_VERSION = "java.version";

    static {
        if ("9".equals(System.getProperty(JAVA_VERSION))) {
            System.setProperty(JAVA_VERSION, "1.9");
        }
    }

    @Autowired
    private org.apache.hadoop.conf.Configuration configuration;
    @Autowired
    private FsShell shell;

    private org.apache.hadoop.fs.FsShell hdfsShell;

    public static void main(String[] args) {
        new SpringApplicationBuilder(TestHadoopHDFS.class)
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .build()
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.debug("         BEFORE PUT");
        for (FileStatus s : shell.lsr("tmp/")) {
            LOGGER.debug("> " + s.getPath() + " " + s.getPermission());
        }

        //shell.mkdir("tmp");
        UUID id = UUID.randomUUID();
        String localName = "velky_cap.cap";
        String hdfsName = "tmp/velky_cap" + id.toString() + ".cap";
        shell.put(localName, hdfsName);
        shell.get(hdfsName, localName + "_1");

        LOGGER.debug("         After PUT and GET / Before RM");
        for (FileStatus s : shell.lsr("tmp/")) {
            LOGGER.debug("> " + s.getPath() + " " + s.getPermission());
        }

        shell.rm(hdfsName);

        LOGGER.debug("         After RM");
        for (FileStatus s : shell.lsr("tmp/")) {
            LOGGER.debug("> " + s.getPath() + " " + s.getPermission());
        }

        //hdfsShell = new org.apache.hadoop.fs.FsShell(configuration);
        //int code = hdfsShell.run(new String[]{ "-put", "install.sh", "/user/install.sh" });
        //LOGGER.debug("> Code " + code);
    }

}
