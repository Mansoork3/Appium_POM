package com.saucedemo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader
 * ------------
 * Reads values from config.properties so we never hard-code
 * device settings, credentials, or URLs inside test code.
 *
 * Usage:
 *   String url = ConfigReader.get("appium.server.url");
 */
public class ConfigReader {

    // Single Properties object shared by the whole test run
    private static final Properties properties = new Properties();

    // Static block: runs once when the class is first loaded
    static {
        loadProperties();
    }

    // -------------------------------------------------------
    // Load the file from classpath OR from a known file path
    // -------------------------------------------------------
    private static void loadProperties() {
        // Try classpath first (works when running via Gradle)
        InputStream classpathStream =
                ConfigReader.class.getClassLoader()
                        .getResourceAsStream("config.properties");

        if (classpathStream != null) {
            try {
                properties.load(classpathStream);
                System.out.println("[ConfigReader] Loaded config.properties from classpath.");
            } catch (IOException e) {
                throw new RuntimeException("[ConfigReader] Failed to load config.properties from classpath.", e);
            }
        } else {
            // Fallback: load from project root relative path
            String fallbackPath = "app/src/main/resources/config.properties";
            try (FileInputStream fileStream = new FileInputStream(fallbackPath)) {
                properties.load(fileStream);
                System.out.println("[ConfigReader] Loaded config.properties from: " + fallbackPath);
            } catch (IOException e) {
                throw new RuntimeException("[ConfigReader] Could not find config.properties at: " + fallbackPath, e);
            }
        }
    }

    // -------------------------------------------------------
    // Public getter
    // -------------------------------------------------------
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException(
                    "[ConfigReader] Key not found in config.properties: '" + key + "'");
        }
        return value.trim();
    }

    // -------------------------------------------------------
    // Convenience getters with type conversion
    // -------------------------------------------------------
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
