package ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import ui.theme.ThemeManager;

public class JobCard extends RoundedPanel {
    
    // Asumimos que los datos los pasamos como String para simplificar, 
    // en la vida real se podria pasar un objeto Oferta o similar.
    public JobCard(String titulo, String descripcion, String empleador, String botonTexto, Runnable accionBtn) {
        super(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        setShadowEnabled(true);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setOpaque(false);
        
        JLabel tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(ThemeManager.F_HEAD_BOLD);
        tituloLabel.setForeground(ThemeManager.PRIMARY);
        
        JLabel empresaLabel = new JLabel("Empresa: " + empleador);
        empresaLabel.setFont(ThemeManager.F_SMALL_BOLD);
        empresaLabel.setForeground(ThemeManager.TEXT_SECONDARY);

        JLabel descLabel = new JLabel("<html><p style='width:300px'>" + descripcion + "</p></html>");
        descLabel.setFont(ThemeManager.F_SMALL_PLAIN);
        descLabel.setForeground(ThemeManager.TEXT_DESCRIPTION);

        infoPanel.add(tituloLabel);
        infoPanel.add(empresaLabel);
        infoPanel.add(descLabel);

        add(infoPanel, BorderLayout.CENTER);

        if (botonTexto != null && accionBtn != null) {
            JPanel actionPanel = new JPanel(new BorderLayout());
            actionPanel.setOpaque(false);
            RoundedButton btn = new RoundedButton(botonTexto, true);
            btn.setPreferredSize(new Dimension(ThemeManager.BTN_W, ThemeManager.BTN_H));
            btn.addActionListener(e -> accionBtn.run());
            actionPanel.add(btn, BorderLayout.EAST);
            add(actionPanel, BorderLayout.SOUTH);
        }
    }
}
