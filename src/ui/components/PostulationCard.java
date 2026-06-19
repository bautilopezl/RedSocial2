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
import modelo.OfertaLaboral;
import modelo.Postulacion;
import modelo.Usuario;
import ui.theme.ThemeManager;

public class PostulationCard extends RoundedPanel {

    // Professional view — shows the job the user applied to
    public PostulationCard(Postulacion postulacion) {
        super(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        setShadowEnabled(true);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));

        OfertaLaboral oferta = postulacion.getOferta();
        String tituloOferta = oferta != null ? oferta.getTitulo() : "Oferta no disponible";
        String empresa = oferta != null && oferta.getEmpleador() != null
            ? oferta.getEmpleador().getNombreEmpresa()
            : "Empresa no disponible";

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitulo = new JLabel(tituloOferta);
        lblTitulo.setFont(ThemeManager.F_HEAD_BOLD);
        lblTitulo.setForeground(ThemeManager.PRIMARY);
        add(lblTitulo, gbc);

        gbc.gridy++;
        JLabel lblEmpresa = new JLabel(empresa);
        lblEmpresa.setFont(ThemeManager.F_SMALL_BOLD);
        lblEmpresa.setForeground(ThemeManager.TEXT_SECONDARY);
        add(lblEmpresa, gbc);

        if (oferta != null) {
            gbc.gridy++;
            JLabel lblEstado = new JLabel(oferta.isActiva() ? "\u25cf Activa" : "\u25cf Cerrada");
            lblEstado.setFont(ThemeManager.F_SMALL_BOLD);
            lblEstado.setForeground(oferta.isActiva() ? ThemeManager.SUCCESS : ThemeManager.ERROR);
            add(lblEstado, gbc);
        }
    }

    // Employer view — shows candidate info with accept/reject actions
    public PostulationCard(Postulacion postulacion, Runnable onAccept, Runnable onReject) {
        super(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        setShadowEnabled(true);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));

        Usuario user = postulacion.getUsuario();
        OfertaLaboral oferta = postulacion.getOferta();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 4, 0);

        JLabel lblNombre = new JLabel(user != null ? user.getNombre() : "N/A");
        lblNombre.setFont(ThemeManager.F_HEAD_BOLD);
        lblNombre.setForeground(ThemeManager.PRIMARY);
        add(lblNombre, gbc);

        gbc.gridy++;
        JLabel lblId = new JLabel("ID: " + (user != null ? user.getId() : "N/A"));
        lblId.setFont(ThemeManager.F_XS_PLAIN);
        lblId.setForeground(ThemeManager.TEXT_MUTED);
        add(lblId, gbc);

        gbc.gridy++;
        JLabel lblEmail = new JLabel("Email: " + (user != null ? user.getEmail() : "N/A"));
        lblEmail.setFont(ThemeManager.F_SMALL_PLAIN);
        add(lblEmail, gbc);

        gbc.gridy++;
        JLabel lblProfesion = new JLabel("Profesi\u00f3n: " + (user != null ? user.getProfesion() : "N/A"));
        lblProfesion.setFont(ThemeManager.F_SMALL_PLAIN);
        lblProfesion.setForeground(ThemeManager.TEXT_SECONDARY);
        add(lblProfesion, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(4, 0, 4, 0);
        JLabel lblDescripcion = new JLabel("<html><p style='width:450px'>"
            + (user != null ? user.getDescripcion() : "N/A") + "</p></html>");
        lblDescripcion.setFont(ThemeManager.F_SMALL_PLAIN);
        add(lblDescripcion, gbc);

        if (user != null && user.cantidadHabilidades() > 0) {
            gbc.gridy++;
            gbc.insets = new Insets(6, 0, 4, 0);
            JPanel skillsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
            skillsPanel.setOpaque(false);

            JLabel lblSkills = new JLabel("Habilidades: ");
            lblSkills.setFont(ThemeManager.F_SMALL_BOLD);
            lblSkills.setForeground(ThemeManager.TEXT_DESCRIPTION);
            skillsPanel.add(lblSkills);

            for (String habilidad : user.obtenerHabilidades()) {
                skillsPanel.add(new SkillBadge(habilidad));
            }
            add(skillsPanel, gbc);
        }

        gbc.gridy++;
        gbc.insets = new Insets(8, 0, 0, 0);
        JLabel lblOferta = new JLabel("Postulado a: " + (oferta != null ? oferta.getTitulo() : "N/A"));
        lblOferta.setFont(ThemeManager.F_SMALL_BOLD);
        lblOferta.setForeground(ThemeManager.PRIMARY);
        add(lblOferta, gbc);

        if (onAccept != null && onReject != null) {
            gbc.gridy++;
            gbc.insets = new Insets(12, 0, 0, 0);
            JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            acciones.setOpaque(false);

            RoundedButton btnAceptar = new RoundedButton("Aceptar", true);
            btnAceptar.setPreferredSize(new Dimension(110, 32));
            btnAceptar.setBackground(ThemeManager.SUCCESS);
            btnAceptar.addActionListener(e -> onAccept.run());
            acciones.add(btnAceptar);

            RoundedButton btnRechazar = new RoundedButton("Rechazar", false);
            btnRechazar.setPreferredSize(new Dimension(110, 32));
            btnRechazar.addActionListener(e -> onReject.run());
            acciones.add(btnRechazar);

            add(acciones, gbc);
        }
    }
}
