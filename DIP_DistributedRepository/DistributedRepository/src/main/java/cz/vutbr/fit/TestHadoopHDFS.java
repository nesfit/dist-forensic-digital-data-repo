package cz.vutbr.fit;

import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.hadoop.fs.FsShell;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class TestHadoopHDFS implements CommandLineRunner {

    // Hadoop 2.7 is not compatible with Java 9
    // https://issues.apache.org/jira/browse/HADOOP-14586
    private static final String JAVA_VERSION = "java.version";
    static {
        if ("9".equals(System.getProperty(JAVA_VERSION))) {
            System.setProperty(JAVA_VERSION, "1.9");
            System.out.println(System.getProperty(JAVA_VERSION));
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
        for (FileStatus s : shell.lsr("/")) {
            System.out.println("> " + s.getPath() + " " + s.getPermission());
        }

        //shell.mkdir("tmp");
        //shell.put("install.sh", "tmp/install.on.hdfs-20-54.sh");
        shell.get("tmp/install.on.hdfs-20-54.sh", "install.on.hdfs-20-54.sh");

        //shell.put("install.sh", "/user/install.on.hdfs.sh");
        //shell.get("input/core-site.xml", "core-site-from-hdfs.xml");
        //shell.copyToLocal("input/core-site.xml", "core-site-from-hdfs-copyToLocal.xml");

        //hdfsShell = new org.apache.hadoop.fs.FsShell(configuration);
        //int code = hdfsShell.run(new String[]{ "-put", "install.sh", "/user/install.sh" });
        //System.out.println("> Code " + code);
    }

}
