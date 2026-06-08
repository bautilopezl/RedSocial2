package src.tda;

public class Lista<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo cabeza;
    private int tamanio;

    public Lista() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    public void agregar(T dato) {
        Nodo nuevo = new Nodo(dato);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo aux = cabeza;
            while (aux.siguiente != null) {
                aux = aux.siguiente;
            }
            aux.siguiente = nuevo;
        }
        tamanio++;
    }

    public boolean eliminar(T dato) {
        if (cabeza == null) return false;

        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            tamanio--;
            return true;
        }

        Nodo aux = cabeza;
        while (aux.siguiente != null) {
            if (aux.siguiente.dato.equals(dato)) {
                aux.siguiente = aux.siguiente.siguiente;
                tamanio--;
                return true;
            }
            aux = aux.siguiente;
        }
        return false;
    }

    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) return null;
        Nodo aux = cabeza;
        for (int i = 0; i < indice; i++) {
            aux = aux.siguiente;
        }
        return aux.dato;
    }

    public boolean contiene(T dato) {
        Nodo aux = cabeza;
        while (aux != null) {
            if (aux.dato.equals(dato)) return true;
            aux = aux.siguiente;
        }
        return false;
    }

    public int tamanio() { return tamanio; }

    public boolean estaVacia() { return tamanio == 0; }

    public void mostrar() {
        Nodo aux = cabeza;
        while (aux != null) {
            System.out.print(aux.dato + " ");
            aux = aux.siguiente;
        }
        System.out.println();
    }
}
