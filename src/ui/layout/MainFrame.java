package ui.layout;

import controladores.*;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ui.theme.ThemeManager;
import ui.views.auth.LoginPanel;
import ui.state.SessionManager;
import ui.views.profesional.HomeProfesionalPanel;
import ui.views.profesional.PerfilProfesionalPanel;
import ui.views.profesional.MiRedPanel;
import ui.views.profesional.EmpleosPanel;
import ui.views.profesional.SettingsProfesionalPanel;
import ui.views.profesional.PostulacionesProfesionalPanel;
import ui.views.empresa.CompanyDashboardPanel;
import ui.views.empresa.CompanyJobsPanel;
import ui.views.empresa.CandidatesPanel;
import ui.views.empresa.SettingsEmpresaPanel;

public class MainFrame extends JFrame {
    private final TopBar topBar;
    private final Sidebar sidebar;
    private final ContentPanel contentPanel;
    private final CardNavigator cardNavigator;
    private final RoleLayoutManager roleLayoutManager;

    private final UsuarioController usuarioController;
    private final EmpleadorController empleadorController;
    private final PostulacionController postulacionController;
    private final ContactoController contactoController;
    private final HabilidadController habilidadController;

    public MainFrame(UsuarioController usuarioController,
                     EmpleadorController empleadorController,
                     PostulacionController postulacionController,
                     ContactoController contactoController,
                     HabilidadController habilidadController) {
        super("Red Social Profesional");
        
        this.usuarioController = usuarioController;
        this.empleadorController = empleadorController;
        this.postulacionController = postulacionController;
        this.contactoController = contactoController;
        this.habilidadController = habilidadController;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        contentPanel = new ContentPanel();
        cardNavigator = new CardNavigator(contentPanel);
        
        NavigationManager.getInstance().setCardNavigator(cardNavigator);
        
        topBar = new TopBar();
        sidebar = new Sidebar();
        roleLayoutManager = new RoleLayoutManager(sidebar, topBar);

        add(topBar, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        initBasicPanels();
        
        // Empezar en modo login / seleccion de rol básico
        roleLayoutManager.setRoleGuest();
    }

    private void initBasicPanels() {
        LoginPanel loginPanel = new LoginPanel(usuarioController, empleadorController, roleLayoutManager);
        contentPanel.addPanel(loginPanel, "LOGIN");
        
        // Profesional Home
        HomeProfesionalPanel homeProfesional = new HomeProfesionalPanel(usuarioController, postulacionController, contactoController);
        roleLayoutManager.setHomeProfesionalPanel(homeProfesional);
        contentPanel.addPanel(homeProfesional, "HOME_PROFESIONAL");
        
        // Profesional Perfil
        PerfilProfesionalPanel perfilProfesional = new PerfilProfesionalPanel(usuarioController, habilidadController, contactoController);
        roleLayoutManager.setPerfilProfesionalPanel(perfilProfesional);
        contentPanel.addPanel(perfilProfesional, "PERFIL_PROFESIONAL");
        
        // Profesional Mi Red
        MiRedPanel miRedPanel = new MiRedPanel(usuarioController, contactoController, habilidadController);
        roleLayoutManager.setMiRedPanel(miRedPanel);
        contentPanel.addPanel(miRedPanel, "RED_PROFESIONAL");
        
        // Profesional Empleos
        EmpleosPanel empleosPanel = new EmpleosPanel(postulacionController);
        roleLayoutManager.setEmpleosPanel(empleosPanel);
        contentPanel.addPanel(empleosPanel, "EMPLEOS_PROFESIONAL");
        
        // Profesional Configuración
        SettingsProfesionalPanel settingsProfesional = new SettingsProfesionalPanel(roleLayoutManager);
        contentPanel.addPanel(settingsProfesional, "CONFIG_PROFESIONAL");

        // Profesional Postulaciones
        PostulacionesProfesionalPanel postulacionesPanel = new PostulacionesProfesionalPanel(postulacionController);
        roleLayoutManager.setPostulacionesPanel(postulacionesPanel);
        contentPanel.addPanel(postulacionesPanel, "POSTULACIONES_PROFESIONAL");

        // Empresa Dashboard
        CompanyDashboardPanel companyDashboardPanel = new CompanyDashboardPanel(empleadorController, postulacionController);
        roleLayoutManager.setCompanyDashboardPanel(companyDashboardPanel);
        contentPanel.addPanel(companyDashboardPanel, "DASHBOARD_EMPRESA");

        // Empresa Ofertas
        CompanyJobsPanel companyJobsPanel = new CompanyJobsPanel(postulacionController);
        roleLayoutManager.setCompanyJobsPanel(companyJobsPanel);
        contentPanel.addPanel(companyJobsPanel, "OFERTAS_EMPRESA");

        // Empresa Candidatos
        CandidatesPanel candidatesPanel = new CandidatesPanel(usuarioController, postulacionController, habilidadController);
        roleLayoutManager.setCandidatesPanel(candidatesPanel);
        contentPanel.addPanel(candidatesPanel, "CANDIDATOS_EMPRESA");

        // Empresa Configuración
        SettingsEmpresaPanel settingsEmpresa = new SettingsEmpresaPanel(roleLayoutManager);
        contentPanel.addPanel(settingsEmpresa, "CONFIG_EMPRESA");

    }

    private JPanel createMockPanel(String title, Color bg) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bg);
        JLabel lbl = new JLabel(title, JLabel.CENTER);
        lbl.setFont(ThemeManager.F_TITLE_BOLD);
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }
}
