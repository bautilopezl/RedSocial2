package modelo;

public class EstadoPerfil {
    private final int idUsuario;
    private final String nombre;
    private final String email;
    private final String profesion;
    private final String descripcion;

    public EstadoPerfil(int idUsuario, String nombre, String email, String profesion, String descripcion) {
        this.idUsuario = idUsuario;
        this.nombre = limpiar(nombre);
        this.email = limpiar(email);
        this.profesion = limpiar(profesion);
        this.descripcion = limpiar(descripcion);
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getProfesion() {
        return profesion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "EstadoPerfil{idUsuario=" + idUsuario + ", nombre='" + nombre + "', email='" + email + "', profesion='" + profesion + "', descripcion='" + descripcion + "'}";
    }

    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }
}
