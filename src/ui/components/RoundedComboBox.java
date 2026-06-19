package ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import ui.theme.ThemeManager;

public class RoundedComboBox<E> extends JComboBox<E> {
    private int radius = ThemeManager.RADIUS_SMALL;

    public RoundedComboBox(E[] items) {
        super(items);
        setOpaque(false);
        setFont(ThemeManager.F_BODY_PLAIN);
        setRenderer(new Renderer<>());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(ThemeManager.PANEL);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(ThemeManager.BORDER);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }

    private static class Renderer<E> extends JLabel implements ListCellRenderer<E> {
        Renderer() {
            setOpaque(true);
            setFont(ThemeManager.F_BODY_PLAIN);
            setBorder(new EmptyBorder(4, 8, 4, 8));
        }

        @Override
        public Component getListCellRendererComponent(javax.swing.JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value == null ? "" : value.toString());
            if (isSelected) {
                setBackground(ThemeManager.BG_GENERAL);
                setForeground(ThemeManager.TEXT_PRIMARY);
            } else {
                setBackground(ThemeManager.PANEL);
                setForeground(ThemeManager.TEXT_PRIMARY);
            }
            return this;
        }
    }
}
