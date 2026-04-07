package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ConfigManager {

    private static final String CONFIG_FILE = "db_config.properties";

    private static File getConfigFile() {
        return new File(CONFIG_FILE);
    }

    // ===== SAVE =====
    public static void save(
            String dbType,
            String host,
            String port,
            String dbName,
            String username,
            String password,
            String tableName,
            String mobileColumn) throws Exception {

        Properties props = new Properties();
        props.setProperty("db.type", dbType);
        props.setProperty("db.host", host);
        props.setProperty("db.port", port);
        props.setProperty("db.name", dbName);
        props.setProperty("db.username", username);
        props.setProperty("db.password", password);
        props.setProperty("db.table", tableName);
        props.setProperty("db.mobile.column", mobileColumn);

        try (FileOutputStream fos = new FileOutputStream(getConfigFile())) {
            props.store(fos, "Saved DB Config");
        }
    }

    // ===== LOAD ALL =====
    public static Properties load() throws Exception {
        File file = getConfigFile();
        if (!file.exists()) return null;

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            props.load(fis);
        }
        return props;
    }

    // ===== GET SINGLE VALUE =====
    public static String get(String key) {
        try {
            Properties props = load();
            return props != null ? props.getProperty(key) : null;
        } catch (Exception e) {
            return null;
        }
    }

    // ===== CLEAR =====
    public static void clear() {
        File file = getConfigFile();
        if (file.exists()) {
            file.delete();
        }
    }
}
