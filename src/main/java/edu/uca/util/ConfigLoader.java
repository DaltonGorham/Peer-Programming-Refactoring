package edu.uca.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static ConfigLoader instance;
    private static final Properties prop = new Properties();

    static {
        try (InputStream in = ConfigLoader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in == null) {
                throw new FileNotFoundException("Property file 'application.properties' not found in the classpath");
            }

            prop.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    public static ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    public String getProperty(String key) {
        return prop.getProperty(key);
    }

    public void setProperty(String key, String value) {
        prop.setProperty(key, value);
    }
}
