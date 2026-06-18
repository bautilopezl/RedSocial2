package servicios;

import implementaciones.ArbolHabilidades;
import implementaciones.DiccionarioABB;
import modelo.Usuario;

public class GestorHabilidades {
    private final ArbolHabilidades arbolHabilidades;
    private final GestorUsuarios gestorUsuarios;

    public GestorHabilidades(GestorUsuarios gestorUsuarios) {
        this.arbolHabilidades = new ArbolHabilidades();
        this.gestorUsuarios = gestorUsuarios;
    }

    public void inicializar(String categoriaRaiz) {
        arbolHabilidades.inicializar(categoriaRaiz);
    }

    public boolean agregarCategoria(String categoriaPadre, String nuevaCategoria) {
        return arbolHabilidades.agregarCategoria(categoriaPadre, nuevaCategoria);
    }

    public boolean buscarCategoria(String categoria) {
        return arbolHabilidades.buscarCategoria(categoria);
    }

    public void mostrarJerarquia() {
        arbolHabilidades.mostrarJerarquia();
    }

    public String obtenerJerarquiaTexto() {
        return arbolHabilidades.obtenerJerarquiaTexto();
    }

    public boolean asociarHabilidadAUsuario(int idUsuario, String habilidad) {
        if (!buscarCategoria(habilidad)) {
            return false;
        }
        Usuario usuario = gestorUsuarios.buscarUsuario(idUsuario);
        if (usuario == null) {
            return false;
        }
        return usuario.agregarHabilidad(habilidad);
    }

    public boolean desasociarHabilidadDeUsuario(int idUsuario, String habilidad) {
        Usuario usuario = gestorUsuarios.buscarUsuario(idUsuario);
        if (usuario == null) {
            return false;
        }
        return usuario.eliminarHabilidad(habilidad);
    }

    public Usuario[] buscarUsuariosPorHabilidad(String habilidad) {
        final String habilidadLimpia = limpiar(habilidad);
        if (habilidadLimpia.isEmpty()) {
            return new Usuario[0];
        }
        final Usuario[] coincidencias = new Usuario[gestorUsuarios.cantidadUsuarios()];
        final int[] indice = new int[]{0};

        gestorUsuarios.recorrerUsuarios(new DiccionarioABB.Visitante<Usuario>() {
            @Override
            public void visitar(int clave, Usuario usuario) {
                if (usuario != null && usuario.tieneHabilidad(habilidadLimpia)) {
                    coincidencias[indice[0]] = usuario;
                    indice[0]++;
                }
            }
        });

        Usuario[] resultado = new Usuario[indice[0]];
        for (int i = 0; i < indice[0]; i++) {
            resultado[i] = coincidencias[i];
        }
        return resultado;
    }

    public String[] obtenerCategorias() {
        return arbolHabilidades.obtenerCategorias();
    }

    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }
}
