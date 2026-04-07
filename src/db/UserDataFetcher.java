package db;

import java.sql.*;
import java.util.*;

public class UserDataFetcher {

    public static Map<String, String> fetchByMobile(
            Connection con,
            String tableName,
            String mobileColumn,
            String mobileValue) throws Exception {

        if (con == null)
            throw new Exception("Database connection is null");

        String sql = "SELECT * FROM " + tableName +
                     " WHERE " + mobileColumn + " = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, mobileValue);

        ResultSet rs = ps.executeQuery();

        Map<String, String> data = new LinkedHashMap<>();

        if (rs.next()) {
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();

            for (int i = 1; i <= cols; i++) {
                data.put(meta.getColumnName(i), rs.getString(i));
            }
        }

        rs.close();
        ps.close();

        return data;
    }

    public static List<Map<String, String>> fetchAll(
            Connection con,
            String tableName) throws Exception {

        if (con == null)
            throw new Exception("Database connection is null");

        String sql = "SELECT * FROM " + tableName;

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        List<Map<String, String>> list = new ArrayList<>();

        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();

        while (rs.next()) {
            Map<String, String> row = new LinkedHashMap<>();
            for (int i = 1; i <= cols; i++) {
                row.put(meta.getColumnName(i), rs.getString(i));
            }
            list.add(row);
        }

        rs.close();
        ps.close();

        return list;
    }
}
