package ui.views.auth;

import controladores.EmpleadorController;
import controladores.UsuarioController;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import modelo.Empleador;
import modelo.Usuario;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.RoundedPasswordField;
import ui.components.RoundedTextField;
import ui.layout.RoleLayoutManager;
import ui.state.SessionManager;
import ui.theme.ThemeManager;

public class LoginPanel extends JPanel {
    private final UsuarioController usuarioController;
    private final EmpleadorController empleadorController;
    private final RoleLayoutManager roleLayoutManager;

    public LoginPanel(UsuarioController usuarioController, EmpleadorController empleadorController, RoleLayoutManager roleLayoutManager) {
        this.usuarioController = usuarioController;
        this.empleadorController = empleadorController;
        this.roleLayoutManager = roleLayoutManager;

        setBackground(ThemeManager.BG_GENERAL);
        setLayout(new GridBagLayout());

        RoundedPanel card = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);

        JLabel title = new JLabel("Iniciar Sesión");
        title.setFont(ThemeManager.F_TITLE_BOLD);
        card.add(title, gbc);

        gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        card.add(new JLabel("Email:"), gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.gridwidth = 2;
        RoundedTextField txtEmail = new RoundedTextField();
        txtEmail.setPreferredSize(new Dimension(ThemeManager.SEARCH_W, ThemeManager.INPUT_H));
        card.add(txtEmail, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(new JLabel("Contrase\u00f1a:"), gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.gridwidth = 2;
        RoundedPasswordField txtPassword = new RoundedPasswordField();
        txtPassword.setPreferredSize(new Dimension(ThemeManager.SEARCH_W, ThemeManager.INPUT_H));
        card.add(txtPassword, gbc);

        gbc.gridy = 5; gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 10, 10);
        RoundedButton btnLoginProf = new RoundedButton("Login Profesional", true);
        btnLoginProf.setPreferredSize(new Dimension(170, ThemeManager.BTN_H));
        btnLoginProf.addActionListener(e -> loginProfesional(txtEmail.getText(), new String(txtPassword.getPassword())));
        card.add(btnLoginProf, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        RoundedButton btnLoginEmp = new RoundedButton("Login Empresa", true);
        btnLoginEmp.setPreferredSize(new Dimension(170, ThemeManager.BTN_H));
        btnLoginEmp.addActionListener(e -> loginEmpresa(txtEmail.getText(), new String(txtPassword.getPassword())));
        card.add(btnLoginEmp, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        gbc.insets = new Insets(10, 0, 0, 10);
        RoundedButton btnRegProf = new RoundedButton("Register Profesional", false);
        btnRegProf.setPreferredSize(new Dimension(170, ThemeManager.BTN_H));
        btnRegProf.addActionListener(e -> registrarProfesional());
        card.add(btnRegProf, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        RoundedButton btnRegEmp = new RoundedButton("Register Empresa", false);
        btnRegEmp.setPreferredSize(new Dimension(170, ThemeManager.BTN_H));
        btnRegEmp.addActionListener(e -> registrarEmpresa());
        card.add(btnRegEmp, gbc);

        add(card);
    }

    private void loginProfesional(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar email y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Usuario user = usuarioController.buscarPorEmail(email);
        if (user != null && password.equals(user.getPassword())) {
            SessionManager.getInstance().loginProfesional(user.getId());
            roleLayoutManager.setRoleProfesional(user.getNombre());
        } else {
            JOptionPane.showMessageDialog(this, "Email o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loginEmpresa(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar email y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Empleador emp = empleadorController.buscarPorEmail(email);
        if (emp != null && password.equals(emp.getPassword())) {
            SessionManager.getInstance().loginEmpresa(emp.getId());
            roleLayoutManager.setRoleEmpresa(emp.getNombreEmpresa());
        } else {
            JOptionPane.showMessageDialog(this, "Email o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loginProfesionalPorId(int id) {
        Usuario user = usuarioController.buscarUsuario(id);
        if (user != null) {
            SessionManager.getInstance().loginProfesional(user.getId());
            roleLayoutManager.setRoleProfesional(user.getNombre());
        }
    }

    private void loginEmpresaPorId(int id) {
        Empleador emp = empleadorController.buscarEmpleador(id);
        if (emp != null) {
            SessionManager.getInstance().loginEmpresa(emp.getId());
            roleLayoutManager.setRoleEmpresa(emp.getNombreEmpresa());
        }
    }

    private void registrarProfesional() {
        JPanel p = new JPanel(new GridLayout(6, 2, 4, 4));
        JTextField nombre = new JTextField();
        JTextField email = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField profesion = new JTextField();
        JTextField descripcion = new JTextField();
        p.add(new JLabel("Nombre:")); p.add(nombre);
        p.add(new JLabel("Email:")); p.add(email);
        p.add(new JLabel("Contraseña:")); p.add(password);
        p.add(new JLabel("Profesión:")); p.add(profesion);
        p.add(new JLabel("Descripción (opcional):")); p.add(descripcion);
        int res = JOptionPane.showConfirmDialog(this, p, "Registrar Profesional", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            if (usuarioController.registrarUsuario(nombre.getText(), email.getText(), profesion.getText(), descripcion.getText())) {
                Usuario user = usuarioController.buscarPorEmail(email.getText());
                if (user != null) {
                    user.setPassword(new String(password.getPassword()));
                    loginProfesionalPorId(user.getId());
                }
                JOptionPane.showMessageDialog(this, "Profesional registrado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar. Datos inválidos o email ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registrarEmpresa() {
        JPanel p = new JPanel(new GridLayout(5, 2, 4, 4));
        JTextField nombre = new JTextField();
        JTextField email = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField rubro = new JTextField();
        JTextField descripcion = new JTextField();
        p.add(new JLabel("Nombre empresa:")); p.add(nombre);
        p.add(new JLabel("Email:")); p.add(email);
        p.add(new JLabel("Contraseña:")); p.add(password);
        p.add(new JLabel("Rubro:")); p.add(rubro);
        p.add(new JLabel("Descripción (opcional):")); p.add(descripcion);
        int res = JOptionPane.showConfirmDialog(this, p, "Registrar Empresa", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            String emailText = email.getText();
            if (empleadorController.registrarEmpleador(nombre.getText(), rubro.getText(), descripcion.getText(), emailText)) {
                Empleador emp = empleadorController.buscarPorEmail(emailText);
                if (emp != null) {
                    emp.setPassword(new String(password.getPassword()));
                    loginEmpresaPorId(emp.getId());
                }
                JOptionPane.showMessageDialog(this, "Empresa registrada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar. Datos inválidos o ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
