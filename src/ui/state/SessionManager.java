package ui.state;

public class SessionManager {
    private static SessionManager instance;
    private int currentUserId = -1;
    private Role currentRole;

    public enum Role {
        GUEST,
        PROFESIONAL,
        EMPRESA
    }

    private SessionManager() {
        this.currentRole = Role.GUEST;
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void loginProfesional(int id) {
        this.currentUserId = id;
        this.currentRole = Role.PROFESIONAL;
    }

    public void loginEmpresa(int id) {
        this.currentUserId = id;
        this.currentRole = Role.EMPRESA;
    }

    public void logout() {
        this.currentUserId = -1;
        this.currentRole = Role.GUEST;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public Role getCurrentRole() {
        return currentRole;
    }
}
