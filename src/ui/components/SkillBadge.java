package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import ui.theme.ThemeManager;

public class SkillBadge extends JPanel {
    private final String skillName;

    public SkillBadge(String skillName) {
        this.skillName = skillName;
        setOpaque(false);
        
        JLabel lbl = new JLabel(skillName);
        lbl.setFont(ThemeManager.F_SMALL_BOLD);
        lbl.setForeground(ThemeManager.PANEL);
        lbl.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        add(lbl);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(ThemeManager.PRIMARY);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), ThemeManager.RADIUS_PANEL, ThemeManager.RADIUS_PANEL);
        g2.dispose();
        super.paintComponent(g);
    }
}
