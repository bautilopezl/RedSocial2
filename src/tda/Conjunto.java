package src.tda;

// TDA CONJUNTO
public class Conjunto<T> {

    private Lista<T> elementos;

    public Conjunto() {
        this.elementos = new Lista<>();
    }

    public void agregar(T dato) {
        if (!elementos.contiene(dato)) {
            elementos.agregar(dato);
        }
    }

    public boolean contiene(T dato) {
        return elementos.contiene(dato);
    }

    public boolean eliminar(T dato) {
        return elementos.eliminar(dato);
    }

    public int tamanio() { return elementos.tamanio(); }

    public boolean estaVacio() { return elementos.estaVacia(); }

    public T obtener(int indice) { return elementos.obtener(indice); }

    public void mostrar() { elementos.mostrar(); }
}
