package tda;

public interface ColaPostulacionesTDA<T> {
    void inicializar();

    void acolar(T dato);

    T desacolar();

    T primero();

    boolean colaVacia();

    int tamanio();
}

