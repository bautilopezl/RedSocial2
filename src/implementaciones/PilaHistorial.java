package implementaciones;

import tda.PilaHistorialTDA;

public class PilaHistorial<T> implements PilaHistorialTDA<T> {
    private NodoPila<T> cima;
    private int tamanio;

    public PilaHistorial() {
        inicializar();
    }

    @Override
    public void inicializar() {
        cima = null;
        tamanio = 0;
    }

    @Override
    public void apilar(T dato) {
        if (dato == null) {
            return;
        }
        NodoPila<T> nuevo = new NodoPila<>(dato);
        nuevo.siguiente = cima;
        cima = nuevo;
        tamanio++;
    }

    @Override
    public T desapilar() {
        if (cima == null) {
            return null;
        }
        T dato = cima.dato;
        cima = cima.siguiente;
        tamanio--;
        return dato;
    }

    @Override
    public T tope() {
        if (cima == null) {
            return null;
        }
        return cima.dato;
    }

    @Override
    public boolean pilaVacia() {
        return cima == null;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }
}

