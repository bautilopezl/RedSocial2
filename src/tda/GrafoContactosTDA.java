package tda;

import modelo.Usuario;

public interface GrafoContactosTDA {
    void inicializar();

    boolean agregarUsuario(Usuario usuario);

    boolean eliminarUsuario(int idUsuario);

    boolean agregarConexion(int idUsuario1, int idUsuario2);

    boolean eliminarConexion(int idUsuario1, int idUsuario2);

    boolean sonContactos(int idUsuario1, int idUsuario2);

    int gradoSeparacion(int idUsuario1, int idUsuario2);

    Usuario recuperarUsuario(int idUsuario);

    boolean existeUsuario(int idUsuario);

    int[] obtenerContactosDirectos(int idUsuario);

    int[] obtenerIdsUsuarios();
}
