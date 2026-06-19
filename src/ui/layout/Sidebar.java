package ui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import ui.components.RoundedButton;
import ui.components.SidebarButton;
import ui.state.SessionManager;
import ui.theme.ThemeManager;

public class Sidebar extends JPanel {
    private static final int SIDEBAR_W = 220;

    private final JPanel navSection;
    private final JPanel bottomSection;
    private SidebarButton activeButton;
    private JLabel userNameLabel;

    public Sidebar() {
        setLayout(new BorderLayout());
        setBackground(ThemeManager.PANEL);
        setPreferredSize(new Dimension(SIDEBAR_W, 0));

        // ── Top: logo + system name ──
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        topSection.setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.PADDING, ThemeManager.BORDER_W, ThemeManager.PADDING));

        JLabel logoLabel = new JLabel("RP");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logoLabel.setForeground(ThemeManager.PRIMARY);
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(logoLabel);

        topSection.add(Box.createRigidArea(new Dimension(0, 2)));

        JLabel systemLabel = new JLabel("Red Social");
        systemLabel.setFont(ThemeManager.F_SMALL_PLAIN);
        systemLabel.setForeground(ThemeManager.TEXT_SECONDARY);
        systemLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.add(systemLabel);

        add(topSection, BorderLayout.NORTH);

        // ── Center: navigation buttons ──
        navSection = new JPanel();
        navSection.setLayout(new BoxLayout(navSection, BoxLayout.Y_AXIS));
        navSection.setOpaque(false);
        navSection.setBorder(new EmptyBorder(0, 8, 0, 8));
        add(navSection, BorderLayout.CENTER);

        // ── Bottom: user info + logout ──
        bottomSection = new JPanel();
        bottomSection.setLayout(new BoxLayout(bottomSection, BoxLayout.Y_AXIS));
        bottomSection.setOpaque(false);
        bottomSection.setBorder(new EmptyBorder(0, ThemeManager.PADDING, ThemeManager.PADDING, ThemeManager.PADDING));

        JSeparator sep = new JSeparator();
        sep.setForeground(ThemeManager.BORDER);
        sep.setMaximumSize(new Dimension(SIDEBAR_W - ThemeManager.PADDING * 2, 1));
        bottomSection.add(sep);
        bottomSection.add(Box.createRigidArea(new Dimension(0, 12)));

        userNameLabel = new JLabel("");
        userNameLabel.setFont(ThemeManager.F_BODY_BOLD);
        userNameLabel.setForeground(ThemeManager.TEXT_PRIMARY);
        userNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomSection.add(userNameLabel);

        bottomSection.add(Box.createRigidArea(new Dimension(0, 8)));

        RoundedButton btnLogout = new RoundedButton("Cerrar Sesion", false);
        btnLogout.setPreferredSize(new Dimension(SIDEBAR_W - ThemeManager.PADDING * 2, ThemeManager.BTN_H));
        btnLogout.setMaximumSize(new Dimension(SIDEBAR_W - ThemeManager.PADDING * 2, ThemeManager.BTN_H));
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogout.addActionListener(e -> logout());
        bottomSection.add(btnLogout);

        add(bottomSection, BorderLayout.SOUTH);
    }

    public void setMenuProfesional(RoleLayoutManager roleLayoutManager, String userName) {
        bottomSection.setVisible(true);
        navSection.removeAll();
        userNameLabel.setText(userName);

        SidebarButton btnInicio = new SidebarButton("Inicio");
        btnInicio.addActionListener(e -> {
            setActive(btnInicio);
            roleLayoutManager.actualizarHome();
            NavigationManager.getInstance().navigateTo("HOME_PROFESIONAL");
        });
        navSection.add(btnInicio);
        setActive(btnInicio);

        SidebarButton btnPerfil = new SidebarButton("Mi Perfil");
        btnPerfil.addActionListener(e -> {
            setActive(btnPerfil);
            roleLayoutManager.actualizarPerfilProfesional();
            NavigationManager.getInstance().navigateTo("PERFIL_PROFESIONAL");
        });
        navSection.add(btnPerfil);

        SidebarButton btnRed = new SidebarButton("Mi Red");
        btnRed.addActionListener(e -> {
            setActive(btnRed);
            roleLayoutManager.actualizarMiRed();
            NavigationManager.getInstance().navigateTo("RED_PROFESIONAL");
        });
        navSection.add(btnRed);

        SidebarButton btnEmpleos = new SidebarButton("Buscar Empleos");
        btnEmpleos.addActionListener(e -> {
            setActive(btnEmpleos);
            roleLayoutManager.actualizarEmpleos();
            NavigationManager.getInstance().navigateTo("EMPLEOS_PROFESIONAL");
        });
        navSection.add(btnEmpleos);

        SidebarButton btnPostulaciones = new SidebarButton("Mis Postulaciones");
        btnPostulaciones.addActionListener(e -> {
            setActive(btnPostulaciones);
            roleLayoutManager.actualizarPostulaciones();
            NavigationManager.getInstance().navigateTo("POSTULACIONES_PROFESIONAL");
        });
        navSection.add(btnPostulaciones);

        SidebarButton btnConfig = new SidebarButton("Configuracion");
        btnConfig.addActionListener(e -> {
            setActive(btnConfig);
            NavigationManager.getInstance().navigateTo("CONFIG_PROFESIONAL");
        });
        navSection.add(btnConfig);

        revalidate();
        repaint();
    }

    public void setMenuEmpresa(RoleLayoutManager roleLayoutManager, String companyName) {
        bottomSection.setVisible(true);
        navSection.removeAll();
        userNameLabel.setText(companyName);

        SidebarButton btnDashboard = new SidebarButton("Dashboard");
        btnDashboard.addActionListener(e -> {
            setActive(btnDashboard);
            roleLayoutManager.actualizarCompanyDashboard();
            NavigationManager.getInstance().navigateTo("DASHBOARD_EMPRESA");
        });
        navSection.add(btnDashboard);

        SidebarButton btnOfertas = new SidebarButton("Mis Ofertas");
        btnOfertas.addActionListener(e -> {
            setActive(btnOfertas);
            roleLayoutManager.actualizarCompanyJobs();
            NavigationManager.getInstance().navigateTo("OFERTAS_EMPRESA");
        });
        navSection.add(btnOfertas);

        SidebarButton btnPostulaciones = new SidebarButton("Postulaciones");
        btnPostulaciones.addActionListener(e -> {
            setActive(btnPostulaciones);
            roleLayoutManager.actualizarCandidates();
            NavigationManager.getInstance().navigateTo("CANDIDATOS_EMPRESA");
        });
        navSection.add(btnPostulaciones);

        SidebarButton btnConfig = new SidebarButton("Configuracion");
        btnConfig.addActionListener(e -> {
            setActive(btnConfig);
            NavigationManager.getInstance().navigateTo("CONFIG_EMPRESA");
        });
        navSection.add(btnConfig);

        revalidate();
        repaint();
    }

    public void setMenuBasico() {
        bottomSection.setVisible(false);
        navSection.removeAll();
        userNameLabel.setText("");
        revalidate();
        repaint();
    }

    private void setActive(SidebarButton btn) {
        if (activeButton != null && activeButton != btn) {
            activeButton.setActive(false);
        }
        activeButton = btn;
        activeButton.setActive(true);
    }

    private void logout() {
        SessionManager.getInstance().logout();
        NavigationManager.getInstance().navigateTo("LOGIN");
        setMenuBasico();
    }
}
