package controladores;

import implementaciones.DiccionarioABB;
import modelo.Usuario;
import servicios.GestorContactos;
import servicios.GestorUsuarios;

public class UsuarioController {
    private final GestorUsuarios gestorUsuarios;
    private final GestorContactos gestorContactos;

    public UsuarioController(GestorUsuarios gestorUsuarios, GestorContactos gestorContactos) {
        this.gestorUsuarios = gestorUsuarios;
        this.gestorContactos = gestorContactos;
    }

    public boolean registrarUsuario(String nombre, String email, String profesion, String descripcion) {
        if (esVacio(nombre) || esVacio(email)) return false;

        boolean registrado = gestorUsuarios.registrarUsuario(nombre, email, profesion, descripcion);
        if (registrado) {
            Usuario u = gestorUsuarios.buscarPorEmail(email);
            if (u != null) {
                if (!gestorContactos.registrarUsuario(u)) {
                    gestorUsuarios.eliminarUsuario(u.getId());
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Usuario buscarUsuario(int id) {
        if (id < 0) return null;
        return gestorUsuarios.buscarUsuario(id);
    }

    public Usuario buscarPorEmail(String email) {
        if (esVacio(email)) return null;
        return gestorUsuarios.buscarPorEmail(email);
    }

    public boolean modificarUsuario(int id, String nombre, String email, String profesion, String descripcion) {
        if (id < 0 || esVacio(nombre) || esVacio(email)) return false;
        return gestorUsuarios.modificarUsuario(id, nombre, email, profesion, descripcion);
    }

    public boolean eliminarUsuario(int id) {
        if (id < 0) return false;
        boolean eliminadoGrafo = gestorContactos.eliminarUsuario(id);
        boolean eliminadoUsuario = gestorUsuarios.eliminarUsuario(id);
        return eliminadoUsuario || eliminadoGrafo;
    }

    public boolean deshacerCambioPerfil(int id) {
        if (id < 0) return false;
        return gestorUsuarios.deshacerCambioPerfil(id);
    }

    public Usuario[] obtenerTodosLosUsuarios() {
        int cantidad = gestorUsuarios.cantidadUsuarios();
        Usuario[] resultado = new Usuario[cantidad];
        final int[] index = {0};

        gestorUsuarios.recorrerUsuarios(new DiccionarioABB.Visitante<Usuario>() {
            @Override
            public void visitar(int clave, Usuario valor) {
                if (index[0] < cantidad) {
                    resultado[index[0]] = valor;
                    index[0]++;
                }
            }
        });

        return resultado;
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
