package servicios;

import implementaciones.ColaPostulaciones;
import implementaciones.DiccionarioABB;
import modelo.Empleador;
import modelo.OfertaLaboral;
import modelo.Postulacion;
import modelo.Usuario;
import persistencia.RepositorioOfertas;
import persistencia.RepositorioPostulaciones;
import persistencia.SequenceRepository;

public class GestorPostulaciones {
    private final DiccionarioABB<OfertaLaboral> ofertas;
    private final ColaPostulaciones<Postulacion> colaPostulaciones;
    private final GestorUsuarios gestorUsuarios;
    private final GestorEmpleadores gestorEmpleadores;
    private final RepositorioOfertas repositorioOfertas;
    private final RepositorioPostulaciones repositorioPostulaciones;
    private final SequenceRepository sequenceRepository;
    private final java.util.ArrayList<String> postulacionesExistentes;

    public GestorPostulaciones(GestorUsuarios gestorUsuarios, GestorEmpleadores gestorEmpleadores,
                                RepositorioOfertas repositorioOfertas,
                                RepositorioPostulaciones repositorioPostulaciones,
                                SequenceRepository sequenceRepository) {
        this.ofertas = new DiccionarioABB<>();
        this.colaPostulaciones = new ColaPostulaciones<>();
        this.gestorUsuarios = gestorUsuarios;
        this.gestorEmpleadores = gestorEmpleadores;
        this.repositorioOfertas = repositorioOfertas;
        this.repositorioPostulaciones = repositorioPostulaciones;
        this.sequenceRepository = sequenceRepository;
        this.postulacionesExistentes = new java.util.ArrayList<>();
        cargarDatos();
    }

    private void cargarDatos() {
        for (OfertaLaboral of : repositorioOfertas.cargarTodas()) {
            if (of != null && of.esValida()) {
                ofertas.agregar(of.getId(), of);
            }
        }
        for (Postulacion p : repositorioPostulaciones.cargarTodas()) {
            if (p != null && p.esValida()) {
                colaPostulaciones.acolar(p);
                String key = p.getUsuario().getId() + "|" + p.getOferta().getId();
                postulacionesExistentes.add(key);
            }
        }
    }

    private void persistirOfertas() {
        java.util.List<OfertaLaboral> lista = new java.util.ArrayList<>();
        ofertas.recorrerEnOrden(new DiccionarioABB.Visitante<OfertaLaboral>() {
            @Override
            public void visitar(int clave, OfertaLaboral valor) {
                if (valor != null) {
                    lista.add(valor);
                }
            }
        });
        repositorioOfertas.guardarTodas(lista);
    }

    private void persistirPostulaciones() {
        java.util.List<Postulacion> lista = new java.util.ArrayList<>();
        ColaPostulaciones<Postulacion> temp = new ColaPostulaciones<>();
        while (!colaPostulaciones.colaVacia()) {
            Postulacion p = colaPostulaciones.desacolar();
            if (p != null) {
                lista.add(p);
                temp.acolar(p);
            }
        }
        while (!temp.colaVacia()) {
            colaPostulaciones.acolar(temp.desacolar());
        }
        repositorioPostulaciones.guardarTodas(lista);
    }

    public boolean registrarOfertaLaboral(String titulo, String descripcion, int idEmpleador) {
        Empleador empleador = gestorEmpleadores.buscarEmpleador(idEmpleador);
        if (empleador == null) return false;

        int id = sequenceRepository.siguienteOferta();
        OfertaLaboral oferta = new OfertaLaboral(id, titulo, descripcion, empleador);
        if (!oferta.esValida() || ofertas.existeClave(oferta.getId())) {
            return false;
        }
        boolean ok = ofertas.agregar(oferta.getId(), oferta);
        if (ok) persistirOfertas();
        return ok;
    }

    public boolean actualizarOferta(int idOferta, String titulo, String descripcion) {
        OfertaLaboral oferta = buscarOferta(idOferta);
        if (oferta == null) return false;
        oferta.setTitulo(titulo);
        oferta.setDescripcion(descripcion);
        persistirOfertas();
        return true;
    }

    public boolean eliminarOferta(int idOferta) {
        if (idOferta < 0) return false;
        boolean ok = ofertas.eliminar(idOferta);
        if (ok) persistirOfertas();
        return ok;
    }

    public boolean cerrarOferta(int idOferta) {
        OfertaLaboral oferta = buscarOferta(idOferta);
        if (oferta == null) return false;
        oferta.setActiva(false);
        persistirOfertas();
        return true;
    }

    public OfertaLaboral buscarOferta(int idOferta) {
        if (idOferta < 0) return null;
        return ofertas.recuperar(idOferta);
    }

    public boolean postularUsuario(int idUsuario, int idOferta) {
        Usuario usuario = gestorUsuarios.buscarUsuario(idUsuario);
        OfertaLaboral oferta = buscarOferta(idOferta);
        if (usuario == null || oferta == null) return false;

        String key = idUsuario + "|" + idOferta;
        if (postulacionesExistentes.contains(key)) return false;

        postulacionesExistentes.add(key);
        colaPostulaciones.acolar(new Postulacion(usuario, oferta));
        persistirPostulaciones();
        return true;
    }

    public Postulacion procesarPostulacion() {
        Postulacion p = colaPostulaciones.desacolar();
        if (p != null) {
            String key = p.getUsuario().getId() + "|" + p.getOferta().getId();
            postulacionesExistentes.remove(key);
            persistirPostulaciones();
        }
        return p;
    }

    public Postulacion verPrimeraPostulacion() {
        return colaPostulaciones.primero();
    }

    public boolean colaVacia() {
        return colaPostulaciones.colaVacia();
    }

    public int cantidadPostulacionesPendientes() {
        return colaPostulaciones.tamanio();
    }

    public boolean existeOferta(int idOferta) {
        if (idOferta < 0) return false;
        return ofertas.existeClave(idOferta);
    }

    public int cantidadOfertas() {
        return ofertas.tamanio();
    }

    public void recorrerOfertas(DiccionarioABB.Visitante<OfertaLaboral> visitante) {
        ofertas.recorrerEnOrden(visitante);
    }
}
