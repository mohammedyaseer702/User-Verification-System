package util;

import javax.swing.*;
import java.io.*;
import java.util.Map;

public class ExcelWriter {

    private static final String BASE_DIR = "output";

    public static void save(
            Map<String, JTextField> fieldMap,
            String remarks,
            String status) throws Exception {

        File dir = new File(BASE_DIR + File.separator + status);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, status.toLowerCase() + ".csv");
        boolean writeHeader = !file.exists();

        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            if (writeHeader) {
                for (String key : fieldMap.keySet()) {
                    bw.write(key + ",");
                }
                bw.write("Remarks,Status\n");
            }

            for (JTextField field : fieldMap.values()) {
                bw.write(field.getText().replace(",", " ") + ",");
            }

            bw.write(remarks.replace(",", " ") + "," + status + "\n");
        }
    }
}