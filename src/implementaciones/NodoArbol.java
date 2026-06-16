package implementaciones;

public class NodoArbol {
    private String categoria;
    private NodoArbol hijoIzquierdo;
    private NodoArbol hermanoDerecho;

    public NodoArbol(String categoria) {
        this.categoria = limpiar(categoria);
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = limpiar(categoria);
    }

    public NodoArbol getHijoIzquierdo() {
        return hijoIzquierdo;
    }

    public void setHijoIzquierdo(NodoArbol hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }

    public NodoArbol getHermanoDerecho() {
        return hermanoDerecho;
    }

    public void setHermanoDerecho(NodoArbol hermanoDerecho) {
        this.hermanoDerecho = hermanoDerecho;
    }

    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }
}

