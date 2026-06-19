package controladores;

import modelo.Usuario;
import servicios.GestorHabilidades;

public class HabilidadController {
    private final GestorHabilidades gestorHabilidades;

    public HabilidadController(GestorHabilidades gestorHabilidades) {
        this.gestorHabilidades = gestorHabilidades;
    }

    public boolean agregarCategoria(String padre, String nuevaCategoria) {
        if (esVacio(nuevaCategoria)) return false;
        return gestorHabilidades.agregarCategoria(padre, nuevaCategoria);
    }

    public boolean buscarCategoria(String categoria) {
        if (esVacio(categoria)) return false;
        return gestorHabilidades.buscarCategoria(categoria);
    }

    public String obtenerJerarquiaTexto() {
        return gestorHabilidades.obtenerJerarquiaTexto();
    }

    public boolean asociarHabilidad(int idUsuario, String habilidad) {
        if (idUsuario < 0 || esVacio(habilidad)) return false;
        return gestorHabilidades.asociarHabilidadAUsuario(idUsuario, habilidad);
    }

    public boolean desasociarHabilidad(int idUsuario, String habilidad) {
        if (idUsuario < 0 || esVacio(habilidad)) return false;
        return gestorHabilidades.desasociarHabilidadDeUsuario(idUsuario, habilidad);
    }

    public Usuario[] buscarUsuariosPorHabilidad(String habilidad) {
        if (esVacio(habilidad)) return new Usuario[0];
        return gestorHabilidades.buscarUsuariosPorHabilidad(habilidad);
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
