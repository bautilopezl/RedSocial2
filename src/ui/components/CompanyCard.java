package ui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import modelo.Empleador;
import ui.theme.ThemeManager;

public class CompanyCard extends RoundedPanel {

    public CompanyCard(Empleador empleador) {
        super(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        setShadowEnabled(true);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 6, 0);

        JLabel lblNombre = new JLabel(empleador.getNombreEmpresa());
        lblNombre.setFont(ThemeManager.F_HEAD_BOLD);
        lblNombre.setForeground(ThemeManager.PRIMARY);
        add(lblNombre, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 4, 0);
        JLabel lblRubro = new JLabel(empleador.getRubro());
        lblRubro.setFont(ThemeManager.F_BODY_BOLD);
        lblRubro.setForeground(ThemeManager.TEXT_SECONDARY);
        add(lblRubro, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel lblDescripcion = new JLabel("<html><p style='width:500px'>" + empleador.getDescripcion() + "</p></html>");
        lblDescripcion.setFont(ThemeManager.F_SMALL_PLAIN);
        lblDescripcion.setForeground(ThemeManager.TEXT_DESCRIPTION);
        add(lblDescripcion, gbc);
    }
}
