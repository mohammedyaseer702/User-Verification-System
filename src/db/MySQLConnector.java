package db;

import java.sql.Connection;
import java.sql.DriverManager;
import util.ConfigManager;

public class MySQLConnector {

    public static Connection getConnection() throws Exception {

        String host = ConfigManager.get("db.host");
        String port = ConfigManager.get("db.port");
        String db   = ConfigManager.get("db.name");
        String user = ConfigManager.get("db.username");
        String pass = ConfigManager.get("db.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db
                   + "?useSSL=false&serverTimezone=UTC"
                   + "&allowPublicKeyRetrieval=true";

        return DriverManager.getConnection(url, user, pass);
    }
}
