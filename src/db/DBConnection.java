package db;

import java.sql.Connection;
import util.ConfigManager;

public class DBConnection {

    public static Connection getConnection() throws Exception {

        String dbType = ConfigManager.get("db.type");

        if ("mysql".equalsIgnoreCase(dbType)) {
            return MySQLConnector.getConnection();
        }

        if ("sqlserver".equalsIgnoreCase(dbType)) {
            return SQLServerConnector.getConnection();
        }

        throw new RuntimeException("Unsupported DB type: " + dbType);
    }
}
