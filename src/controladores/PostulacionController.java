package controladores;

import implementaciones.DiccionarioABB;
import modelo.Postulacion;
import servicios.GestorEmpleadores;
import servicios.GestorPostulaciones;

public class PostulacionController {
    private final GestorPostulaciones gestorPostulaciones;
    private final GestorEmpleadores gestorEmpleadores;

    public PostulacionController(GestorPostulaciones gestorPostulaciones, GestorEmpleadores gestorEmpleadores) {
        this.gestorPostulaciones = gestorPostulaciones;
        this.gestorEmpleadores = gestorEmpleadores;
    }

    public boolean registrarOfertaLaboral(String titulo, String descripcion, int idEmpleador) {
        if (esVacio(titulo) || idEmpleador < 0) return false;
        if (!gestorEmpleadores.existeEmpleador(idEmpleador)) return false;
        return gestorPostulaciones.registrarOfertaLaboral(titulo, descripcion, idEmpleador);
    }

    public boolean actualizarOferta(int idOferta, String titulo, String descripcion) {
        if (idOferta < 0 || esVacio(titulo)) return false;
        return gestorPostulaciones.actualizarOferta(idOferta, titulo, descripcion);
    }

    public boolean eliminarOferta(int idOferta) {
        if (idOferta < 0) return false;
        return gestorPostulaciones.eliminarOferta(idOferta);
    }

    public boolean cerrarOferta(int idOferta) {
        if (idOferta < 0) return false;
        return gestorPostulaciones.cerrarOferta(idOferta);
    }

    public boolean postularUsuario(int idUsuario, int idOferta) {
        if (idUsuario < 0 || idOferta < 0) return false;
        return gestorPostulaciones.postularUsuario(idUsuario, idOferta);
    }

    public Postulacion procesarPostulacion() {
        return gestorPostulaciones.procesarPostulacion();
    }

    public int cantidadPostulacionesPendientes() {
        return gestorPostulaciones.cantidadPostulacionesPendientes();
    }

    public Postulacion verPrimeraPostulacion() {
        return gestorPostulaciones.verPrimeraPostulacion();
    }

    public Postulacion[] obtenerPostulacionesDeUsuario(int idUsuario) {
        if (idUsuario < 0) return new Postulacion[0];

        int total = gestorPostulaciones.cantidadPostulacionesPendientes();
        java.util.ArrayList<Postulacion> todas = new java.util.ArrayList<>();

        for (int i = 0; i < total; i++) {
            Postulacion p = gestorPostulaciones.procesarPostulacion();
            if (p != null) todas.add(p);
        }

        for (Postulacion p : todas) {
            gestorPostulaciones.postularUsuario(p.getUsuario().getId(), p.getOferta().getId());
        }

        java.util.ArrayList<Postulacion> delUsuario = new java.util.ArrayList<>();
        for (Postulacion p : todas) {
            if (p.getUsuario() != null && idUsuario == p.getUsuario().getId()) {
                delUsuario.add(p);
            }
        }

        return delUsuario.toArray(new Postulacion[0]);
    }

    public modelo.OfertaLaboral[] obtenerTodasLasOfertas() {
        int cantidad = gestorPostulaciones.cantidadOfertas();
        modelo.OfertaLaboral[] resultado = new modelo.OfertaLaboral[cantidad];

        final int[] index = {0};

        gestorPostulaciones.recorrerOfertas(new DiccionarioABB.Visitante<modelo.OfertaLaboral>() {
            @Override
            public void visitar(int clave, modelo.OfertaLaboral valor) {
                if (index[0] < cantidad) {
                    resultado[index[0]] = valor;
                    index[0]++;
                }
            }
        });

        return resultado;
    }

    public modelo.OfertaLaboral[] obtenerOfertasPorEmpleador(int idEmpleador) {
        modelo.OfertaLaboral[] todas = obtenerTodasLasOfertas();
        if (todas == null) return new modelo.OfertaLaboral[0];

        java.util.ArrayList<modelo.OfertaLaboral> filtradas = new java.util.ArrayList<>();
        for (modelo.OfertaLaboral of : todas) {
            if (of != null && of.getEmpleador() != null && of.getEmpleador().getId() == idEmpleador) {
                filtradas.add(of);
            }
        }
        return filtradas.toArray(new modelo.OfertaLaboral[0]);
    }

    public Postulacion[] obtenerPostulacionesPorEmpleador(int idEmpleador) {
        if (idEmpleador < 0) return new Postulacion[0];

        int total = gestorPostulaciones.cantidadPostulacionesPendientes();
        java.util.ArrayList<Postulacion> todas = new java.util.ArrayList<>();

        for (int i = 0; i < total; i++) {
            Postulacion p = gestorPostulaciones.procesarPostulacion();
            if (p != null) todas.add(p);
        }

        for (Postulacion p : todas) {
            gestorPostulaciones.postularUsuario(p.getUsuario().getId(), p.getOferta().getId());
        }

        java.util.ArrayList<Postulacion> delEmpleador = new java.util.ArrayList<>();
        for (Postulacion p : todas) {
            if (p.getOferta() != null && p.getOferta().getEmpleador() != null
                    && p.getOferta().getEmpleador().getId() == idEmpleador) {
                delEmpleador.add(p);
            }
        }

        return delEmpleador.toArray(new Postulacion[0]);
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
