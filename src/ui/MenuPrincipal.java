package ui;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Dimension;

import modelo.Postulacion;
import modelo.Usuario;
import modelo.Empleador;
import controladores.ContactoController;
import controladores.EmpleadorController;
import controladores.HabilidadController;
import controladores.PostulacionController;
import controladores.UsuarioController;

public class MenuPrincipal extends JFrame {
    private final UsuarioController usuarioController;
    private final EmpleadorController empleadorController;
    private final PostulacionController postulacionController;
    private final ContactoController contactoController;
    private final HabilidadController habilidadController;

    public MenuPrincipal(UsuarioController usuarioController,
                         EmpleadorController empleadorController,
                         PostulacionController postulacionController,
                         ContactoController contactoController,
                         HabilidadController habilidadController) {
        super("Red Social Profesional");
        this.usuarioController = usuarioController;
        this.empleadorController = empleadorController;
        this.postulacionController = postulacionController;
        this.contactoController = contactoController;
        this.habilidadController = habilidadController;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        pack();
        setLocationRelativeTo(null);
    }

    private void initUI() {
        JPanel main = new JPanel(new GridLayout(3, 1, 8, 8));
        JPanel header = new JPanel();
        header.add(new JLabel("Bienvenido a la Red Social Profesional"));
        main.add(header);

        JPanel btns = new JPanel();
        JButton btnEmpleador = new JButton("Soy Empleador");
        btnEmpleador.addActionListener(e -> flujoEmpleador());
        btns.add(btnEmpleador);

        JButton btnUsuario = new JButton("Soy Usuario / Postulante");
        btnUsuario.addActionListener(e -> flujoUsuario());
        btns.add(btnUsuario);

        main.add(btns);
        main.setPreferredSize(new Dimension(500, 300));
        setContentPane(main);
        revalidate();
    }

    // ── Empleador ──────────────────────────────────────────────

    private void flujoEmpleador() {
        JPanel p = new JPanel(new GridLayout(4, 2, 4, 4));
        JTextField nombre = new JTextField();
        JTextField rubro = new JTextField();
        JTextField descripcionEmp = new JTextField();
        JTextField email = new JTextField();
        p.add(new JLabel("Nombre empresa:")); p.add(nombre);
        p.add(new JLabel("Rubro:")); p.add(rubro);
        p.add(new JLabel("Descripcion:")); p.add(descripcionEmp);
        p.add(new JLabel("Email:")); p.add(email);
        int res = JOptionPane.showConfirmDialog(this, p, "Registro Empleador", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            if (empleadorController.registrarEmpleador(nombre.getText(), rubro.getText(), descripcionEmp.getText(), email.getText())) {
                Empleador emp = empleadorController.buscarPorEmail(email.getText());
                if (emp != null) {
                    JOptionPane.showMessageDialog(this, "Empleador registrado correctamente. Su ID es: " + emp.getId());
                    menuDashboardEmpleador(emp.getId());
                } else {
                    JOptionPane.showMessageDialog(this, "Error al obtener el empleador registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar o ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void menuDashboardEmpleador(int idEmpleador) {
        JPanel main = new JPanel(new GridLayout(0, 1, 8, 8));
        main.add(new JLabel("Panel de Empleador: " + idEmpleador));
        JPanel btns = new JPanel(new GridLayout(0, 2, 6, 6));
        JButton btnRegistrarOferta = new JButton("Publicar oferta");
        btnRegistrarOferta.addActionListener(e -> registrarOfertaLaboral(idEmpleador));
        btns.add(btnRegistrarOferta);

        JButton btnProcesar = new JButton("Procesar postulaciones");
        btnProcesar.addActionListener(e -> procesarPostulacion());
        btns.add(btnProcesar);

        JButton salir = new JButton("Volver al inicio");
        salir.addActionListener(e -> initUI());
        btns.add(salir);

        main.add(btns);
        setContentPane(main);
        revalidate();
    }

    // ── Usuario ────────────────────────────────────────────────

    private void flujoUsuario() {
        JPanel p = new JPanel(new GridLayout(5, 2, 4, 4));
        JTextField nombre = new JTextField();
        JTextField email = new JTextField();
        JTextField profesion = new JTextField();
        JTextField descripcion = new JTextField();
        JTextField password = new JTextField();
        p.add(new JLabel("Nombre:")); p.add(nombre);
        p.add(new JLabel("Email:")); p.add(email);
        p.add(new JLabel("Profesion:")); p.add(profesion);
        p.add(new JLabel("Descripcion:")); p.add(descripcion);
        p.add(new JLabel("Password:")); p.add(password);
        int res = JOptionPane.showConfirmDialog(this, p, "Registro Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            if (usuarioController.buscarPorEmail(email.getText()) != null) {
                Usuario u = usuarioController.buscarPorEmail(email.getText());
                JOptionPane.showMessageDialog(this, "Ya existe. Ingresando...", "Info", JOptionPane.INFORMATION_MESSAGE);
                menuDashboardUsuario(u.getId());
                return;
            }
            if (usuarioController.registrarUsuario(nombre.getText(), email.getText(), profesion.getText(), descripcion.getText())) {
                Usuario u = usuarioController.buscarPorEmail(email.getText());
                if (u != null) {
                    JOptionPane.showMessageDialog(this, "Registrado correctamente. Su ID es: " + u.getId());
                    menuDashboardUsuario(u.getId());
                } else {
                    JOptionPane.showMessageDialog(this, "Error al obtener el usuario registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar o datos invalidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void menuDashboardUsuario(int idUsuario) {
        JPanel main = new JPanel(new GridLayout(0, 1, 8, 8));
        main.add(new JLabel("Panel de Usuario: " + idUsuario));
        JPanel btns = new JPanel(new GridLayout(0, 2, 6, 6));

        JButton btnVerOfertas = new JButton("Ver ofertas y postularse");
        btnVerOfertas.addActionListener(e -> verOfertasYPostularse(idUsuario));
        btns.add(btnVerOfertas);

        JButton btnContactos = new JButton("Mis Contactos");
        btnContactos.addActionListener(e -> agregarContacto());
        btns.add(btnContactos);

        JButton btnHabilidades = new JButton("Mis Habilidades");
        btnHabilidades.addActionListener(e -> administrarHabilidades(idUsuario));
        btns.add(btnHabilidades);

        JButton salir = new JButton("Volver al inicio");
        salir.addActionListener(e -> initUI());
        btns.add(salir);

        main.add(btns);
        setContentPane(main);
        revalidate();
    }

    private void verOfertasYPostularse(int idUsuario) {
        String input = JOptionPane.showInputDialog(this, "Ingrese el ID de la oferta a la que se quiere postular:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                int idOferta = Integer.parseInt(input.trim());
                if (postulacionController.postularUsuario(idUsuario, idOferta)) {
                    JOptionPane.showMessageDialog(this, "Postulacion registrada exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error: no se pudo postular (oferta inexistente o error interno).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID de oferta invalido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registrarOfertaLaboral(int idEmpleador) {
        JPanel p = new JPanel(new GridLayout(2, 2, 4, 4));
        JTextField titulo = new JTextField();
        JTextField descripcion = new JTextField();
        p.add(new JLabel("Titulo:")); p.add(titulo);
        p.add(new JLabel("Descripcion:")); p.add(descripcion);
        int res = JOptionPane.showConfirmDialog(this, p, "Registrar oferta laboral", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            if (postulacionController.registrarOfertaLaboral(titulo.getText(), descripcion.getText(), idEmpleador)) {
                JOptionPane.showMessageDialog(this, "Oferta registrada correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la oferta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Acciones sueltas ───────────────────────────────────────

    private void agregarContacto() {
        JPanel p = new JPanel(new GridLayout(2, 2, 4, 4));
        JTextField id1 = new JTextField();
        JTextField id2 = new JTextField();
        p.add(new JLabel("ID del primer usuario:")); p.add(id1);
        p.add(new JLabel("ID del segundo usuario:")); p.add(id2);
        int res = JOptionPane.showConfirmDialog(this, p, "Agregar contacto", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int idUsuario1 = Integer.parseInt(id1.getText().trim());
                int idUsuario2 = Integer.parseInt(id2.getText().trim());
                if (contactoController.agregarContacto(idUsuario1, idUsuario2)) {
                    JOptionPane.showMessageDialog(this, "Contacto agregado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo crear la conexion.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "IDs invalidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void verGradoSeparacion() {
        JPanel p = new JPanel(new GridLayout(2, 2, 4, 4));
        JTextField id1 = new JTextField();
        JTextField id2 = new JTextField();
        p.add(new JLabel("ID origen:")); p.add(id1);
        p.add(new JLabel("ID destino:")); p.add(id2);
        int res = JOptionPane.showConfirmDialog(this, p, "Ver grado de separacion", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int idUsuario1 = Integer.parseInt(id1.getText().trim());
                int idUsuario2 = Integer.parseInt(id2.getText().trim());
                int grado = contactoController.verGradoSeparacion(idUsuario1, idUsuario2);
                if (grado < 0) JOptionPane.showMessageDialog(this, "No hay conexion entre esos usuarios.", "Info", JOptionPane.INFORMATION_MESSAGE);
                else JOptionPane.showMessageDialog(this, "Grado de separacion: " + grado);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "IDs invalidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void procesarPostulacion() {
        Postulacion postulacion = postulacionController.procesarPostulacion();
        if (postulacion == null) {
            JOptionPane.showMessageDialog(this, "No hay postulaciones en cola.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Postulacion procesada: " + postulacion);
        }
    }

    private void administrarHabilidades(int idUsuario) {
        String[] opciones = {"Agregar categoria", "Buscar categoria", "Mostrar jerarquia", "Asociar habilidad a usuario", "Buscar usuarios por habilidad", "Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(this, "Administracion de habilidades", "Habilidades",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
            if (sel == 0) {
                String padre = JOptionPane.showInputDialog(this, "Categoria padre (vacio si es la raiz):");
                if (padre == null) continue;
                String nueva = JOptionPane.showInputDialog(this, "Nueva categoria:");
                if (nueva == null) continue;
                if (habilidadController.agregarCategoria(padre, nueva)) JOptionPane.showMessageDialog(this, "Categoria agregada.");
                else JOptionPane.showMessageDialog(this, "No se pudo agregar la categoria.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (sel == 1) {
                String categoria = JOptionPane.showInputDialog(this, "Categoria a buscar:");
                if (categoria == null) continue;
                if (habilidadController.buscarCategoria(categoria)) JOptionPane.showMessageDialog(this, "La categoria existe.");
                else JOptionPane.showMessageDialog(this, "La categoria no existe.");
            } else if (sel == 2) {
                String texto = habilidadController.obtenerJerarquiaTexto();
                javax.swing.JTextArea area = new javax.swing.JTextArea(texto);
                area.setEditable(false);
                area.setRows(15);
                area.setColumns(40);
                javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(area);
                JOptionPane.showMessageDialog(this, scroll, "Jerarquia de habilidades", JOptionPane.INFORMATION_MESSAGE);
            } else if (sel == 3) {
                String input = JOptionPane.showInputDialog(this, "ID del usuario (actual: " + idUsuario + "):");
                if (input == null) continue;
                String habilidad = JOptionPane.showInputDialog(this, "Habilidad/categoria:");
                if (habilidad == null) continue;
                try {
                    int uid = Integer.parseInt(input.trim());
                    if (habilidadController.asociarHabilidad(uid, habilidad)) JOptionPane.showMessageDialog(this, "Habilidad asociada correctamente.");
                    else JOptionPane.showMessageDialog(this, "No se pudo asociar la habilidad.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID invalido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (sel == 4) {
                String habilidad = JOptionPane.showInputDialog(this, "Habilidad a buscar:");
                if (habilidad == null) continue;
                Usuario[] usuarios = habilidadController.buscarUsuariosPorHabilidad(habilidad);
                if (usuarios.length == 0) JOptionPane.showMessageDialog(this, "No se encontraron usuarios con esa habilidad.");
                else {
                    StringBuilder sb = new StringBuilder();
                    for (Usuario u : usuarios) sb.append(u).append("\n");
                    JOptionPane.showMessageDialog(this, sb.toString(), "Usuarios encontrados", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                break;
            }
        }
    }

    // ── Admin Empleadores ──────────────────────────────────────

    private void administrarEmpleadores() {
        String[] opciones = {"Registrar empleador", "Buscar empleador", "Modificar empleador", "Eliminar empleador", "Listar empleadores", "Volver"};
        while (true) {
            int sel = JOptionPane.showOptionDialog(this, "Administracion de empleadores", "Empleadores",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
            if (sel == 0) {
                JPanel p = new JPanel(new GridLayout(3, 2, 4, 4));
                JTextField nombre = new JTextField();
                JTextField rubro = new JTextField();
                JTextField descripcionEmp = new JTextField();
                p.add(new JLabel("Nombre empresa:")); p.add(nombre);
                p.add(new JLabel("Rubro:")); p.add(rubro);
                p.add(new JLabel("Descripcion (opcional):")); p.add(descripcionEmp);
                int res = JOptionPane.showConfirmDialog(this, p, "Registrar empleador", JOptionPane.OK_CANCEL_OPTION);
                if (res == JOptionPane.OK_OPTION) {
                    if (empleadorController.registrarEmpleador(nombre.getText(), rubro.getText(), descripcionEmp.getText())) {
                        JOptionPane.showMessageDialog(this, "Empleador registrado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo registrar el empleador.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (sel == 1) {
                String input = JOptionPane.showInputDialog(this, "ID del empleador:");
                if (input == null) continue;
                try {
                    int id = Integer.parseInt(input.trim());
                    Empleador e = empleadorController.buscarEmpleador(id);
                    if (e == null) JOptionPane.showMessageDialog(this, "Empleador inexistente.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    else JOptionPane.showMessageDialog(this, e.toString(), "Empleador", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID invalido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (sel == 2) {
                JPanel p = new JPanel(new GridLayout(3, 2, 4, 4));
                JTextField id = new JTextField();
                JTextField nombre = new JTextField();
                JTextField rubro = new JTextField();
                p.add(new JLabel("ID del empleador a modificar:")); p.add(id);
                p.add(new JLabel("Nuevo nombre empresa:")); p.add(nombre);
                p.add(new JLabel("Nuevo rubro:")); p.add(rubro);
                int res = JOptionPane.showConfirmDialog(this, p, "Modificar empleador", JOptionPane.OK_CANCEL_OPTION);
                if (res == JOptionPane.OK_OPTION) {
                    try {
                        int eid = Integer.parseInt(id.getText().trim());
                        if (empleadorController.modificarEmpleador(eid, nombre.getText(), rubro.getText())) {
                            JOptionPane.showMessageDialog(this, "Empleador modificado correctamente.");
                        } else {
                            JOptionPane.showMessageDialog(this, "No se pudo modificar el empleador.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "ID invalido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (sel == 3) {
                String input = JOptionPane.showInputDialog(this, "ID del empleador a eliminar:");
                if (input == null) continue;
                try {
                    int id = Integer.parseInt(input.trim());
                    if (empleadorController.eliminarEmpleador(id)) JOptionPane.showMessageDialog(this, "Empleador eliminado.");
                    else JOptionPane.showMessageDialog(this, "No se pudo eliminar o no existe.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID invalido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (sel == 4) {
                final StringBuilder sb = new StringBuilder();
                empleadorController.recorrerEmpleadores(new implementaciones.DiccionarioABB.Visitante<Empleador>() {
                    @Override
                    public void visitar(int clave, Empleador valor) {
                        sb.append(valor.toString()).append(System.lineSeparator());
                    }
                });
                if (sb.length() == 0) sb.append("No hay empleadores registrados.");
                javax.swing.JTextArea area = new javax.swing.JTextArea(sb.toString());
                area.setEditable(false);
                area.setRows(15);
                area.setColumns(40);
                javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(area);
                JOptionPane.showMessageDialog(this, scroll, "Empleadores", JOptionPane.INFORMATION_MESSAGE);
            } else {
                break;
            }
        }
    }}
