package implementaciones;

public class NodoPila<T> {
    T dato;
    NodoPila<T> siguiente;

    public NodoPila(T dato) {
        this.dato = dato;
    }
}

