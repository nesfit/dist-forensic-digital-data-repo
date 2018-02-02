package common.properties;

public class Properties {

    public static final String CONFIG_FILE = "config.properties";

    private static volatile common.properties.Properties instance = null;

    private PropertiesLoader propertiesLoader;

    private Properties() {
        propertiesLoader = new PropertiesLoader(CONFIG_FILE);
    }

    public static common.properties.Properties getInstance() {
        if (instance == null)
            synchronized (common.properties.Properties.class) {
                if (instance == null)
                    instance = new common.properties.Properties();
            }
        return instance;
    }

    public String loadProperty(String property) {
        return (String) propertiesLoader.loadProperty(property);
    }

}
