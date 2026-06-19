package ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class RoundedPanel extends JPanel {
    private int cornerRadius = 15;
    private boolean shadowEnabled = false;

    public RoundedPanel(int radius, Color bgColor) {
        super();
        this.cornerRadius = radius;
        setOpaque(false);
        setBackground(bgColor);
    }

    public RoundedPanel() {
        this(15, Color.WHITE);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setShadowEnabled(boolean enabled) {
        this.shadowEnabled = enabled;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (shadowEnabled) {
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(2, 3, getWidth() - 2, getHeight() - 1, cornerRadius, cornerRadius);
        }

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
        super.paintComponent(g);
    }
}
