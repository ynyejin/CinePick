package cinepick.gui;

import javax.swing.SwingUtilities;

public class GuiMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppContext context = new AppContext();
            LoginFrame loginFrame = new LoginFrame(context);
            loginFrame.setVisible(true);
        });
    }
}