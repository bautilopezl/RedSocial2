package ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import ui.theme.ThemeManager;

public class SidebarButton extends JButton {
    private Color bg = ThemeManager.PANEL;
    private boolean active;

    public SidebarButton(String text) {
        super(text);
        setFont(ThemeManager.F_BODY_PLAIN);
        setForeground(ThemeManager.TEXT_SECONDARY);
        setBackground(bg);
        setHorizontalAlignment(SwingConstants.LEFT);
        setPreferredSize(new Dimension(200, ThemeManager.BTN_H + 5));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) {
                    bg = ThemeManager.BG_GENERAL;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!active) {
                    bg = ThemeManager.PANEL;
                    repaint();
                }
            }
        });
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            setFont(ThemeManager.F_BODY_BOLD);
            setForeground(ThemeManager.PRIMARY);
            bg = ThemeManager.SIDEBAR_ACTIVE_BG;
        } else {
            setFont(ThemeManager.F_BODY_PLAIN);
            setForeground(ThemeManager.TEXT_SECONDARY);
            bg = ThemeManager.PANEL;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
        super.paintComponent(g);
    }
}
