package common.properties;

public class Properties {

    private static volatile Properties instance = null;

    private PropertiesLoader propertiesLoader;

    private Properties() {
        propertiesLoader = new PropertiesLoader(PropertyConstants.CONFIG_FILE);
    }

    public static Properties getInstance() {
        if (instance == null)
            synchronized (Properties.class) {
                if (instance == null)
                    instance = new Properties();
            }
        return instance;
    }

    public String loadProperty(String property) {
        return (String) propertiesLoader.loadProperty(property);
    }

}
