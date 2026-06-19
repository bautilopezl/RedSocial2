package ui.views.empresa;

import controladores.HabilidadController;
import controladores.PostulacionController;
import controladores.UsuarioController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import modelo.Postulacion;
import modelo.Usuario;
import ui.components.PostulationCard;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.RoundedScrollPane;
import ui.components.RoundedTextField;
import ui.components.SectionHeader;
import ui.components.UserCard;
import ui.state.SessionManager;
import ui.theme.ThemeManager;

public class CandidatesPanel extends JPanel {
    private final PostulacionController postulacionController;
    private final UsuarioController usuarioController;
    private final HabilidadController habilidadController;
    private final JPanel colaSection;
    private final JPanel searchResultsGrid;
    private final RoundedTextField searchField;
    private JLabel lblColaCount;
    private int totalColaGlobal;

    public CandidatesPanel(UsuarioController usuarioController, PostulacionController postulacionController, HabilidadController habilidadController) {
        this.usuarioController = usuarioController;
        this.postulacionController = postulacionController;
        this.habilidadController = habilidadController;

        setBackground(ThemeManager.BG_GENERAL);
        setLayout(new BorderLayout(ThemeManager.CARD_GAP, ThemeManager.CARD_GAP));
        setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN, ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN));

        JPanel contentContainer = new JPanel(new GridBagLayout());
        contentContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);

        RoundedPanel colaPanel = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        colaPanel.setLayout(new BorderLayout(10, 10));
        colaPanel.setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));

        JPanel colaHeader = new JPanel(new BorderLayout());
        colaHeader.setOpaque(false);
        colaHeader.add(new SectionHeader("Postulaciones Pendientes"), BorderLayout.WEST);
        lblColaCount = new JLabel("0 en cola");
        lblColaCount.setFont(ThemeManager.F_BODY_BOLD);
        lblColaCount.setForeground(ThemeManager.TEXT_SECONDARY);
        colaHeader.add(lblColaCount, BorderLayout.EAST);
        colaPanel.add(colaHeader, BorderLayout.NORTH);

        colaSection = new JPanel(new BorderLayout());
        colaSection.setOpaque(false);
        colaPanel.add(colaSection, BorderLayout.CENTER);

        contentContainer.add(colaPanel, gbc);

        gbc.gridy++;
        RoundedPanel searchPanel = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        searchPanel.setLayout(new BorderLayout(10, 10));
        searchPanel.setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));

        searchPanel.add(new SectionHeader("Buscar Profesionales"), BorderLayout.NORTH);

        JPanel searchToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchToolbar.setOpaque(false);
        searchField = new RoundedTextField();
        searchField.setPreferredSize(new Dimension(ThemeManager.SEARCH_W, ThemeManager.INPUT_H));
        RoundedButton btnSearch = new RoundedButton("Buscar", true);
        btnSearch.setPreferredSize(new Dimension(ThemeManager.BTN_W, ThemeManager.BTN_H));
        btnSearch.addActionListener(e -> buscarProfesionales(searchField.getText()));
        searchToolbar.add(searchField);
        searchToolbar.add(btnSearch);
        searchPanel.add(searchToolbar, BorderLayout.CENTER);

        searchResultsGrid = new JPanel(new GridBagLayout());
        searchResultsGrid.setOpaque(false);
        searchPanel.add(searchResultsGrid, BorderLayout.SOUTH);

        contentContainer.add(searchPanel, gbc);

        gbc.gridy++; gbc.weighty = 1.0;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        contentContainer.add(filler, gbc);

        RoundedScrollPane scroll = new RoundedScrollPane(contentContainer);

        add(scroll, BorderLayout.CENTER);
    }

    public void cargarDatos() {
        actualizarCola();
        searchField.setText("");
        searchResultsGrid.removeAll();
        revalidate();
        repaint();
    }

    private void actualizarCola() {
        colaSection.removeAll();
        int empleadorId = SessionManager.getInstance().getCurrentUserId();
        if (empleadorId < 0) return;

        Postulacion[] misPostulaciones = postulacionController.obtenerPostulacionesPorEmpleador(empleadorId);
        totalColaGlobal = postulacionController.cantidadPostulacionesPendientes();
        lblColaCount.setText((misPostulaciones != null ? misPostulaciones.length : 0) + " para mi empresa (" + totalColaGlobal + " global)");

        JPanel colaDetailPanel = new JPanel(new GridBagLayout());
        colaDetailPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);

        if (misPostulaciones == null || misPostulaciones.length == 0) {
            JLabel lblVacio = new JLabel("No hay postulaciones para tus ofertas.");
            lblVacio.setForeground(Color.GRAY);
            colaDetailPanel.add(lblVacio, gbc);
        } else {
            for (Postulacion p : misPostulaciones) {
                if (p != null) {
                    Postulacion captured = p;
                    colaDetailPanel.add(new PostulationCard(p,
                        () -> aceptarPostulacion(captured),
                        () -> rechazarPostulacion(captured)), gbc);
                    gbc.gridy++;
                }
            }
        }

        colaSection.add(colaDetailPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void aceptarPostulacion(Postulacion postulacion) {
        if (postulacion == null) return;
        String nombre = postulacion.getUsuario() != null ? postulacion.getUsuario().getNombre() : "N/A";
        String oferta = postulacion.getOferta() != null ? postulacion.getOferta().getTitulo() : "N/A";

        postulacionController.procesarPostulacion();
        JOptionPane.showMessageDialog(this,
            "Postulación ACEPTADA.\n\nCandidato: " + nombre + "\nOferta: " + oferta,
            "Postulación Aceptada", JOptionPane.INFORMATION_MESSAGE);
        actualizarCola();
    }

    private void rechazarPostulacion(Postulacion postulacion) {
        if (postulacion == null) return;
        String nombre = postulacion.getUsuario() != null ? postulacion.getUsuario().getNombre() : "N/A";
        String oferta = postulacion.getOferta() != null ? postulacion.getOferta().getTitulo() : "N/A";

        postulacionController.procesarPostulacion();
        JOptionPane.showMessageDialog(this,
            "Postulación RECHAZADA.\n\nCandidato: " + nombre + "\nOferta: " + oferta,
            "Postulación Rechazada", JOptionPane.INFORMATION_MESSAGE);
        actualizarCola();
    }

    private void buscarProfesionales(String query) {
        searchResultsGrid.removeAll();
        Usuario[] todos = usuarioController.obtenerTodosLosUsuarios();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        boolean found = false;
        String busqueda = query == null ? "" : query.toLowerCase().trim();

        if (todos != null) {
            for (Usuario u : todos) {
                if (u == null) continue;
                boolean match = busqueda.isEmpty()
                    || u.getNombre().toLowerCase().contains(busqueda)
                    || u.getProfesion().toLowerCase().contains(busqueda)
                    || u.getEmail().toLowerCase().contains(busqueda);
                if (match) {
                    found = true;
                    UserCard card = new UserCard(u, "Ver Perfil", () -> mostrarPerfilProfesional(u));
                    searchResultsGrid.add(card, gbc);
                    gbc.gridy++;
                }
            }
        }

        if (!found) {
            JLabel lblVacio = new JLabel(busqueda.isEmpty()
                ? "No hay profesionales registrados."
                : "No se encontraron profesionales para \"" + query + "\".");
            lblVacio.setForeground(Color.GRAY);
            searchResultsGrid.add(lblVacio, gbc);
        }

        revalidate();
        repaint();
    }

    private void mostrarPerfilProfesional(Usuario usuario) {
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 15, 8, 15);

        JLabel lblNombre = new JLabel(usuario.getNombre());
        lblNombre.setFont(ThemeManager.F_HEAD_BOLD);
        lblNombre.setForeground(ThemeManager.PRIMARY);
        profilePanel.add(lblNombre, gbc);

        gbc.gridy++;
        JLabel lblEmail = new JLabel(usuario.getEmail());
        lblEmail.setFont(ThemeManager.F_SMALL_PLAIN);
        lblEmail.setForeground(ThemeManager.TEXT_SECONDARY);
        profilePanel.add(lblEmail, gbc);

        gbc.gridy++;
        JLabel lblProfesion = new JLabel(usuario.getProfesion());
        lblProfesion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        profilePanel.add(lblProfesion, gbc);

        if (!usuario.getDescripcion().isEmpty()) {
            gbc.gridy++;
            gbc.insets = new Insets(6, 15, 4, 15);
            profilePanel.add(new JLabel("<html><p style='width:350px'>" + usuario.getDescripcion() + "</p></html>"), gbc);
        }

        gbc.gridy++;
        gbc.insets = new Insets(10, 15, 4, 15);

        String[] userSkills = usuario.obtenerHabilidades();

        JTree arbolHabilidades = construirArbolHabilidades(userSkills);
        RoundedScrollPane scrollTree = new RoundedScrollPane(arbolHabilidades);
        scrollTree.setPreferredSize(new Dimension(350, 200));
        profilePanel.add(scrollTree, gbc);

        JOptionPane.showMessageDialog(this, profilePanel, "Perfil de " + usuario.getNombre(), JOptionPane.PLAIN_MESSAGE);
    }

    private JTree construirArbolHabilidades(String[] userSkills) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Habilidades Profesionales");
        String jerarquiaStr = habilidadController.obtenerJerarquiaTexto();

        if (jerarquiaStr != null && !jerarquiaStr.contains("No hay categorías registradas")) {
            String[] lineas = jerarquiaStr.split("\n");
            DefaultMutableTreeNode[] ultimosNodosPorNivel = new DefaultMutableTreeNode[50];
            ultimosNodosPorNivel[0] = root;

            for (String linea : lineas) {
                linea = linea.replaceAll("\r", "");
                if (linea.trim().isEmpty()) continue;

                int nivel = 0;
                while (linea.startsWith("  ")) {
                    nivel++;
                    linea = linea.substring(2);
                }

                String nombreCat = linea.replace("- ", "").trim();
                DefaultMutableTreeNode nuevoNodo = new DefaultMutableTreeNode(nombreCat);
                ultimosNodosPorNivel[nivel].add(nuevoNodo);
                ultimosNodosPorNivel[nivel + 1] = nuevoNodo;
            }
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        JTree tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);

        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                String nodeName = (String) node.getUserObject();

                boolean esHabilidadDelUsuario = false;
                for (String skill : userSkills) {
                    if (skill != null && skill.equalsIgnoreCase(nodeName)) {
                        esHabilidadDelUsuario = true;
                        break;
                    }
                }

                if (esHabilidadDelUsuario) {
                    setForeground(ThemeManager.SUCCESS);
                    setFont(getFont().deriveFont(Font.BOLD));
                    setText("✓ " + nodeName);
                } else {
                    setForeground(Color.BLACK);
                    setFont(getFont().deriveFont(Font.PLAIN));
                    setText(nodeName);
                }

                return c;
            }
        });

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        return tree;
    }
}
