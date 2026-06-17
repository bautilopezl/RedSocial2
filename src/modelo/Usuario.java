package modelo;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String profesion;
    private String descripcion;
    private String password;
    private String[] habilidades;
    private int cantidadHabilidades;

    public Usuario() {}

    public Usuario(int id, String nombre, String email, String profesion, String descripcion) {
        this.id = id;
        this.nombre = limpiar(nombre);
        this.email = limpiar(email);
        this.profesion = limpiar(profesion);
        this.descripcion = limpiar(descripcion);
        this.password = "";
        this.habilidades = new String[4];
        this.cantidadHabilidades = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = limpiar(password);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = limpiar(nombre);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = limpiar(email);
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = limpiar(profesion);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = limpiar(descripcion);
    }

    public boolean agregarHabilidad(String habilidad) {
        String habilidadLimpia = limpiar(habilidad);
        if (habilidadLimpia.isEmpty() || tieneHabilidad(habilidadLimpia)) {
            return false;
        }
        asegurarCapacidad();
        habilidades[cantidadHabilidades] = habilidadLimpia;
        cantidadHabilidades++;
        return true;
    }

    public boolean tieneHabilidad(String habilidad) {
        String buscada = limpiar(habilidad);
        for (int i = 0; i < cantidadHabilidades; i++) {
            if (habilidades[i] != null && habilidades[i].equalsIgnoreCase(buscada)) {
                return true;
            }
        }
        return false;
    }

    public boolean eliminarHabilidad(String habilidad) {
        String buscada = limpiar(habilidad);
        for (int i = 0; i < cantidadHabilidades; i++) {
            if (habilidades[i] != null && habilidades[i].equalsIgnoreCase(buscada)) {
                for (int j = i; j < cantidadHabilidades - 1; j++) {
                    habilidades[j] = habilidades[j + 1];
                }
                habilidades[cantidadHabilidades - 1] = null;
                cantidadHabilidades--;
                return true;
            }
        }
        return false;
    }

    public String[] obtenerHabilidades() {
        String[] copia = new String[cantidadHabilidades];
        for (int i = 0; i < cantidadHabilidades; i++) {
            copia[i] = habilidades[i];
        }
        return copia;
    }

    public int cantidadHabilidades() {
        return cantidadHabilidades;
    }

    public EstadoPerfil crearEstadoPerfil() {
        return new EstadoPerfil(id, nombre, email, profesion, descripcion);
    }

    public void restaurarPerfil(EstadoPerfil estadoPerfil) {
        if (estadoPerfil == null) {
            return;
        }
        if (id != estadoPerfil.getIdUsuario()) {
            return;
        }
        this.nombre = limpiar(estadoPerfil.getNombre());
        this.email = limpiar(estadoPerfil.getEmail());
        this.profesion = limpiar(estadoPerfil.getProfesion());
        this.descripcion = limpiar(estadoPerfil.getDescripcion());
    }

    public void actualizarPerfil(String nombre, String email, String profesion, String descripcion) {
        this.nombre = limpiar(nombre);
        this.email = limpiar(email);
        this.profesion = limpiar(profesion);
        this.descripcion = limpiar(descripcion);
    }

    public boolean esValido() {
        return id > 0 && !nombre.isEmpty() && !email.isEmpty() && !profesion.isEmpty();
    }

    public String obtenerHabilidadesComoTexto() {
        if (cantidadHabilidades == 0) {
            return "Sin habilidades registradas";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cantidadHabilidades; i++) {
            sb.append(habilidades[i]);
            if (i < cantidadHabilidades - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nombre='" + nombre + "', email='" + email + "', profesion='" + profesion + "', descripcion='" + descripcion + "', habilidades=[" + obtenerHabilidadesComoTexto() + "]}";
    }

    private void asegurarCapacidad() {
        if (cantidadHabilidades < habilidades.length) {
            return;
        }
        String[] nuevoArreglo = new String[habilidades.length * 2];
        for (int i = 0; i < habilidades.length; i++) {
            nuevoArreglo[i] = habilidades[i];
        }
        habilidades = nuevoArreglo;
    }

    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }
}
