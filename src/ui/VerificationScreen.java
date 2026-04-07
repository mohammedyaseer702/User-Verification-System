package ui;

import db.UserDataFetcher;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import java.util.ArrayList;

public class VerificationScreen extends JFrame {

    private final Connection con;
    private final String tableName;
    private final String mobileColumn;

    private List<Map<String, String>> records;
    private int currentIndex = 0;

    private JPanel formPanel;
    private JTextField searchField;
    private JTextArea remarksArea;

    private Preferences prefs;

    public VerificationScreen(Connection con, String tableName, String mobileColumn) {
        this.con = con;
        this.tableName = tableName;
        this.mobileColumn = mobileColumn;
        this.prefs = Preferences.userNodeForPackage(VerificationScreen.class);

        setTitle("User Verification");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ---------- TOP PANEL ----------
        JPanel topPanel = new JPanel(new GridLayout(2, 4, 10, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        topPanel.add(new JLabel("Table:"));
        JTextField tableField = new JTextField(tableName);
        tableField.setEditable(false);
        topPanel.add(tableField);

        topPanel.add(new JLabel("Mobile Column:"));
        JTextField mobileField = new JTextField(mobileColumn);
        mobileField.setEditable(false);
        topPanel.add(mobileField);

        topPanel.add(new JLabel("Search Mobile:"));
        searchField = new JTextField();
        topPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        topPanel.add(searchBtn);

        add(topPanel, BorderLayout.NORTH);

        // ---------- FORM PANEL ----------
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane scrollPane = new JScrollPane(formPanel);
        add(scrollPane, BorderLayout.CENTER);

        // ---------- REMARKS PANEL ----------
        JPanel remarksPanel = new JPanel(new BorderLayout(5, 5));
        remarksPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        remarksPanel.add(new JLabel("Remarks:"), BorderLayout.NORTH);

        remarksArea = new JTextArea(3, 50);
        remarksArea.setLineWrap(true);
        remarksArea.setWrapStyleWord(true);

        JScrollPane remarksScroll = new JScrollPane(remarksArea);
        remarksPanel.add(remarksScroll, BorderLayout.CENTER);

        // ---------- BUTTON PANEL ----------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton backBtn = new JButton("Back");
        JButton acceptBtn = new JButton("Accept");
        JButton rejectBtn = new JButton("Reject");
        JButton nextBtn = new JButton("Next");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(backBtn);
        buttonPanel.add(acceptBtn);
        buttonPanel.add(rejectBtn);
        buttonPanel.add(nextBtn);
        buttonPanel.add(exitBtn);

        // ---------- BOTTOM CONTAINER (FIX) ----------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(remarksPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // ---------- LOAD DATA ----------
        try {
            records = UserDataFetcher.fetchAll(con, tableName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load records:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
            records = new ArrayList<>();
        }

        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No records found");
            return;
        }

        currentIndex = prefs.getInt("lastIndex", 0);
        if (currentIndex >= records.size()) currentIndex = 0;

        showRecord(currentIndex);

        // ---------- ACTIONS ----------
        searchBtn.addActionListener(e -> searchRecord());

        nextBtn.addActionListener(e -> {
            if (currentIndex < records.size() - 1) {
                currentIndex++;
                showRecord(currentIndex);
                saveIndex();
            } else {
                JOptionPane.showMessageDialog(this, "Last record reached");
            }
        });

        backBtn.addActionListener(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                showRecord(currentIndex);
                saveIndex();
            } else {
                JOptionPane.showMessageDialog(this, "First record");
            }
        });

        acceptBtn.addActionListener(e -> saveToCSV("ACCEPTED"));
        rejectBtn.addActionListener(e -> saveToCSV("REJECTED"));

        exitBtn.addActionListener(e -> {
            saveIndex();
            dispose();
        });

        setVisible(true);
    }

    private boolean isLongTextField(String columnName) {
    columnName = columnName.toLowerCase();
    return columnName.contains("address")
            || columnName.contains("remark")
            || columnName.contains("description");
}

    // ---------- SEARCH ----------
    private void searchRecord() {
        String mobile = searchField.getText().trim();

        if (mobile.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter mobile number");
            return;
        }

        for (int i = 0; i < records.size(); i++) {
            String val = records.get(i).get(mobileColumn);
            if (val != null && mobile.equals(val)) {
                currentIndex = i;
                showRecord(currentIndex);
                saveIndex();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Record not found");
    }

    // ---------- SHOW RECORD ----------
    private void showRecord(int index) {
        formPanel.removeAll();

        Map<String, String> row = records.get(index);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int y = 0;

        for (Map.Entry<String, String> entry : row.entrySet()) {

            gbc.gridx = 0;
            gbc.gridy = y;
            gbc.weightx = 0.3;
            formPanel.add(new JLabel(entry.getKey()), gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;

            String value = entry.getValue() == null ? "" : entry.getValue();

if (isLongTextField(entry.getKey())) {

    JTextArea area = new JTextArea(value, 3, 30);
    area.setLineWrap(true);
    area.setWrapStyleWord(true);
    area.setEditable(false);
    area.setBackground(new JTextField().getBackground());

    JScrollPane areaScroll = new JScrollPane(area);
    areaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1;
    formPanel.add(areaScroll, gbc);

} else {

    JTextField field = new JTextField(value);
    field.setEditable(false);
    field.setPreferredSize(new Dimension(300, 25));

    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weighty = 0;
    formPanel.add(field, gbc);
}

            
            y++;
        }

        remarksArea.setText("");

        formPanel.revalidate();
        formPanel.repaint();
    }

    // ---------- SAVE INDEX ----------
    private void saveIndex() {
        prefs.putInt("lastIndex", currentIndex);
    }

    // ---------- SAVE TO CSV ----------
    private void saveToCSV(String status) {
        try {
            String baseDir = System.getProperty("user.home")
                    + java.io.File.separator
                    + "UserVerification";

            java.io.File dir = new java.io.File(baseDir);
            if (!dir.exists()) dir.mkdirs();

            java.io.File file = new java.io.File(dir, "verification_log.csv");
            boolean newFile = !file.exists();

            try (java.io.FileWriter fw = new java.io.FileWriter(file, true)) {

                Map<String, String> row = records.get(currentIndex);

                if (newFile) {
                    for (String col : row.keySet()) {
                        fw.write(col + ",");
                    }
                    fw.write("Status,Remarks,DateTime\n");
                }

                for (String value : row.values()) {
                    if (value == null) value = "";
                    value = value.replace(",", " ").replace("\n", " ");
                    fw.write("\"" + value + "\",");
                }

                String remarks = remarksArea.getText()
                        .replace(",", " ")
                        .replace("\n", " ");

                String time = java.time.LocalDateTime.now().toString();
                fw.write(status + ",\"" + remarks + "\"," + time + "\n");
            }

            JOptionPane.showMessageDialog(this,
                    "Saved successfully (" + status + ")");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "CSV Save Failed:\n" + e.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}