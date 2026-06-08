package src.sistema;

import src.modelo.Usuario;
import src.tda.Diccionario;
import src.tda.Lista;

// GESTOR DE USUARIOS
public class GestorUsuarios {

    private Diccionario<String, Usuario> usuarios;

    public GestorUsuarios() {
        this.usuarios = new Diccionario<>(100);
    }

    public void registrar(Usuario usuario) {
        if (usuarios.contiene(usuario.getId())) {
            System.out.println("Ya existe un usuario con ID: " + usuario.getId());
            return;
        }
        usuarios.agregar(usuario.getId(), usuario);
        System.out.println("Usuario registrado: " + usuario.getNombre());
    }

    public Usuario buscar(String id) {
        Usuario u = usuarios.buscar(id);
        if (u == null) {
            System.out.println("No existe usuario con ID: " + id);
        }
        return u;
    }

    public boolean eliminar(String id) {
        if (!usuarios.contiene(id)) {
            System.out.println("No existe usuario con ID: " + id);
            return false;
        }
        usuarios.eliminar(id);
        System.out.println("Usuario eliminado: " + id);
        return true;
    }

    public void mostrarTodos() {
        System.out.println("── Usuarios registrados ──");
        Lista<Usuario> lista = usuarios.valores();
        for (int i = 0; i < lista.tamanio(); i++) {
            System.out.println("  " + lista.obtener(i));
        }
    }

    public int cantidad() { return usuarios.tamanio(); }
}
