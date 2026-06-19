package ui.views.empresa;

import controladores.EmpleadorController;
import controladores.PostulacionController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import modelo.Empleador;
import ui.components.CompanyCard;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.SectionHeader;
import ui.layout.NavigationManager;
import ui.state.SessionManager;
import ui.theme.ThemeManager;

public class CompanyDashboardPanel extends JPanel {
    private final EmpleadorController empleadorController;
    private final PostulacionController postulacionController;

    public CompanyDashboardPanel(EmpleadorController empleadorController, PostulacionController postulacionController) {
        this.empleadorController = empleadorController;
        this.postulacionController = postulacionController;

        setBackground(ThemeManager.BG_GENERAL);
        setLayout(new BorderLayout(ThemeManager.CARD_GAP, ThemeManager.CARD_GAP));
        setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN, ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN));
    }

    public void cargarDatos() {
        removeAll();

        int idEmpresa = SessionManager.getInstance().getCurrentUserId();
        if (idEmpresa < 0) return;

        Empleador empleador = empleadorController.buscarEmpleador(idEmpresa);
        if (empleador == null) return;

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblBienvenida = new JLabel("Dashboard de " + empleador.getNombreEmpresa());
        lblBienvenida.setFont(ThemeManager.F_XXL_BOLD);
        lblBienvenida.setForeground(ThemeManager.TEXT_PRIMARY);
        headerPanel.add(lblBienvenida, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentGrid = new JPanel(new GridBagLayout());
        contentGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 0.5;
        gbc.insets = new Insets(0, 0, 20, 0);

        gbc.gridx = 0; gbc.gridy = 0;
        RoundedPanel resumenPanel = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        resumenPanel.setLayout(new BorderLayout(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));
        resumenPanel.setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W));
        
        resumenPanel.add(new SectionHeader("Resumen Corporativo"), BorderLayout.NORTH);

        resumenPanel.add(new CompanyCard(empleador), BorderLayout.CENTER);
        contentGrid.add(resumenPanel, gbc);

        gbc.gridy = 1; gbc.weighty = 0.5;
        RoundedPanel accionesPanel = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        accionesPanel.setLayout(new BorderLayout());
        accionesPanel.setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W));
        
        accionesPanel.add(new SectionHeader("Acciones Rápidas"), BorderLayout.NORTH);
        
        JPanel botonesGrid = new JPanel(new GridBagLayout());
        botonesGrid.setOpaque(false);
        GridBagConstraints gBtn = new GridBagConstraints();
        gBtn.gridx = 0; gBtn.gridy = 0; gBtn.insets = new Insets(0, 0, 0, 15);
        
        RoundedButton btnPublicar = new RoundedButton("Publicar nueva oferta", true);
        btnPublicar.setPreferredSize(new Dimension(200, ThemeManager.BTN_H + 5));
        btnPublicar.addActionListener(e -> NavigationManager.getInstance().navigateTo("OFERTAS_EMPRESA"));
        botonesGrid.add(btnPublicar, gBtn);
        
        gBtn.gridx = 1;
        RoundedButton btnGestionar = new RoundedButton("Gestionar Postulaciones", false);
        btnGestionar.setPreferredSize(new Dimension(200, ThemeManager.BTN_H + 5));
        btnGestionar.addActionListener(e -> NavigationManager.getInstance().navigateTo("CANDIDATOS_EMPRESA"));
        botonesGrid.add(btnGestionar, gBtn);

        accionesPanel.add(botonesGrid, BorderLayout.CENTER);
        contentGrid.add(accionesPanel, gbc);

        add(contentGrid, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

}
