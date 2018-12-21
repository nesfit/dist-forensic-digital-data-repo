package cz.vutbr.fit.producerdemo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaEnvironment {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaEnvironment.class);

    // Hadoop 2.7 is not compatible with Java 9
    // https://issues.apache.org/jira/browse/HADOOP-14586
    private static final String JAVA_VERSION = "java.version";
    private static final String JAVA_VERSION_PROPERTY_VALUE_DEFAULT = "9";
    private static final String JAVA_VERSION_PROPERTY_VALUE_CUSTOM = "1.9";

    public static void SetUp() {
        if (JAVA_VERSION_PROPERTY_VALUE_DEFAULT.equals(System.getProperty(JAVA_VERSION))) {
            System.setProperty(JAVA_VERSION, JAVA_VERSION_PROPERTY_VALUE_CUSTOM);
            LOGGER.info(JAVA_VERSION + "=" + System.getProperty(JAVA_VERSION));
        }
    }

}
