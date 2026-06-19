package ui.components;
 
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import ui.theme.ThemeManager;

public class RoundedButton extends JButton {
    private boolean isPrimary;
    private int radius;
    
    private final Color primaryBg = ThemeManager.PRIMARY;
    private final Color primaryHover = ThemeManager.PRIMARY_HOVER;
    private final Color secondaryBg = ThemeManager.PANEL;
    private final Color secondaryHover = ThemeManager.BG_GENERAL;
    private final Color secondaryBorder = ThemeManager.PRIMARY;
    
    private Color currentBg;

    public RoundedButton(String text, boolean isPrimary) {
        super(text);
        this.isPrimary = isPrimary;
        this.radius = ThemeManager.RADIUS_BTN;
        
        setFont(ThemeManager.F_BODY_BOLD);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);

        if (isPrimary) {
            setForeground(ThemeManager.PANEL);
            currentBg = primaryBg;
        } else {
            setForeground(ThemeManager.PRIMARY);
            currentBg = secondaryBg;
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                currentBg = isPrimary ? primaryHover : secondaryHover;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                currentBg = isPrimary ? primaryBg : secondaryBg;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(currentBg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        if (!isPrimary) {
            g2.setColor(secondaryBorder);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
