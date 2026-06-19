package ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import modelo.Usuario;
import ui.theme.ThemeManager;

public class UserCard extends RoundedPanel {

    public UserCard(Usuario usuario, String botonTexto, Runnable accionBtn) {
        super(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        setShadowEnabled(true);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 2, 0);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nombreLabel = new JLabel(usuario.getNombre());
        nombreLabel.setFont(ThemeManager.F_BODY_BOLD);
        infoPanel.add(nombreLabel, gbc);

        gbc.gridy++;
        JLabel emailLabel = new JLabel(usuario.getEmail());
        emailLabel.setFont(ThemeManager.F_XS_PLAIN);
        emailLabel.setForeground(ThemeManager.TEXT_SECONDARY);
        infoPanel.add(emailLabel, gbc);

        gbc.gridy++;
        JLabel profesionLabel = new JLabel(usuario.getProfesion());
        profesionLabel.setFont(ThemeManager.F_SMALL_PLAIN);
        profesionLabel.setForeground(ThemeManager.PRIMARY);
        infoPanel.add(profesionLabel, gbc);

        if (usuario.cantidadHabilidades() > 0) {
            gbc.gridy++;
            gbc.insets = new Insets(4, 0, 0, 0);
            JPanel skillsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
            skillsPanel.setOpaque(false);
            String[] habilidades = usuario.obtenerHabilidades();
            int maxSkills = Math.min(habilidades.length, 3);
            for (int i = 0; i < maxSkills; i++) {
                skillsPanel.add(new SkillBadge(habilidades[i]));
            }
            if (habilidades.length > 3) {
                JLabel masLabel = new JLabel("+" + (habilidades.length - 3) + " más");
                masLabel.setFont(ThemeManager.F_XS_PLAIN);
                masLabel.setForeground(Color.GRAY);
                skillsPanel.add(masLabel);
            }
            infoPanel.add(skillsPanel, gbc);
        }

        add(infoPanel, BorderLayout.CENTER);

        if (botonTexto != null && accionBtn != null) {
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            btnPanel.setOpaque(false);
            RoundedButton btn = new RoundedButton(botonTexto, false);
            btn.setPreferredSize(new Dimension(ThemeManager.BTN_W_SMALL, 30));
            btn.addActionListener(e -> accionBtn.run());
            btnPanel.add(btn);
            add(btnPanel, BorderLayout.SOUTH);
        }
    }
}
