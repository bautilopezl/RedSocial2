package ui.views.profesional;

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
import javax.swing.border.EmptyBorder;
import modelo.OfertaLaboral;
import ui.components.JobCard;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.RoundedScrollPane;
import ui.components.RoundedTextField;
import ui.components.SectionHeader;
import ui.state.SessionManager;
import ui.theme.ThemeManager;

public class EmpleosPanel extends JPanel {
    private final PostulacionController postulacionController;
    private final JPanel ofertasGrid;
    private final RoundedTextField searchField;

    public EmpleosPanel(PostulacionController postulacionController) {
        this.postulacionController = postulacionController;

        setBackground(ThemeManager.BG_GENERAL);
        setLayout(new BorderLayout(ThemeManager.CARD_GAP, ThemeManager.CARD_GAP));
        setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN, ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setOpaque(false);
        searchField = new RoundedTextField();
        searchField.setPreferredSize(new Dimension(ThemeManager.SEARCH_W, ThemeManager.INPUT_H));
        RoundedButton btnSearch = new RoundedButton("Buscar Oferta", true);
        btnSearch.setPreferredSize(new Dimension(150, ThemeManager.INPUT_H));
        btnSearch.addActionListener(e -> filtrarOfertas(searchField.getText()));
        
        toolbar.add(searchField);
        toolbar.add(btnSearch);
        add(toolbar, BorderLayout.NORTH);

        JPanel contentContainer = new JPanel(new GridBagLayout());
        contentContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;

        RoundedPanel panelPrincipal = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));
        panelPrincipal.add(new SectionHeader("Ofertas Laborales Disponibles"), BorderLayout.NORTH);

        ofertasGrid = new JPanel(new GridBagLayout());
        ofertasGrid.setOpaque(false);
        panelPrincipal.add(ofertasGrid, BorderLayout.CENTER);
        
        contentContainer.add(panelPrincipal, gbc);

        gbc.gridy++; gbc.weighty = 1.0;
        JPanel filler = new JPanel(); filler.setOpaque(false);
        contentContainer.add(filler, gbc);

        RoundedScrollPane scroll = new RoundedScrollPane(contentContainer);

        add(scroll, BorderLayout.CENTER);
    }

    public void cargarDatos() {
        searchField.setText("");
        filtrarOfertas("");
    }

    private void filtrarOfertas(String query) {
        ofertasGrid.removeAll();
        OfertaLaboral[] ofertas = postulacionController.obtenerTodasLasOfertas();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, ThemeManager.PADDING - 1, 0);

        boolean found = false;
        String busqueda = query == null ? "" : query.toLowerCase().trim();

        if (ofertas != null) {
            for (OfertaLaboral of : ofertas) {
                if (of == null) continue;
                
                boolean match = busqueda.isEmpty() || 
                                of.getTitulo().toLowerCase().contains(busqueda) || 
                                of.getEmpleador().getNombreEmpresa().toLowerCase().contains(busqueda);
                                
                if (match) {
                    found = true;
                    int ofertaId = of.getId();
                    JobCard card = new JobCard(
                        of.getTitulo(),
                        of.getDescripcion(),
                        of.getEmpleador().getNombreEmpresa(),
                        "Postularme",
                        () -> postularse(ofertaId)
                    );
                    ofertasGrid.add(card, gbc);
                    gbc.gridy++;
                }
            }
        }
        
        if (!found) {
            JLabel lblVacio = new JLabel(busqueda.isEmpty() ? "No hay ofertas laborales publicadas." : "No se encontraron ofertas para esa búsqueda.");
            lblVacio.setForeground(Color.GRAY);
            ofertasGrid.add(lblVacio, gbc);
        }

        revalidate();
        repaint();
    }

    private void postularse(int idOferta) {
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;
        
        if (postulacionController.postularUsuario(miId, idOferta)) {
            JOptionPane.showMessageDialog(this, "Te has postulado exitosamente a la oferta.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Ya te postulaste a esta oferta o la oferta ya no existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
