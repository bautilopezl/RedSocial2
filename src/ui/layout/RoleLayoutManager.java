package ui.layout;

import ui.views.profesional.HomeProfesionalPanel;
import ui.views.profesional.PerfilProfesionalPanel;
import ui.views.profesional.MiRedPanel;
import ui.views.profesional.EmpleosPanel;
import ui.views.profesional.PostulacionesProfesionalPanel;
import ui.views.empresa.CompanyDashboardPanel;
import ui.views.empresa.CompanyJobsPanel;
import ui.views.empresa.CandidatesPanel;

public class RoleLayoutManager {
    private final Sidebar sidebar;
    private final TopBar topBar;
    private final NavigationManager navManager;
    
    // Profesional
    private HomeProfesionalPanel homeProfesionalPanel;
    private PerfilProfesionalPanel perfilProfesionalPanel;
    private MiRedPanel miRedPanel;
    private EmpleosPanel empleosPanel;
    private PostulacionesProfesionalPanel postulacionesPanel;
    
    // Empresa
    private CompanyDashboardPanel companyDashboardPanel;
    private CompanyJobsPanel companyJobsPanel;
    private CandidatesPanel candidatesPanel;

    public RoleLayoutManager(Sidebar sidebar, TopBar topBar) {
        this.sidebar = sidebar;
        this.topBar = topBar;
        this.navManager = NavigationManager.getInstance();
    }

    public void setHomeProfesionalPanel(HomeProfesionalPanel homeProfesionalPanel) {
        this.homeProfesionalPanel = homeProfesionalPanel;
    }
    
    public void setPerfilProfesionalPanel(PerfilProfesionalPanel perfilProfesionalPanel) {
        this.perfilProfesionalPanel = perfilProfesionalPanel;
    }
    
    public void setMiRedPanel(MiRedPanel miRedPanel) {
        this.miRedPanel = miRedPanel;
    }

    public void setEmpleosPanel(EmpleosPanel empleosPanel) {
        this.empleosPanel = empleosPanel;
    }

    public void setPostulacionesPanel(PostulacionesProfesionalPanel postulacionesPanel) {
        this.postulacionesPanel = postulacionesPanel;
    }
    
    public void setCompanyDashboardPanel(CompanyDashboardPanel companyDashboardPanel) {
        this.companyDashboardPanel = companyDashboardPanel;
    }

    public void setCompanyJobsPanel(CompanyJobsPanel companyJobsPanel) {
        this.companyJobsPanel = companyJobsPanel;
    }

    public void setCandidatesPanel(CandidatesPanel candidatesPanel) {
        this.candidatesPanel = candidatesPanel;
    }
    
    public void actualizarHome() {
        if (homeProfesionalPanel != null) {
            homeProfesionalPanel.cargarDatos();
        }
    }

    public void actualizarCompanyDashboard() {
        if (companyDashboardPanel != null) {
            companyDashboardPanel.cargarDatos();
        }
    }

    public void actualizarCompanyJobs() {
        if (companyJobsPanel != null) {
            companyJobsPanel.cargarDatos();
        }
    }
    
    public void actualizarPerfilProfesional() {
        if (perfilProfesionalPanel != null) {
            perfilProfesionalPanel.cargarDatos();
        }
    }
    
    public void actualizarMiRed() {
        if (miRedPanel != null) {
            miRedPanel.cargarDatos();
        }
    }
    
    public void actualizarEmpleos() {
        if (empleosPanel != null) {
            empleosPanel.cargarDatos();
        }
    }
    
    public void actualizarPostulaciones() {
        if (postulacionesPanel != null) {
            postulacionesPanel.cargarDatos();
        }
    }

    public void actualizarCandidates() {
        if (candidatesPanel != null) {
            candidatesPanel.cargarDatos();
        }
    }

    public void setRoleProfesional(String userName) {
        topBar.actualizarUsuario(userName);
        sidebar.setMenuProfesional(this, userName);
        if (homeProfesionalPanel != null) {
            homeProfesionalPanel.cargarDatos();
        }
        navManager.navigateTo("HOME_PROFESIONAL");
    }

    public void setRoleEmpresa(String companyName) {
        topBar.actualizarUsuario(companyName);
        sidebar.setMenuEmpresa(this, companyName);
        if (companyDashboardPanel != null) {
            companyDashboardPanel.cargarDatos();
        }
        navManager.navigateTo("DASHBOARD_EMPRESA");
    }

    public void setRoleGuest() {
        topBar.actualizarUsuario("");
        sidebar.setMenuBasico();
        navManager.navigateTo("LOGIN");
    }
}
