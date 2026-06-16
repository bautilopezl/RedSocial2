package implementaciones;

import tda.ColaPostulacionesTDA;

public class ColaPostulaciones<T> implements ColaPostulacionesTDA<T> {
    private NodoCola<T> frente;
    private NodoCola<T> fin;
    private int tamanio;

    public ColaPostulaciones() {
        inicializar();
    }

    @Override
    public void inicializar() {
        frente = null;
        fin = null;
        tamanio = 0;
    }

    @Override
    public void acolar(T dato) {
        if (dato == null) {
            return;
        }
        NodoCola<T> nuevo = new NodoCola<>(dato);
        if (fin == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    @Override
    public T desacolar() {
        if (frente == null) {
            return null;
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        tamanio--;
        return dato;
    }

    @Override
    public T primero() {
        if (frente == null) {
            return null;
        }
        return frente.dato;
    }

    @Override
    public boolean colaVacia() {
        return frente == null;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }
}

