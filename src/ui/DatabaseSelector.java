package ui;

import db.DBConnection;
import util.ConfigManager;

import javax.swing.*;
import java.sql.Connection;
import java.util.Properties;

public class DatabaseSelector extends JFrame {

    private JComboBox<String> dbTypeCombo;
    private JTextField hostField;
    private JTextField portField;
    private JTextField dbNameField;
    private JTextField userField;
    private JPasswordField passField;
    private JTextField tableNameField;
    private JTextField mobileColumnField;

    public DatabaseSelector() {

        setTitle("Database Selection");
        setSize(420, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // ===== DB TYPE =====
        JLabel dbTypeLbl = new JLabel("Database Type:");
        dbTypeLbl.setBounds(30, 30, 120, 25);
        add(dbTypeLbl);

        dbTypeCombo = new JComboBox<>(new String[]{"mysql", "sqlserver"});
        dbTypeCombo.setBounds(160, 30, 180, 25);
        add(dbTypeCombo);

        // ===== HOST =====
        JLabel hostLbl = new JLabel("DB Host:");
        hostLbl.setBounds(30, 70, 120, 25);
        add(hostLbl);

        hostField = new JTextField();
        hostField.setBounds(160, 70, 180, 25);
        add(hostField);

        // ===== PORT =====
        JLabel portLbl = new JLabel("DB Port:");
        portLbl.setBounds(30, 110, 120, 25);
        add(portLbl);

        portField = new JTextField();
        portField.setBounds(160, 110, 180, 25);
        add(portField);

        // ===== DB NAME =====
        JLabel dbNameLbl = new JLabel("Database Name:");
        dbNameLbl.setBounds(30, 150, 120, 25);
        add(dbNameLbl);

        dbNameField = new JTextField();
        dbNameField.setBounds(160, 150, 180, 25);
        add(dbNameField);

        // ===== USER =====
        JLabel userLbl = new JLabel("Username:");
        userLbl.setBounds(30, 190, 120, 25);
        add(userLbl);

        userField = new JTextField();
        userField.setBounds(160, 190, 180, 25);
        add(userField);

        // ===== PASSWORD =====
        JLabel passLbl = new JLabel("Password:");
        passLbl.setBounds(30, 230, 120, 25);
        add(passLbl);

        passField = new JPasswordField();
        passField.setBounds(160, 230, 180, 25);
        add(passField);

        // ===== TABLE NAME =====
        JLabel tableLbl = new JLabel("Table Name:");
        tableLbl.setBounds(30, 270, 120, 25);
        add(tableLbl);

        tableNameField = new JTextField();
        tableNameField.setBounds(160, 270, 180, 25);
        add(tableNameField);

        // ===== MOBILE COLUMN =====
        JLabel colLbl = new JLabel("Mobile Column:");
        colLbl.setBounds(30, 310, 120, 25);
        add(colLbl);

        mobileColumnField = new JTextField();
        mobileColumnField.setBounds(160, 310, 180, 25);
        add(mobileColumnField);

        // ===== BUTTONS =====
        JButton connectBtn = new JButton("Connect");
        connectBtn.setBounds(80, 360, 120, 30);
        add(connectBtn);

        JButton clearBtn = new JButton("Clear Saved Config");
        clearBtn.setBounds(210, 360, 160, 30);
        add(clearBtn);

        connectBtn.addActionListener(e -> connectDatabase());
        clearBtn.addActionListener(e -> clearSavedConfig());

        loadSavedConfig();

        setVisible(true);
    }

    // ================= LOAD CONFIG =================
    private void loadSavedConfig() {
        try {
            Properties props = ConfigManager.load();
            if (props == null) return;

            dbTypeCombo.setSelectedItem(props.getProperty("db.type", "mysql"));
            hostField.setText(props.getProperty("db.host", ""));
            portField.setText(props.getProperty("db.port", ""));
            dbNameField.setText(props.getProperty("db.name", ""));
            userField.setText(props.getProperty("db.username", ""));
            passField.setText(props.getProperty("db.password", ""));
            tableNameField.setText(props.getProperty("db.table", ""));
            mobileColumnField.setText(props.getProperty("db.mobile.column", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CLEAR CONFIG =================
    private void clearSavedConfig() {
        ConfigManager.clear();

        hostField.setText("");
        portField.setText("");
        dbNameField.setText("");
        userField.setText("");
        passField.setText("");
        tableNameField.setText("");
        mobileColumnField.setText("");

        JOptionPane.showMessageDialog(this, "Saved config cleared");
    }

    // ================= CONNECT =================
    private void connectDatabase() {

        try {
            ConfigManager.save(
                    dbTypeCombo.getSelectedItem().toString(),
                    hostField.getText().trim(),
                    portField.getText().trim(),
                    dbNameField.getText().trim(),
                    userField.getText().trim(),
                    new String(passField.getPassword()),
                    tableNameField.getText().trim(),
                    mobileColumnField.getText().trim()
            );

            Connection con = DBConnection.getConnection();

            JOptionPane.showMessageDialog(this,
                    "Connected Successfully");

            new VerificationScreen(con,
                    tableNameField.getText().trim(),
                    mobileColumnField.getText().trim());

            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Connection Failed!\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DatabaseSelector::new);
    }
}
