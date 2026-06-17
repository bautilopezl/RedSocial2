package modelo;

public class Empleador {
    private int id;
    private String nombreEmpresa;
    private String email;
    private String rubro;
    private String descripcion;
    private String password;

    public Empleador() {}

    public Empleador(int id, String nombreEmpresa, String rubro) {
        this(id, nombreEmpresa, rubro, "");
    }

    public Empleador(int id, String nombreEmpresa, String rubro, String descripcion) {
        this.id = id;
        this.nombreEmpresa = limpiar(nombreEmpresa);
        this.email = "";
        this.rubro = limpiar(rubro);
        this.descripcion = limpiar(descripcion);
        this.password = "";
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = limpiar(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = limpiar(email);
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = limpiar(nombreEmpresa);
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = limpiar(rubro);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = limpiar(descripcion);
    }

    public boolean esValido() {
        return id > 0 && !nombreEmpresa.isEmpty() && !rubro.isEmpty();
    }

    @Override
    public String toString() {
        return "Empleador{id=" + id + ", nombreEmpresa='" + nombreEmpresa + "', rubro='" + rubro + "', descripcion='" + descripcion + "'}";
    }

    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }
}
