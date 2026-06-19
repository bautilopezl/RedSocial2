package ui.views.profesional;

import controladores.ContactoController;
import controladores.PostulacionController;
import controladores.UsuarioController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import ui.components.JobCard;
import ui.components.RoundedPanel;
import ui.components.SectionHeader;
import ui.components.UserCard;
import ui.state.SessionManager;
import ui.theme.ThemeManager;
import modelo.OfertaLaboral;
import modelo.Usuario;

public class HomeProfesionalPanel extends JPanel {
    
    private final UsuarioController usuarioController;
    private final PostulacionController postulacionController;
    private final ContactoController contactoController;
    
    public HomeProfesionalPanel(UsuarioController usuarioController, PostulacionController postulacionController, ContactoController contactoController) {
        this.usuarioController = usuarioController;
        this.postulacionController = postulacionController;
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

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblBienvenida = new JLabel("\u00a1Bienvenido, " + usuario.getNombre() + "!");
        lblBienvenida.setFont(ThemeManager.F_XXL_BOLD);
        lblBienvenida.setForeground(ThemeManager.TEXT_PRIMARY);
        headerPanel.add(lblBienvenida, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentGrid = new JPanel(new GridBagLayout());
        contentGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 20);

        JPanel ofertasPanel = new JPanel(new BorderLayout());
        ofertasPanel.setOpaque(false);
        ofertasPanel.add(new SectionHeader("Ofertas recomendadas para ti"), BorderLayout.NORTH);
        
        JPanel listaOfertas = new JPanel(new GridBagLayout());
        listaOfertas.setOpaque(false);
        GridBagConstraints gbcOfertas = new GridBagConstraints();
        gbcOfertas.gridx = 0; gbcOfertas.gridy = 0; gbcOfertas.weightx = 1.0; gbcOfertas.fill = GridBagConstraints.HORIZONTAL;
        gbcOfertas.insets = new Insets(0, 0, ThemeManager.PADDING - 1, 0);
        
        OfertaLaboral[] todas = postulacionController.obtenerTodasLasOfertas();
        int mostradas = 0;
        if (todas != null) {
            for (OfertaLaboral of : todas) {
                if (of == null || !of.isActiva()) continue;
                int ofertaId = of.getId();
                JobCard card = new JobCard(
                    of.getTitulo(),
                    of.getDescripcion(),
                    of.getEmpleador().getNombreEmpresa(),
                    "Postularme",
                    () -> postularse(ofertaId)
                );
                listaOfertas.add(card, gbcOfertas);
                gbcOfertas.gridy++;
                mostradas++;
                if (mostradas >= 3) break;
            }
        }
        
        if (mostradas == 0) {
            JLabel lblSinOfertas = new JLabel("No hay ofertas recomendadas en este momento.");
            lblSinOfertas.setForeground(Color.GRAY);
            listaOfertas.add(lblSinOfertas, gbcOfertas);
        }
        
        ofertasPanel.add(listaOfertas, BorderLayout.CENTER);
        contentGrid.add(ofertasPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel derechaPanel = new JPanel(new GridBagLayout());
        derechaPanel.setOpaque(false);
        GridBagConstraints gbcDer = new GridBagConstraints();
        gbcDer.gridx = 0; gbcDer.gridy = 0; gbcDer.weightx = 1.0; gbcDer.fill = GridBagConstraints.HORIZONTAL;
        gbcDer.insets = new Insets(0, 0, 20, 0);

        RoundedPanel miniPerfil = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        miniPerfil.setShadowEnabled(true);
        miniPerfil.setLayout(new BorderLayout(5, 5));
        miniPerfil.setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));
        JLabel lblNombre = new JLabel(usuario.getNombre());
        lblNombre.setFont(ThemeManager.F_HEAD_BOLD);
        JLabel lblProf = new JLabel(usuario.getProfesion());
        lblProf.setForeground(Color.GRAY);

        miniPerfil.add(lblNombre, BorderLayout.NORTH);
        miniPerfil.add(lblProf, BorderLayout.CENTER);
        
        derechaPanel.add(miniPerfil, gbcDer);
        
        gbcDer.gridy++;
        derechaPanel.add(new SectionHeader("Personas que quiz\u00e1s conozcas"), gbcDer);
        
        int[] sugeridos = contactoController.sugerirContactos(idUsuario);
        int sugerenciasMostradas = 0;
        if (sugeridos != null) {
            for (int idSugerido : sugeridos) {
                if (idSugerido < 0) continue;
                Usuario uSugerido = usuarioController.buscarUsuario(idSugerido);
                if (uSugerido == null) continue;
                gbcDer.gridy++;
                derechaPanel.add(new UserCard(uSugerido, "Conectar", () -> enviarSolicitud(idSugerido)), gbcDer);
                sugerenciasMostradas++;
                if (sugerenciasMostradas >= 5) break;
            }
        }
        
        if (sugerenciasMostradas == 0) {
            gbcDer.gridy++;
            JLabel noSugerencias = new JLabel("No hay sugerencias en este momento.");
            noSugerencias.setForeground(Color.GRAY);
            derechaPanel.add(noSugerencias, gbcDer);
        }

        contentGrid.add(derechaPanel, gbc);
        add(contentGrid, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    private void postularse(int idOferta) {
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;
        if (postulacionController.postularUsuario(miId, idOferta)) {
            JOptionPane.showMessageDialog(this, "Te has postulado exitosamente a la oferta.", "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Ya te postulaste a esta oferta o la oferta ya no existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enviarSolicitud(int idDestino) {
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0 || idDestino < 0) return;
        if (contactoController.sonContactos(miId, idDestino)) {
            JOptionPane.showMessageDialog(this, "Ya son contactos.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        contactoController.enviarSolicitudContacto(miId, idDestino);
        JOptionPane.showMessageDialog(this, "Solicitud de contacto enviada.", "\u00c9xito", JOptionPane.INFORMATION_MESSAGE);
    }
}
