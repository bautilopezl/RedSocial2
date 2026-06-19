package ui.views.profesional;

import controladores.ContactoController;
import controladores.HabilidadController;
import controladores.UsuarioController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import modelo.Usuario;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.RoundedScrollPane;
import ui.components.RoundedTextField;
import ui.components.SectionHeader;
import ui.components.UserCard;
import ui.state.SessionManager;
import ui.theme.ThemeManager;

public class MiRedPanel extends JPanel {
    private static Set<String> pendingSugerenciasSkills = null;

    public static void setPendingSugerencias(Set<String> skills) {
        pendingSugerenciasSkills = new HashSet<>(skills);
    }

    private java.util.List<Usuario> sugerenciasPool;
    private final UsuarioController usuarioController;
    private final ContactoController contactoController;
    private final HabilidadController habilidadController;

    private final JPanel solicitudesGrid;
    private final JPanel contactosGrid;
    private final JPanel sugerenciasGrid;
    private final RoundedTextField searchField;
    private final JPanel solicitudesSection;
    private final JPanel resultadosGrid;

    public MiRedPanel(UsuarioController usuarioController, ContactoController contactoController, HabilidadController habilidadController) {
        this.usuarioController = usuarioController;
        this.contactoController = contactoController;
        this.habilidadController = habilidadController;

        setBackground(ThemeManager.BG_GENERAL);
        setLayout(new BorderLayout(ThemeManager.CARD_GAP, ThemeManager.CARD_GAP));
        setBorder(new EmptyBorder(ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN, ThemeManager.BORDER_W, ThemeManager.OUTER_MARGIN));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setOpaque(false);

        searchField = new RoundedTextField();
        searchField.setPreferredSize(new Dimension(350, ThemeManager.INPUT_H));
        RoundedButton btnSearch = new RoundedButton("Buscar", true);
        btnSearch.setPreferredSize(new Dimension(120, ThemeManager.INPUT_H));
        btnSearch.addActionListener(e -> buscarUsuarios(searchField.getText()));

        toolbar.add(new JLabel("Buscar por nombre, email o habilidad:"));
        toolbar.add(searchField);
        toolbar.add(btnSearch);

        add(toolbar, BorderLayout.NORTH);

        JPanel contentContainer = new JPanel(new GridBagLayout());
        contentContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, ThemeManager.CARD_GAP, 0);

        resultadosGrid = new JPanel(new GridBagLayout());
        resultadosGrid.setOpaque(false);
        contentContainer.add(resultadosGrid, gbc);
        gbc.gridy++;

        solicitudesSection = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        solicitudesSection.setLayout(new BorderLayout());
        solicitudesSection.setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));
        solicitudesSection.add(new SectionHeader("Solicitudes de Contacto"), BorderLayout.NORTH);

        solicitudesGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));
        solicitudesGrid.setOpaque(false);
        solicitudesSection.add(solicitudesGrid, BorderLayout.CENTER);
        contentContainer.add(solicitudesSection, gbc);

        gbc.gridy++;
        RoundedPanel panelContactos = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        panelContactos.setLayout(new BorderLayout());
        panelContactos.setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));
        panelContactos.add(new SectionHeader("Mis Contactos"), BorderLayout.NORTH);

        contactosGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));
        contactosGrid.setOpaque(false);
        panelContactos.add(contactosGrid, BorderLayout.CENTER);
        contentContainer.add(panelContactos, gbc);

        gbc.gridy++;
        RoundedPanel panelSugerencias = new RoundedPanel(ThemeManager.RADIUS_PANEL, ThemeManager.PANEL);
        panelSugerencias.setLayout(new BorderLayout());
        panelSugerencias.setBorder(new EmptyBorder(ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));
        panelSugerencias.add(new SectionHeader("Sugerencias de Contactos (BFS)"), BorderLayout.NORTH);

        sugerenciasGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, ThemeManager.PADDING - 1, ThemeManager.PADDING - 1));
        sugerenciasGrid.setOpaque(false);
        panelSugerencias.add(sugerenciasGrid, BorderLayout.CENTER);
        contentContainer.add(panelSugerencias, gbc);

        gbc.gridy++; gbc.weighty = 1.0;
        JPanel filler = new JPanel();
        filler.setOpaque(false);
        contentContainer.add(filler, gbc);

        RoundedScrollPane scroll = new RoundedScrollPane(contentContainer);

        add(scroll, BorderLayout.CENTER);
    }

    public void cargarDatos() {
        if (pendingSugerenciasSkills != null) {
            Set<String> skills = pendingSugerenciasSkills;
            pendingSugerenciasSkills = null;
            mostrarSugerenciasPorSkills(skills);
        } else {
            searchField.setText("");
            resultadosGrid.removeAll();
        }
        cargarSolicitudes();
        cargarContactos();
        cargarSugerenciasBFS();
        revalidate();
        repaint();
    }

    private void cargarSolicitudes() {
        solicitudesGrid.removeAll();
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;

        int[] solicitudes = contactoController.obtenerSolicitudesContacto(miId);
        if (solicitudes == null || solicitudes.length == 0) {
            solicitudesSection.setVisible(false);
            return;
        }
        solicitudesSection.setVisible(true);

        for (int sid : solicitudes) {
            if (sid < 0) continue;
            Usuario u = usuarioController.buscarUsuario(sid);
            if (u == null) continue;

            JPanel cardWrapper = new JPanel(new BorderLayout());
            cardWrapper.setOpaque(false);

            UserCard card = new UserCard(u, null, null);
            cardWrapper.add(card, BorderLayout.CENTER);

            JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            acciones.setOpaque(false);

            RoundedButton btnAceptar = new RoundedButton("Aceptar", true);
            btnAceptar.setPreferredSize(new Dimension(100, 30));
            int finalSid = sid;
            btnAceptar.addActionListener(e -> aceptarSolicitud(finalSid));

            RoundedButton btnRechazar = new RoundedButton("Rechazar", false);
            btnRechazar.setPreferredSize(new Dimension(100, 30));
            btnRechazar.addActionListener(e -> rechazarSolicitud(finalSid));

            acciones.add(btnAceptar);
            acciones.add(btnRechazar);
            cardWrapper.add(acciones, BorderLayout.SOUTH);

            solicitudesGrid.add(cardWrapper);
        }
    }

    private void cargarContactos() {
        contactosGrid.removeAll();
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;

        int[] contactosIds = contactoController.obtenerContactosDirectos(miId);
        if (contactosIds == null || contactosIds.length == 0) {
            JLabel lbl = new JLabel("Aún no tienes contactos.");
            lbl.setForeground(Color.GRAY);
            contactosGrid.add(lbl);
            return;
        }

        for (int cid : contactosIds) {
            if (cid < 0) continue;
            Usuario u = usuarioController.buscarUsuario(cid);
            if (u == null) continue;

            JPanel cardWrapper = new JPanel(new BorderLayout());
            cardWrapper.setOpaque(false);

            UserCard card = new UserCard(u, null, null);
            cardWrapper.add(card, BorderLayout.CENTER);

            RoundedButton btnEliminar = new RoundedButton("Eliminar", false);
            btnEliminar.setPreferredSize(new Dimension(100, 30));
            int finalCid = cid;
            btnEliminar.addActionListener(e -> eliminarContacto(finalCid));

            JPanel accionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            accionPanel.setOpaque(false);
            accionPanel.add(btnEliminar);
            cardWrapper.add(accionPanel, BorderLayout.SOUTH);

            contactosGrid.add(cardWrapper);
        }
    }

    private void cargarSugerenciasBFS() {
        sugerenciasGrid.removeAll();
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;

        Usuario[] todos = usuarioController.obtenerTodosLosUsuarios();
        if (todos == null || todos.length == 0) {
            JLabel lbl = new JLabel("No hay usuarios en la red.");
            lbl.setForeground(Color.GRAY);
            sugerenciasGrid.add(lbl);
            return;
        }

        int[] contactosIds = contactoController.obtenerContactosDirectos(miId);

        java.util.List<UsuarioConGrado> candidatos = new java.util.ArrayList<>();

        for (Usuario u : todos) {
            if (u == null || u.getId() == miId) continue;
            if (contiene(contactosIds, u.getId())) continue;

            int grado = contactoController.verGradoSeparacion(miId, u.getId());
            if (grado > 0 && grado <= 3) {
                candidatos.add(new UsuarioConGrado(u, grado));
            }
        }

        candidatos.sort((a, b) -> Integer.compare(a.grado, b.grado));

        if (candidatos.isEmpty()) {
            JLabel lbl = new JLabel("No hay sugerencias disponibles en este momento.");
            lbl.setForeground(Color.GRAY);
            sugerenciasGrid.add(lbl);
            return;
        }

        for (UsuarioConGrado ug : candidatos) {
            JPanel cardWrapper = new JPanel(new BorderLayout());
            cardWrapper.setOpaque(false);

            UserCard card = new UserCard(ug.usuario, null, null);
            cardWrapper.add(card, BorderLayout.CENTER);

            JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            infoPanel.setOpaque(false);

            JLabel lblGrado = new JLabel("Grado " + ug.grado);
            lblGrado.setFont(ThemeManager.F_SMALL_BOLD);
            lblGrado.setForeground(ThemeManager.PRIMARY);
            infoPanel.add(lblGrado);

            RoundedButton btnConectar = new RoundedButton("Conectar", true);
            btnConectar.setPreferredSize(new Dimension(100, 30));
            btnConectar.addActionListener(e -> enviarSolicitud(ug.usuario.getId()));

            infoPanel.add(btnConectar);
            cardWrapper.add(infoPanel, BorderLayout.SOUTH);

            sugerenciasGrid.add(cardWrapper);
        }
    }

    private void mostrarSugerenciasPorSkills(Set<String> skills) {
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;

        int[] contactosIds = contactoController.obtenerContactosDirectos(miId);
        Set<Integer> contactosSet = new HashSet<>();
        if (contactosIds != null) {
            for (int c : contactosIds) contactosSet.add(c);
        }

        sugerenciasPool = new java.util.ArrayList<>();
        Usuario[] todos = usuarioController.obtenerTodosLosUsuarios();
        if (todos != null) {
            for (Usuario u : todos) {
                if (u == null || u.getId() == miId || contactosSet.contains(u.getId())) continue;
                String[] userSkills = u.obtenerHabilidades();
                if (userSkills == null) continue;
                for (String s : userSkills) {
                    if (s != null && skills.contains(s.trim())) {
                        sugerenciasPool.add(u);
                        break;
                    }
                }
            }
        }

        sugerenciasPool.sort((a, b) -> Integer.compare(
            contactoController.verGradoSeparacion(miId, a.getId()),
            contactoController.verGradoSeparacion(miId, b.getId())));

        renderSiguientes3();
    }

    private void renderSiguientes3() {
        resultadosGrid.removeAll();
        int miId = SessionManager.getInstance().getCurrentUserId();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 6, 0);

        if (sugerenciasPool == null || sugerenciasPool.isEmpty()) {
            JLabel lbl = new JLabel("No hay m\u00e1s contactos sugeridos.");
            lbl.setForeground(ThemeManager.TEXT_SECONDARY);
            resultadosGrid.add(lbl, gbc);
            revalidate();
            repaint();
            return;
        }

        JLabel lbl = new JLabel("Contactos sugeridos por tus habilidades:");
        lbl.setFont(ThemeManager.F_HEAD_BOLD);
        lbl.setForeground(ThemeManager.PRIMARY);
        resultadosGrid.add(lbl, gbc);

        int mostrar = Math.min(3, sugerenciasPool.size());
        for (int i = 0; i < mostrar; i++) {
            Usuario u = sugerenciasPool.remove(0);
            gbc.gridy++;
            resultadosGrid.add(crearCardSugerencia(miId, u), gbc);
        }

        revalidate();
        repaint();
    }

    private JPanel crearCardSugerencia(int miId, Usuario u) {
        RoundedPanel card = new RoundedPanel(ThemeManager.RADIUS_SMALL, ThemeManager.CARD_ALT_BG);
        card.setShadowEnabled(true);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(10, 12, 10, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNombre = new JLabel(u.getNombre());
        lblNombre.setFont(ThemeManager.F_BODY_BOLD);
        lblNombre.setForeground(ThemeManager.TEXT_PRIMARY);
        card.add(lblNombre, gbc);

        gbc.gridy++;
        JLabel lblInfo = new JLabel(u.getEmail() + " \u2014 " + u.getProfesion());
        lblInfo.setFont(ThemeManager.F_SMALL_PLAIN);
        lblInfo.setForeground(ThemeManager.TEXT_SECONDARY);
        card.add(lblInfo, gbc);

        int grado = contactoController.verGradoSeparacion(miId, u.getId());
        if (grado > 0) {
            gbc.gridy++;
            gbc.insets = new Insets(4, 0, 0, 0);
            JLabel lblGrado = new JLabel("Grado " + grado);
            lblGrado.setFont(ThemeManager.F_SMALL_BOLD);
            lblGrado.setForeground(ThemeManager.PRIMARY);
            card.add(lblGrado, gbc);
        }

        gbc.gridy++;
        gbc.insets = new Insets(8, 0, 0, 0);
        RoundedButton btnConectar = new RoundedButton("Conectar", true);
        btnConectar.setPreferredSize(new Dimension(100, 30));
        btnConectar.addActionListener(e -> {
            enviarSolicitud(u.getId());
            renderSiguientes3();
        });
        card.add(btnConectar, gbc);

        return card;
    }

    private void buscarUsuarios(String query) {
        if (query == null || query.trim().isEmpty()) return;
        String busqueda = query.toLowerCase().trim();
        int miId = SessionManager.getInstance().getCurrentUserId();

        java.util.List<Usuario> resultados = new java.util.ArrayList<>();

        Usuario[] todos = usuarioController.obtenerTodosLosUsuarios();
        if (todos != null) {
            for (Usuario u : todos) {
                if (u == null || u.getId() == miId) continue;
                if (u.getNombre().toLowerCase().contains(busqueda)
                    || u.getEmail().toLowerCase().contains(busqueda)) {
                    if (!resultados.contains(u)) resultados.add(u);
                }
            }
        }

        Usuario[] porHabilidad = habilidadController.buscarUsuariosPorHabilidad(query.trim());
        if (porHabilidad != null) {
            for (Usuario u : porHabilidad) {
                if (u != null && u.getId() != miId && !resultados.contains(u)) {
                    resultados.add(u);
                }
            }
        }

        mostrarResultados(resultados, query.trim());
    }

    private void mostrarResultados(java.util.List<Usuario> resultados, String query) {
        resultadosGrid.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 6, 0);

        if (resultados.isEmpty()) {
            JLabel lbl = new JLabel("No se encontraron usuarios para \"" + query + "\".");
            lbl.setForeground(ThemeManager.TEXT_SECONDARY);
            resultadosGrid.add(lbl, gbc);
        } else {
            JLabel lbl = new JLabel(resultados.size() + " resultado(s) para \"" + query + "\":");
            lbl.setFont(ThemeManager.F_BODY_BOLD);
            resultadosGrid.add(lbl, gbc);

            for (Usuario u : resultados) {
                gbc.gridy++;
                resultadosGrid.add(crearResultadoCard(u), gbc);
            }
        }

        revalidate();
        repaint();
    }

    private JPanel crearResultadoCard(Usuario u) {
        int miId = SessionManager.getInstance().getCurrentUserId();
        boolean sonContactos = contactoController.sonContactos(miId, u.getId());
        int grado = contactoController.verGradoSeparacion(miId, u.getId());

        RoundedPanel card = new RoundedPanel(ThemeManager.RADIUS_SMALL, ThemeManager.CARD_ALT_BG);
        card.setShadowEnabled(true);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(10, 12, 10, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNombre = new JLabel(u.getNombre());
        lblNombre.setFont(ThemeManager.F_BODY_BOLD);
        lblNombre.setForeground(ThemeManager.TEXT_PRIMARY);
        card.add(lblNombre, gbc);

        gbc.gridy++;
        JLabel lblInfo = new JLabel(u.getEmail() + " \u2014 " + u.getProfesion());
        lblInfo.setFont(ThemeManager.F_SMALL_PLAIN);
        lblInfo.setForeground(ThemeManager.TEXT_SECONDARY);
        card.add(lblInfo, gbc);

        if (u.cantidadHabilidades() > 0) {
            gbc.gridy++;
            gbc.insets = new Insets(4, 0, 0, 0);
            JPanel skillsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
            skillsRow.setOpaque(false);
            String[] habs = u.obtenerHabilidades();
            for (int i = 0; i < Math.min(habs.length, 4); i++) {
                skillsRow.add(new ui.components.SkillBadge(habs[i]));
            }
            card.add(skillsRow, gbc);
        }

        if (grado > 0 && !sonContactos) {
            gbc.gridy++;
            gbc.insets = new Insets(4, 0, 0, 0);
            JLabel lblGrado = new JLabel("Grado " + grado);
            lblGrado.setFont(ThemeManager.F_SMALL_BOLD);
            lblGrado.setForeground(ThemeManager.PRIMARY);
            card.add(lblGrado, gbc);
        }

        if (!sonContactos) {
            gbc.gridy++;
            gbc.insets = new Insets(8, 0, 0, 0);
            RoundedButton btnConectar = new RoundedButton("Conectar", true);
            btnConectar.setPreferredSize(new Dimension(100, 30));
            btnConectar.addActionListener(e -> {
                enviarSolicitud(u.getId());
                buscarUsuarios(searchField.getText());
            });
            card.add(btnConectar, gbc);
        } else {
            gbc.gridy++;
            gbc.insets = new Insets(4, 0, 0, 0);
            JLabel lblContacto = new JLabel("Ya son contactos");
            lblContacto.setFont(ThemeManager.F_SMALL_BOLD);
            lblContacto.setForeground(ThemeManager.SUCCESS);
            card.add(lblContacto, gbc);
        }

        return card;
    }

    private void enviarSolicitud(int idDestino) {
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0 || idDestino < 0) return;

        Usuario dest = usuarioController.buscarUsuario(idDestino);
        String nombreDestino = dest != null ? dest.getNombre() : String.valueOf(idDestino);

        if (contactoController.enviarSolicitudContacto(miId, idDestino)) {
            JOptionPane.showMessageDialog(this,
                "Solicitud de contacto enviada a " + nombreDestino + ".",
                "Solicitud Enviada", JOptionPane.INFORMATION_MESSAGE);
        } else if (contactoController.sonContactos(miId, idDestino)) {
            JOptionPane.showMessageDialog(this,
                "Ya son contactos.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Ya enviaste una solicitud o no se pudo procesar.",
                "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void aceptarSolicitud(int idSolicitante) {
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;

        if (contactoController.aceptarSolicitudContacto(idSolicitante, miId)) {
            JOptionPane.showMessageDialog(this,
                "¡Ahora son contactos!", "Conexión Aceptada", JOptionPane.INFORMATION_MESSAGE);
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al aceptar la solicitud.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rechazarSolicitud(int idSolicitante) {
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;

        if (contactoController.rechazarSolicitudContacto(idSolicitante, miId)) {
            JOptionPane.showMessageDialog(this,
                "Solicitud rechazada.", "Solicitud Rechazada", JOptionPane.INFORMATION_MESSAGE);
            cargarDatos();
        }
    }

    private void eliminarContacto(int idContacto) {
        int miId = SessionManager.getInstance().getCurrentUserId();
        if (miId < 0) return;

        Usuario u = usuarioController.buscarUsuario(idContacto);
        String nombre = u != null ? u.getNombre() : String.valueOf(idContacto);

        int r = JOptionPane.showConfirmDialog(this,
            "¿Eliminar a " + nombre + " de tus contactos?",
            "Eliminar Contacto", JOptionPane.YES_NO_OPTION);

        if (r == JOptionPane.YES_OPTION) {
            if (contactoController.eliminarContacto(miId, idContacto)) {
                JOptionPane.showMessageDialog(this,
                    "Contacto eliminado.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al eliminar contacto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean contiene(int[] arreglo, int valor) {
        if (arreglo == null) return false;
        for (int s : arreglo) {
            if (s == valor) return true;
        }
        return false;
    }

    private static class UsuarioConGrado {
        final Usuario usuario;
        final int grado;

        UsuarioConGrado(Usuario usuario, int grado) {
            this.usuario = usuario;
            this.grado = grado;
        }
    }
}
