package src.tda;

public class Pila<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo tope;
    private int tamanio;

    public Pila() {
        this.tope = null;
        this.tamanio = 0;
    }

    public void apilar(T dato) {
        Nodo nuevo = new Nodo(dato);
        nuevo.siguiente = tope;
        tope = nuevo;
        tamanio++;
    }

    public T desapilar() {
        if (estaVacia()) {
            System.out.println("La pila esta vacia.");
            return null;
        }
        T dato = tope.dato;
        tope = tope.siguiente;
        tamanio--;
        return dato;
    }

    public T verTope() {
        if (estaVacia()) return null;
        return tope.dato;
    }

    public boolean estaVacia() { return tope == null; }

    public int tamanio() { return tamanio; }
}
