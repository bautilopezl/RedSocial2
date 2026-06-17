package servicios;

import implementaciones.DiccionarioABB;
import implementaciones.PilaHistorial;
import modelo.EstadoPerfil;
import modelo.Usuario;
import persistencia.RepositorioUsuarios;
import persistencia.SequenceRepository;

public class GestorUsuarios {
    private final DiccionarioABB<Usuario> usuarios;
    private final PilaHistorial<EstadoPerfil> historialPerfiles;
    private final RepositorioUsuarios repositorioUsuarios;
    private final SequenceRepository sequenceRepository;

    public GestorUsuarios(RepositorioUsuarios repositorioUsuarios, SequenceRepository sequenceRepository) {
        this.usuarios = new DiccionarioABB<>();
        this.historialPerfiles = new PilaHistorial<>();
        this.repositorioUsuarios = repositorioUsuarios;
        this.sequenceRepository = sequenceRepository;
        cargarDatos();
    }

    private void cargarDatos() {
        for (Usuario u : repositorioUsuarios.cargarTodos()) {
            if (u != null && u.esValido()) {
                usuarios.agregar(u.getId(), u);
            }
        }
    }

    private void persistir() {
        java.util.List<Usuario> lista = new java.util.ArrayList<>();
        usuarios.recorrerEnOrden(new DiccionarioABB.Visitante<Usuario>() {
            @Override
            public void visitar(int clave, Usuario valor) {
                if (valor != null) {
                    lista.add(valor);
                }
            }
        });
        repositorioUsuarios.guardarTodos(lista);
    }

    public boolean registrarUsuario(String nombre, String email, String profesion, String descripcion) {
        int id = sequenceRepository.siguienteUsuario();
        Usuario usuario = new Usuario(id, nombre, email, profesion, descripcion);
        if (!usuario.esValido() || usuarios.existeClave(usuario.getId())) {
            return false;
        }
        boolean ok = usuarios.agregar(usuario.getId(), usuario);
        if (ok) persistir();
        return ok;
    }

    public Usuario buscarUsuario(int id) {
        if (id < 0) return null;
        return usuarios.recuperar(id);
    }

    public Usuario buscarPorEmail(String email) {
        String buscado = limpiar(email);
        if (buscado.isEmpty()) return null;
        final Usuario[] encontrado = {null};
        usuarios.recorrerEnOrden(new DiccionarioABB.Visitante<Usuario>() {
            @Override
            public void visitar(int clave, Usuario valor) {
                if (encontrado[0] == null && valor != null && valor.getEmail().equalsIgnoreCase(buscado)) {
                    encontrado[0] = valor;
                }
            }
        });
        return encontrado[0];
    }

    public boolean modificarUsuario(int id, String nombre, String email, String profesion, String descripcion) {
        Usuario usuario = buscarUsuario(id);
        if (usuario == null) return false;
        if (esVacio(nombre) || esVacio(email) || esVacio(profesion)) return false;
        historialPerfiles.apilar(usuario.crearEstadoPerfil());
        usuario.actualizarPerfil(nombre, email, profesion, descripcion);
        persistir();
        return true;
    }

    public boolean eliminarUsuario(int id) {
        if (id < 0) return false;
        boolean ok = usuarios.eliminar(id);
        if (ok) persistir();
        return ok;
    }

    public boolean deshacerCambioPerfil(int idUsuario) {
        if (idUsuario < 0) return false;
        PilaHistorial<EstadoPerfil> temporal = new PilaHistorial<>();
        EstadoPerfil estadoEncontrado = null;
        while (!historialPerfiles.pilaVacia()) {
            EstadoPerfil estado = historialPerfiles.desapilar();
            if (estado != null && estado.getIdUsuario() == idUsuario) {
                estadoEncontrado = estado;
                break;
            }
            temporal.apilar(estado);
        }
        while (!temporal.pilaVacia()) {
            historialPerfiles.apilar(temporal.desapilar());
        }
        if (estadoEncontrado == null) return false;
        Usuario usuario = buscarUsuario(idUsuario);
        if (usuario == null) return false;
        usuario.restaurarPerfil(estadoEncontrado);
        persistir();
        return true;
    }

    public boolean existeUsuario(int id) {
        if (id < 0) return false;
        return usuarios.existeClave(id);
    }

    public int cantidadUsuarios() {
        return usuarios.tamanio();
    }

    public void recorrerUsuarios(DiccionarioABB.Visitante<Usuario> visitante) {
        usuarios.recorrerEnOrden(visitante);
    }

    private boolean esVacio(String texto) {
        return limpiar(texto).isEmpty();
    }

    private String limpiar(String texto) {
        if (texto == null) return "";
        return texto.trim();
    }
}
