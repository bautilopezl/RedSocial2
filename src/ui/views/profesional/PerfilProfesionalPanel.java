package ui.views.profesional;

import controladores.ContactoController;
import controladores.HabilidadController;
import controladores.UsuarioController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import modelo.Usuario;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.RoundedScrollPane;
import ui.components.RoundedTextField;
import ui.state.SessionManager;
import ui.theme.ThemeManager;

public class PerfilProfesionalPanel extends JPanel {
    private final UsuarioController usuarioController;
    private final HabilidadController habilidadController;
    private final ContactoController contactoController;

    public PerfilProfesionalPanel(UsuarioController usuarioController, HabilidadController habilidadController, ContactoController contactoController) {
        this.usuarioController = usuarioController;
        this.habilidadController = habilidadController;
        this.contactoController = contactoController;

        setBackground(ThemeManager.BG_GENERAL);
        setLayout(new BorderLayout(ThemeManager.CARD_GAP, ThemeManager.CARD_GAP));
        setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN, ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN));
    }

    public void cargarDatos() {
        removeAll();
        int idUsuario = SessionManager.getInstance().getCurrentUserId();
        if (idUsuario < 0) return;

        Usuario usuario = usuarioController.buscarUsuario(idUsuario);
        if (usuario == null) return;

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);

        RoundedPanel cardInfo = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        cardInfo.setShadowEnabled(true);
        cardInfo.setLayout(new GridBagLayout());
        cardInfo.setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W, ThemeManager.BORDER_W));
        GridBagConstraints gbcInfo = new GridBagConstraints();
        gbcInfo.gridx = 0; gbcInfo.gridy = 0; gbcInfo.anchor = GridBagConstraints.WEST;
        gbcInfo.insets = new Insets(0, 0, 10, 0);

        RoundedPanel avatar = new RoundedPanel(ThemeManager.RADIUS_AVATAR, ThemeManager.AVATAR_BG);
        avatar.setPreferredSize(new Dimension(ThemeManager.RADIUS_AVATAR, ThemeManager.RADIUS_AVATAR));
        cardInfo.add(avatar, gbcInfo);

        gbcInfo.gridy++; gbcInfo.insets = new Insets(5, 0, 5, 0);
        JLabel lblNombre = new JLabel(usuario.getNombre());
        lblNombre.setFont(ThemeManager.F_TITLE_BOLD);
        cardInfo.add(lblNombre, gbcInfo);

        gbcInfo.gridy++; gbcInfo.insets = new Insets(0, 0, 10, 0);
        JLabel lblProf = new JLabel(usuario.getProfesion());
        lblProf.setFont(ThemeManager.F_HEAD_BOLD);
        lblProf.setForeground(ThemeManager.TEXT_DESCRIPTION);
        cardInfo.add(lblProf, gbcInfo);

        gbcInfo.gridy++; gbcInfo.insets = new Insets(0, 0, 15, 0);
        JLabel lblDesc = new JLabel("<html><p style='width:500px'>" + (usuario.getDescripcion().isEmpty() ? "Sin descripci\u00f3n" : usuario.getDescripcion()) + "</p></html>");
        lblDesc.setForeground(Color.DARK_GRAY);
        cardInfo.add(lblDesc, gbcInfo);

        gbcInfo.gridy++; gbcInfo.insets = new Insets(5, 0, 8, 0);
        JLabel lblSkillsTitle = new JLabel("Habilidades");
        lblSkillsTitle.setFont(ThemeManager.F_HEAD_BOLD);
        lblSkillsTitle.setForeground(ThemeManager.PRIMARY);
        cardInfo.add(lblSkillsTitle, gbcInfo);

        gbcInfo.gridy++; gbcInfo.insets = new Insets(0, 0, 10, 0);
        gbcInfo.fill = GridBagConstraints.HORIZONTAL;
        gbcInfo.weightx = 1.0;
        JTree arbol = construirArbolJerarquicoFiltrado(usuario.obtenerHabilidades());
        RoundedScrollPane scrollTree = new RoundedScrollPane(arbol);
        scrollTree.setPreferredSize(new Dimension(500, 180));
        cardInfo.add(scrollTree, gbcInfo);

        gbcInfo.gridy++; gbcInfo.fill = GridBagConstraints.NONE;
        gbcInfo.weightx = 0;
        gbcInfo.insets = new Insets(5, 0, 0, 0);
        RoundedButton btnEditar = new RoundedButton("Editar Perfil", false);
        btnEditar.setPreferredSize(new Dimension(ThemeManager.BTN_W, ThemeManager.BTN_H));
        btnEditar.addActionListener(e -> abrirEditorPerfil(usuario));
        cardInfo.add(btnEditar, gbcInfo);

        content.add(cardInfo, gbc);

        RoundedScrollPane scroll = new RoundedScrollPane(content);
        add(scroll, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JTree construirArbolJerarquicoFiltrado(String[] userSkills) {
        Set<String> skillSet = new HashSet<>();
        if (userSkills != null) {
            for (String s : userSkills) {
                if (s != null && !s.trim().isEmpty() && !s.equals("Ninguna")) {
                    skillSet.add(s.trim());
                }
            }
        }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mis Habilidades");
        String jerarquiaStr = habilidadController.obtenerJerarquiaTexto();

        if (jerarquiaStr != null && !jerarquiaStr.contains("No hay categor\u00edas")) {
            String[] lineas = jerarquiaStr.split("\n");
            DefaultMutableTreeNode[] ultimos = new DefaultMutableTreeNode[50];
            ultimos[0] = root;
            for (String raw : lineas) {
                raw = raw.replaceAll("\r", "");
                if (raw.trim().isEmpty()) continue;
                int nivel = 0;
                String content = raw;
                while (content.startsWith("  ")) {
                    nivel++;
                    content = content.substring(2);
                }
                content = content.replace("- ", "").trim();
                DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(content);
                ultimos[nivel].add(nodo);
                ultimos[nivel + 1] = nodo;
            }
            podarArbol(root, skillSet);
        }

        DefaultTreeModel model = new DefaultTreeModel(root);
        JTree tree = new JTree(model);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                String name = (String) node.getUserObject();
                if (skillSet.contains(name)) {
                    setForeground(ThemeManager.SUCCESS);
                    setFont(getFont().deriveFont(Font.BOLD));
                    setText("\u2713 " + name);
                } else {
                    setForeground(ThemeManager.TEXT_PRIMARY);
                    setFont(getFont().deriveFont(Font.PLAIN));
                    setText(name);
                }
                return c;
            }
        });
        for (int i = 0; i < tree.getRowCount(); i++) tree.expandRow(i);
        return tree;
    }

    private boolean podarArbol(DefaultMutableTreeNode node, Set<String> skillSet) {
        String name = (String) node.getUserObject();
        boolean tieneSkill = skillSet.contains(name);
        for (int i = node.getChildCount() - 1; i >= 0; i--) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            if (!podarArbol(child, skillSet)) {
                child.removeFromParent();
            } else {
                tieneSkill = true;
            }
        }
        return tieneSkill;
    }

    private String obtenerRaizJerarquia() {
        String jerarquia = habilidadController.obtenerJerarquiaTexto();
        if (jerarquia == null || jerarquia.contains("No hay categor\u00edas")) {
            return "Tecnolog\u00eda";
        }
        String primeraLinea = jerarquia.split("\n")[0].trim();
        if (primeraLinea.startsWith("- ")) {
            return primeraLinea.substring(2).trim();
        }
        return primeraLinea;
    }

    private void abrirEditorPerfil(Usuario usuario) {
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;

        Set<String> checkedSkills = new HashSet<>();
        String[] userSkills = usuario.obtenerHabilidades();
        if (userSkills != null) {
            for (String s : userSkills) {
                if (s != null && !s.trim().isEmpty() && !s.equals("Ninguna")) {
                    checkedSkills.add(s.trim());
                }
            }
        }

        JDialog dialog = new JDialog();
        dialog.setTitle("Editar Perfil - " + usuario.getNombre());
        dialog.setModal(true);
        dialog.setSize(560, 620);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(ThemeManager.PANEL);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gfc = new GridBagConstraints();
        gfc.gridx = 0; gfc.gridy = 0; gfc.anchor = GridBagConstraints.WEST;
        gfc.insets = new Insets(0, 0, 4, 0);

        JLabel lblFormTitle = new JLabel("Datos del Perfil");
        lblFormTitle.setFont(ThemeManager.F_HEAD_BOLD);
        lblFormTitle.setForeground(ThemeManager.PRIMARY);
        formPanel.add(lblFormTitle, gfc);

        gfc.gridy++; gfc.insets = new Insets(8, 0, 3, 0);
        formPanel.add(new JLabel("Nombre:"), gfc);
        gfc.gridy++; gfc.insets = new Insets(0, 0, 6, 0);
        RoundedTextField txtNombre = new RoundedTextField();
        txtNombre.setText(usuario.getNombre());
        txtNombre.setPreferredSize(new Dimension(500, ThemeManager.INPUT_H));
        formPanel.add(txtNombre, gfc);

        gfc.gridy++; gfc.insets = new Insets(0, 0, 3, 0);
        formPanel.add(new JLabel("Email:"), gfc);
        gfc.gridy++; gfc.insets = new Insets(0, 0, 6, 0);
        RoundedTextField txtEmail = new RoundedTextField();
        txtEmail.setText(usuario.getEmail());
        txtEmail.setPreferredSize(new Dimension(500, ThemeManager.INPUT_H));
        formPanel.add(txtEmail, gfc);

        gfc.gridy++; gfc.insets = new Insets(0, 0, 3, 0);
        formPanel.add(new JLabel("Profesi\u00f3n:"), gfc);
        gfc.gridy++; gfc.insets = new Insets(0, 0, 6, 0);
        RoundedTextField txtProfesion = new RoundedTextField();
        txtProfesion.setText(usuario.getProfesion());
        txtProfesion.setPreferredSize(new Dimension(500, ThemeManager.INPUT_H));
        formPanel.add(txtProfesion, gfc);

        gfc.gridy++; gfc.insets = new Insets(0, 0, 3, 0);
        formPanel.add(new JLabel("Descripci\u00f3n:"), gfc);
        gfc.gridy++; gfc.insets = new Insets(0, 0, 6, 0);
        gfc.fill = GridBagConstraints.HORIZONTAL;
        gfc.weightx = 1.0;
        JTextArea txtDescripcion = new JTextArea(usuario.getDescripcion());
        txtDescripcion.setFont(ThemeManager.F_BODY_PLAIN);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBorder(new EmptyBorder(5, 5, 5, 5));
        RoundedScrollPane scrollDesc = new RoundedScrollPane(txtDescripcion);
        scrollDesc.setPreferredSize(new Dimension(500, 60));
        formPanel.add(scrollDesc, gfc);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        JPanel treeSection = new JPanel(new BorderLayout(5, 5));
        treeSection.setOpaque(false);

        JLabel lblTreeTitle = new JLabel("Habilidades (marc\u00e1 las que quer\u00e9s tener en tu perfil)");
        lblTreeTitle.setFont(ThemeManager.F_HEAD_BOLD);
        lblTreeTitle.setForeground(ThemeManager.PRIMARY);
        treeSection.add(lblTreeTitle, BorderLayout.NORTH);

        JTree arbolCompleto = construirArbolJerarquico(checkedSkills);
        JScrollPane scrollArbol = new JScrollPane(arbolCompleto);
        scrollArbol.setBorder(BorderFactory.createLineBorder(ThemeManager.BORDER));
        scrollArbol.setPreferredSize(new Dimension(500, 200));
        treeSection.add(scrollArbol, BorderLayout.CENTER);

        arbolCompleto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath path = arbolCompleto.getPathForLocation(e.getX(), e.getY());
                if (path == null) return;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                String name = (String) node.getUserObject();
                if (name.equals(obtenerRaizJerarquia())) return;

                toggleCheck(node, checkedSkills);
                arbolCompleto.repaint();
            }
        });

        treeSection.add(Box.createRigidArea(new Dimension(0, 6)), BorderLayout.SOUTH);
        mainPanel.add(treeSection, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new GridBagLayout());
        acciones.setOpaque(false);
        GridBagConstraints gba = new GridBagConstraints();
        gba.gridx = 0; gba.gridy = 0; gba.insets = new Insets(10, 0, 0, 10);

        RoundedButton btnCancelar = new RoundedButton("Cancelar", false);
        btnCancelar.setPreferredSize(new Dimension(130, 35));
        btnCancelar.addActionListener(e -> dialog.dispose());
        acciones.add(btnCancelar, gba);

        gba.gridx = 1; gba.insets = new Insets(10, 0, 0, 0);
        RoundedButton btnGuardar = new RoundedButton("Guardar Cambios", true);
        btnGuardar.setPreferredSize(new Dimension(180, 35));
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String profesion = txtProfesion.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            if (nombre.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nombre y Email son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            usuarioController.modificarUsuario(miId, nombre, email, profesion, descripcion);

            String[] oldSkills = usuario.obtenerHabilidades();
            if (oldSkills != null) {
                for (String s : oldSkills) {
                    if (s != null && !s.trim().isEmpty() && !s.equals("Ninguna")) {
                        habilidadController.desasociarHabilidad(miId, s.trim());
                    }
                }
            }
            for (String s : checkedSkills) {
                habilidadController.asociarHabilidad(miId, s);
            }

            JOptionPane.showMessageDialog(dialog, "Perfil actualizado exitosamente.", "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            cargarDatos();
        });
        acciones.add(btnGuardar, gba);

        mainPanel.add(acciones, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private JTree construirArbolJerarquico(Set<String> checkedSkills) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(obtenerRaizJerarquia());
        String jerarquiaStr = habilidadController.obtenerJerarquiaTexto();

        if (jerarquiaStr != null && !jerarquiaStr.contains("No hay categor\u00edas")) {
            String[] lineas = jerarquiaStr.split("\n");
            DefaultMutableTreeNode[] ultimos = new DefaultMutableTreeNode[50];
            ultimos[0] = root;
            for (String raw : lineas) {
                raw = raw.replaceAll("\r", "");
                if (raw.trim().isEmpty()) continue;
                int nivel = 0;
                String content = raw;
                while (content.startsWith("  ")) {
                    nivel++;
                    content = content.substring(2);
                }
                content = content.replace("- ", "").trim();
                DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(content);
                ultimos[nivel].add(nodo);
                ultimos[nivel + 1] = nodo;
            }
        }

        DefaultTreeModel model = new DefaultTreeModel(root);
        JTree tree = new JTree(model);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                String name = (String) node.getUserObject();
                boolean checked = checkedSkills.contains(name);
                setFont(getFont().deriveFont(Font.PLAIN));
                setForeground(checked ? ThemeManager.SUCCESS : ThemeManager.TEXT_PRIMARY);
                setText((checked ? "\u2611 " : "\u2610 ") + name);
                return c;
            }
        });
        for (int i = 0; i < tree.getRowCount(); i++) tree.expandRow(i);
        return tree;
    }

    private void toggleCheck(DefaultMutableTreeNode node, Set<String> checked) {
        String name = (String) node.getUserObject();
        if (checked.contains(name)) {
            checked.remove(name);
            uncheckedChildren(node, checked);
        } else {
            checked.add(name);
            checkChildren(node, checked);
        }
    }

    private void checkChildren(DefaultMutableTreeNode node, Set<String> checked) {
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            String name = (String) child.getUserObject();
            checked.add(name);
            checkChildren(child, checked);
        }
    }

    private void uncheckedChildren(DefaultMutableTreeNode node, Set<String> checked) {
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            String name = (String) child.getUserObject();
            checked.remove(name);
            uncheckedChildren(child, checked);
        }
    }
}
