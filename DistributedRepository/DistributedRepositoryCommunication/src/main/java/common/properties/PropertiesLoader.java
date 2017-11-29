package common.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private Properties prop;
    private InputStream input;

    public PropertiesLoader(String propertyFile) {
        prop = new Properties();
        input = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFile);
        try {
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Object loadProperty(String propertyName) {
        return prop.getProperty(propertyName);
    }

}
