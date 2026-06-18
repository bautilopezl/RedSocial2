package tda;

public interface PilaHistorialTDA<T> {
    void inicializar();

    void apilar(T dato);

    T desapilar();

    T tope();

    boolean pilaVacia();

    int tamanio();
}

