package ui.views.empresa;

import controladores.PostulacionController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import modelo.OfertaLaboral;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.RoundedScrollPane;
import ui.components.SectionHeader;
import ui.state.SessionManager;
import ui.theme.ThemeManager;

public class CompanyJobsPanel extends JPanel {
    private final PostulacionController postulacionController;
    private final JPanel ofertasGrid;

    public CompanyJobsPanel(PostulacionController postulacionController) {
        this.postulacionController = postulacionController;

        setBackground(ThemeManager.BG_GENERAL);
        setLayout(new BorderLayout(ThemeManager.CARD_GAP, ThemeManager.CARD_GAP));
        setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN, ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setOpaque(false);

        RoundedButton btnPublicar = new RoundedButton("+ Publicar nueva oferta", true);
        btnPublicar.setPreferredSize(new Dimension(220, ThemeManager.BTN_H));
        btnPublicar.addActionListener(e -> mostrarDialogoCrear());
        toolbar.add(btnPublicar);

        add(toolbar, BorderLayout.NORTH);

        JPanel contentContainer = new JPanel(new GridBagLayout());
        contentContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;

        RoundedPanel panelPrincipal = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));
        panelPrincipal.add(new SectionHeader("Mis Ofertas Laborales"), BorderLayout.NORTH);

        ofertasGrid = new JPanel(new GridBagLayout());
        ofertasGrid.setOpaque(false);
        panelPrincipal.add(ofertasGrid, BorderLayout.CENTER);

        contentContainer.add(panelPrincipal, gbc);

        gbc.gridy++; gbc.weighty = 1.0;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        contentContainer.add(filler, gbc);

        RoundedScrollPane scroll = new RoundedScrollPane(contentContainer);

        add(scroll, BorderLayout.CENTER);
    }

    public void cargarDatos() {
        ofertasGrid.removeAll();
        int idEmpresa = SessionManager.getInstance().getCurrentUserId();
        if (idEmpresa < 0) return;

        OfertaLaboral[] ofertas = postulacionController.obtenerOfertasPorEmpleador(idEmpresa);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, ThemeManager.PADDING - 1, 0);

        if (ofertas == null || ofertas.length == 0) {
            JLabel lblVacio = new JLabel("Aún no has publicado ninguna oferta laboral.");
            lblVacio.setForeground(Color.GRAY);
            ofertasGrid.add(lblVacio, gbc);
        } else {
            for (OfertaLaboral of : ofertas) {
                if (of == null) continue;
                ofertasGrid.add(crearOfertaCard(of), gbc);
                gbc.gridy++;
            }
        }

        revalidate();
        repaint();
    }

    private JPanel crearOfertaCard(OfertaLaboral oferta) {
        RoundedPanel card = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        card.setShadowEnabled(true);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 0);

        JLabel lblTitulo = new JLabel(oferta.getTitulo());
        lblTitulo.setFont(ThemeManager.F_HEAD_BOLD);
        lblTitulo.setForeground(ThemeManager.PRIMARY);
        card.add(lblTitulo, gbc);

        gbc.gridy++;
        JLabel lblDesc = new JLabel("<html><p style='width:400px'>" + oferta.getDescripcion() + "</p></html>");
        lblDesc.setFont(ThemeManager.F_SMALL_PLAIN);
        card.add(lblDesc, gbc);

        gbc.gridy++;
        JLabel lblEstado = new JLabel(oferta.isActiva() ? "● Activa" : "● Cerrada");
        lblEstado.setFont(ThemeManager.F_SMALL_BOLD);
        lblEstado.setForeground(oferta.isActiva() ? ThemeManager.SUCCESS : ThemeManager.ERROR);
        card.add(lblEstado, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        acciones.setOpaque(false);

        RoundedButton btnEditar = new RoundedButton("Editar", false);
        btnEditar.setPreferredSize(new Dimension(100, 30));
        btnEditar.addActionListener(e -> mostrarDialogoEditar(oferta));
        acciones.add(btnEditar);

        if (oferta.isActiva()) {
            RoundedButton btnCerrar = new RoundedButton("Cerrar", false);
            btnCerrar.setPreferredSize(new Dimension(100, 30));
            btnCerrar.addActionListener(e -> cerrarOferta(oferta));
            acciones.add(btnCerrar);
        }

        RoundedButton btnEliminar = new RoundedButton("Eliminar", false);
        btnEliminar.setPreferredSize(new Dimension(100, 30));
        btnEliminar.addActionListener(e -> eliminarOferta(oferta));
        acciones.add(btnEliminar);

        card.add(acciones, gbc);

        return card;
    }

    private void mostrarDialogoCrear() {
        JTextField txtTitulo = new JTextField(20);
        JTextArea txtDesc = new JTextArea(5, 20);
        txtDesc.setLineWrap(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        form.add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        form.add(new RoundedScrollPane(txtDesc), gbc);

        int result = JOptionPane.showConfirmDialog(
            this, form, "Publicar nueva oferta",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String titulo = txtTitulo.getText().trim();
            String desc = txtDesc.getText().trim();
            int idEmpresa = SessionManager.getInstance().getCurrentUserId();

            if (postulacionController.registrarOfertaLaboral(titulo, desc, idEmpresa)) {
                JOptionPane.showMessageDialog(this, "Oferta publicada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al publicar la oferta. Verifica que todos los campos estén completos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoEditar(OfertaLaboral oferta) {
        JTextField txtTitulo = new JTextField(oferta.getTitulo(), 20);
        JTextArea txtDesc = new JTextArea(oferta.getDescripcion(), 5, 20);
        txtDesc.setLineWrap(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        form.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        form.add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        form.add(new RoundedScrollPane(txtDesc), gbc);

        int result = JOptionPane.showConfirmDialog(
            this, form, "Editar Oferta - " + oferta.getTitulo(),
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String titulo = txtTitulo.getText().trim();
            String desc = txtDesc.getText().trim();

            if (postulacionController.actualizarOferta(oferta.getId(), titulo, desc)) {
                JOptionPane.showMessageDialog(this, "Oferta actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la oferta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cerrarOferta(OfertaLaboral oferta) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de cerrar la oferta \"" + oferta.getTitulo() + "\"?",
            "Cerrar Oferta", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (postulacionController.cerrarOferta(oferta.getId())) {
                JOptionPane.showMessageDialog(this, "Oferta cerrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al cerrar la oferta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarOferta(OfertaLaboral oferta) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de eliminar la oferta \"" + oferta.getTitulo() + "\"?\nEsta acción no se puede deshacer.",
            "Eliminar Oferta", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (postulacionController.eliminarOferta(oferta.getId())) {
                JOptionPane.showMessageDialog(this, "Oferta eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la oferta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
