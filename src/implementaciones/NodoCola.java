package implementaciones;

public class NodoCola<T> {
    T dato;
    NodoCola<T> siguiente;

    public NodoCola(T dato) {
        this.dato = dato;
    }
}

