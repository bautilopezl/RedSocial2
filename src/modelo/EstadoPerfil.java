package src.modelo;

// ESTADO PERFIL

public class EstadoPerfil {

    private String nombre;
    private String email;
    private String habilidades;

    public EstadoPerfil(String nombre, String email, String habilidades) {
        this.nombre = nombre;
        this.email = email;
        this.habilidades = habilidades;
    }

    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getHabilidades() { return habilidades; }

    @Override
    public String toString() {
        return "EstadoPerfil{nombre=" + nombre +
               ", email=" + email +
               ", habilidades=" + habilidades + "}";
    }
}
