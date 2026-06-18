package modelo;

public class Postulacion {
    private Usuario usuario;
    private OfertaLaboral oferta;

    public Postulacion() {}

    public Postulacion(Usuario usuario, OfertaLaboral oferta) {
        this.usuario = usuario;
        this.oferta = oferta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public OfertaLaboral getOferta() {
        return oferta;
    }

    public void setOferta(OfertaLaboral oferta) {
        this.oferta = oferta;
    }

    public boolean esValida() {
        return usuario != null && oferta != null;
    }

    @Override
    public String toString() {
        String idUsuario = usuario != null ? String.valueOf(usuario.getId()) : "null";
        String idOferta = oferta != null ? String.valueOf(oferta.getId()) : "null";
        return "Postulacion{usuario='" + idUsuario + "', oferta='" + idOferta + "'}";
    }
}

