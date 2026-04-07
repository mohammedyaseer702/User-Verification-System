package ui;

import javax.swing.SwingUtilities;

public class StartApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new DatabaseSelector();
        });
    }
}
