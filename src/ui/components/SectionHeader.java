package ui.components;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import ui.theme.ThemeManager;

public class SectionHeader extends JPanel {
    
    public SectionHeader(String titleText) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel title = new JLabel(titleText);
        title.setFont(ThemeManager.F_SUBT_BOLD);
        title.setForeground(ThemeManager.TEXT_PRIMARY);

        add(title, BorderLayout.WEST);
    }
}
