package src.modelo;

import src.tda.Cola;
import src.tda.Pila;

// USUARIO
public class Usuario {

    private String id;
    private String nombre;
    private String email;
    private String habilidades;

    private Cola<Postulacion> postulaciones;

    private Pila<EstadoPerfil> historial;

    public Usuario(String id, String nombre, String email, String habilidades) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.habilidades = habilidades;
        this.postulaciones = new Cola<>();
        this.historial = new Pila<>();
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getHabilidades() { return habilidades; }

    //Editar perfil
    public void editarPerfil(String nuevoNombre, String nuevoEmail, String nuevasHabilidades) {
        historial.apilar(new EstadoPerfil(nombre, email, habilidades));
        this.nombre = nuevoNombre;
        this.email = nuevoEmail;
        this.habilidades = nuevasHabilidades;
        System.out.println("Perfil actualizado: " + this);
    }

    //Deshacer último cambio
    public void deshacerCambio() {
        if (historial.estaVacia()) {
            System.out.println("No hay cambios para deshacer.");
            return;
        }
        EstadoPerfil anterior = historial.desapilar();
        this.nombre = anterior.getNombre();
        this.email = anterior.getEmail();
        this.habilidades = anterior.getHabilidades();
        System.out.println("Cambio deshecho. Perfil restaurado: " + this);
    }

    //Postulaciones
    public void postularse(Postulacion postulacion) {
        postulaciones.encolar(postulacion);
        System.out.println(nombre + " se postuló a: " + postulacion.getIdEmpleo());
    }

    public Postulacion procesarPostulacion() {
        if (postulaciones.estaVacia()) {
            System.out.println("No hay postulaciones pendientes.");
            return null;
        }
        Postulacion p = postulaciones.desencolar();
        System.out.println("Procesando postulación: " + p);
        return p;
    }

    public int cantidadPostulaciones() {
        return postulaciones.tamanio();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario otro = (Usuario) obj;
        return this.id.equals(otro.id);
    }

    @Override
    public String toString() {
        return "Usuario{id=" + id +
               ", nombre=" + nombre +
               ", email=" + email +
               ", habilidades=" + habilidades + "}";
    }
}
