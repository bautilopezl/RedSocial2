package implementaciones;

import modelo.Usuario;

public class NodoGrafo {
    private int idUsuario;
    private Usuario usuario;
    private NodoGrafo siguiente;
    private NodoGrafo siguienteContacto;

    public NodoGrafo(Usuario usuario) {
        this.usuario = usuario;
        this.idUsuario = usuario != null ? usuario.getId() : -1;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.idUsuario = usuario != null ? usuario.getId() : -1;
    }

    public NodoGrafo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoGrafo siguiente) {
        this.siguiente = siguiente;
    }

    public NodoGrafo getSiguienteContacto() {
        return siguienteContacto;
    }

    public void setSiguienteContacto(NodoGrafo siguienteContacto) {
        this.siguienteContacto = siguienteContacto;
    }
}
