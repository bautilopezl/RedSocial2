package ui.views.profesional;

import controladores.PostulacionController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import modelo.Postulacion;
import ui.components.PostulationCard;
import ui.components.RoundedPanel;
import ui.components.RoundedScrollPane;
import ui.components.SectionHeader;
import ui.state.SessionManager;
import ui.theme.ThemeManager;

public class PostulacionesProfesionalPanel extends JPanel {
    private final PostulacionController postulacionController;
    private final JPanel postulacionesGrid;

    public PostulacionesProfesionalPanel(PostulacionController postulacionController) {
        this.postulacionController = postulacionController;

        setBackground(ThemeManager.BG_GENERAL);
        setLayout(new BorderLayout(ThemeManager.CARD_GAP, ThemeManager.CARD_GAP));
        setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN, ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN));

        JPanel contentContainer = new JPanel(new GridBagLayout());
        contentContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;

        RoundedPanel panelPrincipal = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));
        panelPrincipal.add(new SectionHeader("Mis Postulaciones"), BorderLayout.NORTH);

        postulacionesGrid = new JPanel(new GridBagLayout());
        postulacionesGrid.setOpaque(false);
        panelPrincipal.add(postulacionesGrid, BorderLayout.CENTER);

        contentContainer.add(panelPrincipal, gbc);

        gbc.gridy++; gbc.weighty = 1.0;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        contentContainer.add(filler, gbc);

        RoundedScrollPane scroll = new RoundedScrollPane(contentContainer);

        add(scroll, BorderLayout.CENTER);
    }

    public void cargarDatos() {
        postulacionesGrid.removeAll();
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;

        Postulacion[] postulaciones = postulacionController.obtenerPostulacionesDeUsuario(miId);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, ThemeManager.PADDING - 1, 0);

        if (postulaciones == null || postulaciones.length == 0) {
            JLabel lblVacio = new JLabel("Aún no te has postulado a ninguna oferta.");
            lblVacio.setForeground(Color.GRAY);
            postulacionesGrid.add(lblVacio, gbc);
        } else {
            for (Postulacion p : postulaciones) {
                if (p == null) continue;
                postulacionesGrid.add(new PostulationCard(p), gbc);
                gbc.gridy++;
            }
        }

        revalidate();
        repaint();
    }
}
