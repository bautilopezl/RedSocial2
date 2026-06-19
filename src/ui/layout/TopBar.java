package ui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import ui.theme.ThemeManager;

public class TopBar extends JPanel {
    private AvatarLabel avatar;

    public TopBar() {
        setLayout(new BorderLayout());
        setBackground(ThemeManager.PANEL);
        setPreferredSize(new Dimension(0, 56));
        setBorder(new EmptyBorder(8, ThemeManager.PADDING, 8, ThemeManager.PADDING));

        avatar = new AvatarLabel("?");
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(avatar, BorderLayout.EAST);
        add(rightPanel, BorderLayout.EAST);
    }

    public void actualizarUsuario(String userName) {
        if (userName != null && !userName.isEmpty()) {
            avatar.setInicial(String.valueOf(userName.charAt(0)).toUpperCase());
        } else {
            avatar.setInicial("?");
        }
    }

    private static class AvatarLabel extends JLabel {
        private String inicial;

        AvatarLabel(String inicial) {
            this.inicial = inicial;
            setPreferredSize(new Dimension(36, 36));
            setMinimumSize(new Dimension(36, 36));
            setMaximumSize(new Dimension(36, 36));
            setOpaque(false);
        }

        void setInicial(String inicial) {
            this.inicial = inicial;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(ThemeManager.PRIMARY);
            g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            int x = (getWidth() - g2.getFontMetrics().stringWidth(inicial)) / 2;
            int y = (getHeight() + g2.getFontMetrics().getAscent() / 2) / 2;
            g2.drawString(inicial, x, y);
            g2.dispose();
        }
    }
}
