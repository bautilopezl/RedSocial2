package servicios;

import implementaciones.DiccionarioABB;
import modelo.Empleador;
import persistencia.RepositorioEmpresas;
import persistencia.SequenceRepository;

public class GestorEmpleadores {
    private final DiccionarioABB<Empleador> empleadores;
    private final RepositorioEmpresas repositorioEmpresas;
    private final SequenceRepository sequenceRepository;

    public GestorEmpleadores(RepositorioEmpresas repositorioEmpresas, SequenceRepository sequenceRepository) {
        this.empleadores = new DiccionarioABB<>();
        this.repositorioEmpresas = repositorioEmpresas;
        this.sequenceRepository = sequenceRepository;
        cargarDatos();
    }

    private void cargarDatos() {
        for (Empleador e : repositorioEmpresas.cargarTodos()) {
            if (e != null && e.esValido()) {
                empleadores.agregar(e.getId(), e);
            }
        }
    }

    private void persistir() {
        java.util.List<Empleador> lista = new java.util.ArrayList<>();
        empleadores.recorrerEnOrden(new DiccionarioABB.Visitante<Empleador>() {
            @Override
            public void visitar(int clave, Empleador valor) {
                if (valor != null) {
                    lista.add(valor);
                }
            }
        });
        repositorioEmpresas.guardarTodos(lista);
    }

    public boolean registrarEmpleador(String nombreEmpresa, String rubro, String descripcion, String email) {
        int id = sequenceRepository.siguienteEmpresa();
        Empleador empleador = new Empleador(id, nombreEmpresa, rubro, descripcion);
        empleador.setEmail(email);
        if (!empleador.esValido() || empleadores.existeClave(empleador.getId())) {
            return false;
        }
        boolean ok = empleadores.agregar(empleador.getId(), empleador);
        if (ok) persistir();
        return ok;
    }

    public Empleador buscarEmpleador(int id) {
        if (id < 0) return null;
        return empleadores.recuperar(id);
    }

    public Empleador buscarPorEmail(String email) {
        String buscado = limpiar(email);
        if (buscado.isEmpty()) return null;
        final Empleador[] encontrado = {null};
        empleadores.recorrerEnOrden(new DiccionarioABB.Visitante<Empleador>() {
            @Override
            public void visitar(int clave, Empleador valor) {
                if (encontrado[0] == null && valor != null && valor.getEmail().equalsIgnoreCase(buscado)) {
                    encontrado[0] = valor;
                }
            }
        });
        return encontrado[0];
    }

    public boolean modificarEmpleador(int id, String nuevoNombre, String nuevoRubro, String nuevaDescripcion) {
        Empleador e = buscarEmpleador(id);
        if (e == null) return false;
        e.setNombreEmpresa(nuevoNombre);
        e.setRubro(nuevoRubro);
        e.setDescripcion(nuevaDescripcion);
        persistir();
        return true;
    }

    public boolean modificarEmpleador(int id, String nuevoNombre, String nuevoRubro) {
        return modificarEmpleador(id, nuevoNombre, nuevoRubro, "");
    }

    public boolean eliminarEmpleador(int id) {
        if (id < 0) return false;
        boolean ok = empleadores.eliminar(id);
        if (ok) persistir();
        return ok;
    }

    public boolean existeEmpleador(int id) {
        if (id < 0) return false;
        return empleadores.existeClave(id);
    }

    public int cantidadEmpleadores() {
        return empleadores.tamanio();
    }

    public void recorrerEmpleadores(DiccionarioABB.Visitante<Empleador> visitante) {
        empleadores.recorrerEnOrden(visitante);
    }

    private String limpiar(String texto) {
        if (texto == null) return "";
        return texto.trim();
    }
}
