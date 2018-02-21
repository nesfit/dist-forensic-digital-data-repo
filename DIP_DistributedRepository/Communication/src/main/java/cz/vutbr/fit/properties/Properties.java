package cz.vutbr.fit.properties;

public class Properties {

    public static final String CONFIG_FILE = "config.properties";

    private static volatile cz.vutbr.fit.properties.Properties instance = null;

    private PropertiesLoader propertiesLoader;

    private Properties() {
        propertiesLoader = new PropertiesLoader(CONFIG_FILE);
    }

    public static cz.vutbr.fit.properties.Properties getInstance() {
        if (instance == null)
            synchronized (cz.vutbr.fit.properties.Properties.class) {
                if (instance == null)
                    instance = new cz.vutbr.fit.properties.Properties();
            }
        return instance;
    }

    public String loadProperty(String property) {
        return (String) propertiesLoader.loadProperty(property);
    }

}
