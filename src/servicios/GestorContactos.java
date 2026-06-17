package servicios;

import implementaciones.GrafoContactos;
import modelo.Usuario;
import persistencia.RepositorioContactos;

public class GestorContactos {
    private final GrafoContactos grafo;
    private final RepositorioContactos repositorioContactos;

    public GestorContactos(RepositorioContactos repositorioContactos) {
        this.grafo = new GrafoContactos();
        this.repositorioContactos = repositorioContactos;
    }

    public void cargarConexiones() {
        cargarDatos();
    }

    private void cargarDatos() {
        for (int[] conexion : repositorioContactos.cargarTodos()) {
            if (conexion != null && conexion.length == 2) {
                grafo.agregarConexion(conexion[0], conexion[1]);
            }
        }
    }

    private void persistir() {
        java.util.List<int[]> conexiones = new java.util.ArrayList<>();
        int[] todosLosIds = grafo.obtenerIdsUsuarios();

        for (int i = 0; i < todosLosIds.length; i++) {
            if (todosLosIds[i] < 0) continue;
            int[] contactos = grafo.obtenerContactosDirectos(todosLosIds[i]);
            for (int j = 0; j < contactos.length; j++) {
                if (todosLosIds[i] < contactos[j]) {
                    conexiones.add(new int[]{todosLosIds[i], contactos[j]});
                }
            }
        }
        repositorioContactos.guardarTodos(conexiones);
    }

    public boolean registrarUsuario(Usuario usuario) {
        return grafo.agregarUsuario(usuario);
    }

    public boolean eliminarUsuario(int idUsuario) {
        if (idUsuario < 0) return false;
        boolean ok = grafo.eliminarUsuario(idUsuario);
        if (ok) persistir();
        return ok;
    }

    public boolean agregarConexion(int idUsuario1, int idUsuario2) {
        if (idUsuario1 < 0 || idUsuario2 < 0) return false;
        boolean ok = grafo.agregarConexion(idUsuario1, idUsuario2);
        if (ok) persistir();
        return ok;
    }

    public boolean eliminarConexion(int idUsuario1, int idUsuario2) {
        if (idUsuario1 < 0 || idUsuario2 < 0) return false;
        boolean ok = grafo.eliminarConexion(idUsuario1, idUsuario2);
        if (ok) persistir();
        return ok;
    }

    public boolean sonContactos(int idUsuario1, int idUsuario2) {
        if (idUsuario1 < 0 || idUsuario2 < 0) return false;
        return grafo.sonContactos(idUsuario1, idUsuario2);
    }

    public int gradoSeparacion(int idUsuario1, int idUsuario2) {
        if (idUsuario1 < 0 || idUsuario2 < 0) return -1;
        return grafo.gradoSeparacion(idUsuario1, idUsuario2);
    }

    public int[] obtenerContactosDirectos(int idUsuario) {
        if (idUsuario < 0) return new int[0];
        return grafo.obtenerContactosDirectos(idUsuario);
    }

    public int[] sugerirContactos(int idUsuario) {
        if (!grafo.existeUsuario(idUsuario)) return new int[0];
        int[] contactosDirectos = grafo.obtenerContactosDirectos(idUsuario);
        int[] sugeridos = new int[grafo.cantidadUsuarios()];
        int cantidadSugeridos = 0;

        for (int i = 0; i < contactosDirectos.length; i++) {
            int contacto = contactosDirectos[i];
            int[] secundarios = grafo.obtenerContactosDirectos(contacto);
            for (int j = 0; j < secundarios.length; j++) {
                int candidato = secundarios[j];
                if (candidato < 0) continue;
                if (candidato == idUsuario) continue;
                if (contiene(contactosDirectos, candidato)) continue;
                if (contiene(sugeridos, cantidadSugeridos, candidato)) continue;
                if (cantidadSugeridos < sugeridos.length) {
                    sugeridos[cantidadSugeridos] = candidato;
                    cantidadSugeridos++;
                }
            }
        }

        int[] resultado = new int[cantidadSugeridos];
        for (int i = 0; i < cantidadSugeridos; i++) {
            resultado[i] = sugeridos[i];
        }
        return resultado;
    }

    public Usuario buscarUsuario(int idUsuario) {
        return grafo.recuperarUsuario(idUsuario);
    }

    public boolean existeUsuario(int idUsuario) {
        if (idUsuario < 0) return false;
        return grafo.existeUsuario(idUsuario);
    }

    private boolean contiene(int[] arreglo, int valor) {
        return contiene(arreglo, arreglo.length, valor);
    }

    private boolean contiene(int[] arreglo, int cantidad, int valor) {
        for (int i = 0; i < cantidad; i++) {
            if (arreglo[i] == valor) {
                return true;
            }
        }
        return false;
    }
}
