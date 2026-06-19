package ui.views.profesional;

import controladores.HabilidadController;
import java.awt.BorderLayout;
import java.awt.Dimension;
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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.RoundedScrollPane;
import ui.components.SectionHeader;
import ui.state.SessionManager;
import ui.theme.ThemeManager;

public class HabilidadesPanel extends JPanel {
    private final HabilidadController habilidadController;
    private final JTree arbolUI;
    private DefaultTreeModel treeModel;
    private String habilidadSeleccionada = null;

    public HabilidadesPanel(HabilidadController habilidadController) {
        this.habilidadController = habilidadController;

        setBackground(ThemeManager.BG_GENERAL);
        setLayout(new BorderLayout(ThemeManager.CARD_GAP, ThemeManager.CARD_GAP));
        setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN, ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(new SectionHeader("Explorador de Habilidades"), BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;

        // Panel del JTree
        RoundedPanel treeContainer = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        treeContainer.setLayout(new BorderLayout());
        treeContainer.setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));

        arbolUI = new JTree();
        arbolUI.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        arbolUI.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) arbolUI.getLastSelectedPathComponent();
            if (node == null) return;
            habilidadSeleccionada = (String) node.getUserObject();
        });

        RoundedScrollPane scrollTree = new RoundedScrollPane(arbolUI);
        treeContainer.add(scrollTree, BorderLayout.CENTER);

        // Barra de acciones inferior
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actions.setOpaque(false);
        
        RoundedButton btnAgregar = new RoundedButton("Agregar a mi Perfil", true);
        btnAgregar.setPreferredSize(new Dimension(180, ThemeManager.BTN_H));
        btnAgregar.addActionListener(e -> agregarHabilidad());
        
        actions.add(btnAgregar);
        treeContainer.add(actions, BorderLayout.SOUTH);

        content.add(treeContainer, gbc);
        add(content, BorderLayout.CENTER);
    }

    public void cargarDatos() {
        habilidadSeleccionada = null;
        String jerarquiaStr = habilidadController.obtenerJerarquiaTexto();
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Habilidades Profesionales");
        
        if (!jerarquiaStr.contains("No hay categorías registradas")) {
            String[] lineas = jerarquiaStr.split("\n");
            
            // Usamos un array para mantener referencia al ultimo nodo insertado por nivel
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
                
                // nivel + 1 porque root es el nivel 0 para nosotros
                ultimosNodosPorNivel[nivel].add(nuevoNodo);
                ultimosNodosPorNivel[nivel + 1] = nuevoNodo;
            }
        }
        
        treeModel = new DefaultTreeModel(root);
        arbolUI.setModel(treeModel);
        
        // Expandir el arbol completo
        for (int i = 0; i < arbolUI.getRowCount(); i++) {
            arbolUI.expandRow(i);
        }
        
        revalidate();
        repaint();
    }

    private void agregarHabilidad() {
        if (habilidadSeleccionada == null || habilidadSeleccionada.equals("Habilidades Profesionales")) {
            JOptionPane.showMessageDialog(this, "Seleccione una habilidad específica del árbol.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;

        if (habilidadController.asociarHabilidad(miId, habilidadSeleccionada)) {
            JOptionPane.showMessageDialog(this, "Habilidad agregada a tu perfil exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo agregar la habilidad (quizás ya la posees).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
