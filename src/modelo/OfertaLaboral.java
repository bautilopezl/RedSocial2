package modelo;

public class OfertaLaboral {
    private int id;
    private String titulo;
    private String descripcion;
    private Empleador empleador;
    private boolean activa;

    public OfertaLaboral() {}

    public OfertaLaboral(int id, String titulo, String descripcion, Empleador empleador) {
        this.id = id;
        this.titulo = limpiar(titulo);
        this.descripcion = limpiar(descripcion);
        this.empleador = empleador;
        this.activa = true;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = limpiar(titulo);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = limpiar(descripcion);
    }

    public Empleador getEmpleador() {
        return empleador;
    }

    public void setEmpleador(Empleador empleador) {
        this.empleador = empleador;
    }

    public boolean esValida() {
        return id > 0 && !titulo.isEmpty() && !descripcion.isEmpty() && empleador != null;
    }

    @Override
    public String toString() {
        String nombreEmpleador = empleador != null ? empleador.getNombreEmpresa() : "Sin empleador";
        return "OfertaLaboral{id=" + id + ", titulo='" + titulo + "', descripcion='" + descripcion + "', empleador='" + nombreEmpleador + "'}";
    }

    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }
}
