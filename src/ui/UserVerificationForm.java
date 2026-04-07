package ui;

import db.UserDataFetcher;
import util.ExcelWriter;
import javax.swing.*;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserVerificationForm extends JFrame {

    private Map<String, JTextField> fieldMap = new LinkedHashMap<>();

    public UserVerificationForm(
            Connection con,
            String tableName,
            String mobileColumn,
            String mobileValue) {

        setTitle("User Verification");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        try {
            Map<String, String> data =
                    UserDataFetcher.fetchByMobile(
                            con, tableName, mobileColumn, mobileValue);

            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No record found");
                dispose();
                return;
            }

            int y = 30;

            for (String key : data.keySet()) {

                JLabel lbl = new JLabel(key);
                lbl.setBounds(30, y, 200, 25);
                add(lbl);

                JTextField txt = new JTextField(data.get(key));
                txt.setBounds(240, y, 350, 25);
                add(txt);

                fieldMap.put(key, txt);
                y += 35;
            }

            // ===== Buttons =====
            JButton acceptBtn = new JButton("Accept");
            acceptBtn.setBounds(200, y + 20, 120, 30);
            add(acceptBtn);

            JButton rejectBtn = new JButton("Reject");
            rejectBtn.setBounds(340, y + 20, 120, 30);
            add(rejectBtn);

            // Actions (Excel next step)
            acceptBtn.addActionListener(e -> {
    try {
        ExcelWriter.save(fieldMap, "", "ACCEPTED");
        JOptionPane.showMessageDialog(this, "Saved to Accepted CSV");
        con.close();
        dispose();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
});

rejectBtn.addActionListener(e -> {
    try {
        ExcelWriter.save(fieldMap, "", "REJECTED");
        JOptionPane.showMessageDialog(this, "Saved to Rejected CSV");
        con.close();
        dispose();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        setVisible(true);
    }

    

    



    

}


