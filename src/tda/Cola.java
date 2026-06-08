package src.tda;

public class Cola<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo frente;
    private Nodo fin;
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    public void encolar(T dato) {
        Nodo nuevo = new Nodo(dato);
        if (fin == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    public T desencolar() {
        if (estaVacia()) {
            System.out.println("La cola esta vacia.");
            return null;
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamanio--;
        return dato;
    }

    public T verFrente() {
        if (estaVacia()) return null;
        return frente.dato;
    }

    public boolean estaVacia() { return frente == null; }

    public int tamanio() { return tamanio; }
}
