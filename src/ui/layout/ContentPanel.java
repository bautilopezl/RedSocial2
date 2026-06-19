package ui.layout;

import java.awt.Component;
import javax.swing.JPanel;
import ui.theme.ThemeManager;

public class ContentPanel extends JPanel {
    public ContentPanel() {
        setBackground(ThemeManager.BG_GENERAL);
    }

    public void addPanel(Component panel, String name) {
        add(panel, name);
    }
}
