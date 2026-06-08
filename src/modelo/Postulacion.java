package src.modelo;

// POSTULACION

public class Postulacion {

    private String idUsuario;
    private String idEmpleo;
    private String fecha;

    public Postulacion(String idUsuario, String idEmpleo, String fecha) {
        this.idUsuario = idUsuario;
        this.idEmpleo = idEmpleo;
        this.fecha = fecha;
    }

    public String getIdUsuario() { return idUsuario; }
    public String getIdEmpleo() { return idEmpleo; }
    public String getFecha() { return fecha; }

    @Override
    public String toString() {
        return "Postulacion{usuario=" + idUsuario +
               ", empleo=" + idEmpleo +
               ", fecha=" + fecha + "}";
    }
}
