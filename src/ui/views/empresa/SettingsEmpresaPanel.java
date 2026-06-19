package ui.views.empresa;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.SectionHeader;
import ui.layout.RoleLayoutManager;
import ui.state.SessionManager;
import ui.theme.ThemeManager;

public class SettingsEmpresaPanel extends JPanel {
    private final RoleLayoutManager roleLayoutManager;

    public SettingsEmpresaPanel(RoleLayoutManager roleLayoutManager) {
        this.roleLayoutManager = roleLayoutManager;

        setBackground(ThemeManager.BG_GENERAL);
        setLayout(new BorderLayout(ThemeManager.CARD_GAP, ThemeManager.CARD_GAP));
        setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN, ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN));

        JPanel contentContainer = new JPanel(new GridBagLayout());
        contentContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);

        // Preferencias
        RoundedPanel prefPanel = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        prefPanel.setLayout(new BorderLayout(10, 10));
        prefPanel.setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W));
        prefPanel.add(new SectionHeader("Preferencias"), BorderLayout.NORTH);

        JPanel prefContent = new JPanel(new GridBagLayout());
        prefContent.setOpaque(false);
        GridBagConstraints pgc = new GridBagConstraints();
        pgc.gridx = 0; pgc.gridy = 0; pgc.anchor = GridBagConstraints.WEST;
        pgc.insets = new Insets(5, 0, 5, 0);

        JLabel lblNotif = new JLabel("Las preferencias de notificaciones estarán disponibles en una próxima actualización.");
        lblNotif.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNotif.setForeground(ThemeManager.TEXT_SECONDARY);
        prefContent.add(lblNotif, pgc);

        prefPanel.add(prefContent, BorderLayout.CENTER);
        contentContainer.add(prefPanel, gbc);

        // Cerrar Sesión
        gbc.gridy++;
        RoundedPanel logoutPanel = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        logoutPanel.setLayout(new BorderLayout(10, 10));
        logoutPanel.setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W));
        logoutPanel.add(new SectionHeader("Cerrar Sesión"), BorderLayout.NORTH);

        JLabel lblLogoutDesc = new JLabel("Finalizar tu sesión actual en la plataforma.");
        lblLogoutDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLogoutDesc.setForeground(ThemeManager.TEXT_SECONDARY);
        logoutPanel.add(lblLogoutDesc, BorderLayout.CENTER);

        RoundedButton btnLogout = new RoundedButton("Cerrar Sesión", true);
        btnLogout.setPreferredSize(new Dimension(180, 40));
        btnLogout.addActionListener(e -> cerrarSesion());
        logoutPanel.add(btnLogout, BorderLayout.SOUTH);

        contentContainer.add(logoutPanel, gbc);

        // Acerca del Sistema
        gbc.gridy++;
        RoundedPanel aboutPanel = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        aboutPanel.setLayout(new BorderLayout(10, 10));
        aboutPanel.setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W));
        aboutPanel.add(new SectionHeader("Acerca del Sistema"), BorderLayout.NORTH);

        JPanel aboutContent = new JPanel(new GridBagLayout());
        aboutContent.setOpaque(false);
        GridBagConstraints agc = new GridBagConstraints();
        agc.gridx = 0; agc.gridy = 0; agc.anchor = GridBagConstraints.WEST;
        agc.insets = new Insets(3, 0, 3, 0);

        aboutContent.add(createAboutRow("Red Social Profesional", true), agc);
        agc.gridy++;
        aboutContent.add(createAboutRow("Versión 1.0.0", false), agc);
        agc.gridy++;
        aboutContent.add(createAboutRow("Desarrollado en Java Swing con arquitectura MVC", false), agc);
        agc.gridy++;
        aboutContent.add(createAboutRow("Estructuras de datos: ABB, Cola, Grafo, Pila, Árbol n-ario", false), agc);

        aboutPanel.add(aboutContent, BorderLayout.CENTER);
        contentContainer.add(aboutPanel, gbc);

        // Espaciador
        gbc.gridy++; gbc.weighty = 1.0;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        contentContainer.add(filler, gbc);

        add(contentContainer, BorderLayout.CENTER);
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de cerrar sesión?",
            "Cerrar Sesión", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.getInstance().logout();
            roleLayoutManager.setRoleGuest();
        }
    }

    private JLabel createAboutRow(String text, boolean bold) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, 13));
        lbl.setForeground(bold ? ThemeManager.TEXT_PRIMARY : ThemeManager.TEXT_SECONDARY);
        return lbl;
    }
}
