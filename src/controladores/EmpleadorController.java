package controladores;

import implementaciones.DiccionarioABB.Visitante;
import modelo.Empleador;
import servicios.GestorEmpleadores;

public class EmpleadorController {
    private final GestorEmpleadores gestorEmpleadores;

    public EmpleadorController(GestorEmpleadores gestorEmpleadores) {
        this.gestorEmpleadores = gestorEmpleadores;
    }

    public boolean registrarEmpleador(String nombre, String rubro, String descripcion) {
        return registrarEmpleador(nombre, rubro, descripcion, "");
    }

    public boolean registrarEmpleador(String nombre, String rubro, String descripcion, String email) {
        if (esVacio(nombre) || esVacio(rubro)) return false;
        return gestorEmpleadores.registrarEmpleador(nombre, rubro, descripcion, email);
    }

    public Empleador buscarEmpleador(int id) {
        if (id < 0) return null;
        return gestorEmpleadores.buscarEmpleador(id);
    }

    public Empleador buscarPorEmail(String email) {
        if (esVacio(email)) return null;
        return gestorEmpleadores.buscarPorEmail(email);
    }

    public boolean modificarEmpleador(int id, String nombre, String rubro) {
        if (id < 0 || esVacio(nombre) || esVacio(rubro)) return false;
        return gestorEmpleadores.modificarEmpleador(id, nombre, rubro, "");
    }

    public boolean eliminarEmpleador(int id) {
        if (id < 0) return false;
        return gestorEmpleadores.eliminarEmpleador(id);
    }

    public void recorrerEmpleadores(Visitante<Empleador> visitante) {
        gestorEmpleadores.recorrerEmpleadores(visitante);
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
