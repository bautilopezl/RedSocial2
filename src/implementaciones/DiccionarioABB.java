package implementaciones;

import tda.DiccionarioUsuariosTDA;

public class DiccionarioABB<V> implements DiccionarioUsuariosTDA<V> {
    private NodoDiccionario<V> raiz;
    private int tamanio;

    public interface Visitante<V> {
        void visitar(int clave, V valor);
    }

    public DiccionarioABB() {
        inicializar();
    }

    @Override
    public void inicializar() {
        raiz = null;
        tamanio = 0;
    }

    @Override
    public boolean agregar(int clave, V valor) {
        if (clave < 0 || valor == null) {
            return false;
        }
        if (raiz == null) {
            raiz = new NodoDiccionario<>(clave, valor);
            tamanio++;
            return true;
        }
        if (agregarRecursivo(raiz, clave, valor)) {
            tamanio++;
            return true;
        }
        return false;
    }

    private boolean agregarRecursivo(NodoDiccionario<V> nodo, int clave, V valor) {
        if (clave == nodo.clave) {
            return false;
        }
        if (clave < nodo.clave) {
            if (nodo.izquierdo == null) {
                nodo.izquierdo = new NodoDiccionario<>(clave, valor);
                return true;
            }
            return agregarRecursivo(nodo.izquierdo, clave, valor);
        }
        if (nodo.derecho == null) {
            nodo.derecho = new NodoDiccionario<>(clave, valor);
            return true;
        }
        return agregarRecursivo(nodo.derecho, clave, valor);
    }

    @Override
    public boolean eliminar(int clave) {
        if (clave < 0 || raiz == null) {
            return false;
        }
        boolean[] eliminado = new boolean[1];
        raiz = eliminarRecursivo(raiz, clave, eliminado);
        if (eliminado[0]) {
            tamanio--;
        }
        return eliminado[0];
    }

    private NodoDiccionario<V> eliminarRecursivo(NodoDiccionario<V> nodo, int clave, boolean[] eliminado) {
        if (nodo == null) {
            return null;
        }
        if (clave < nodo.clave) {
            nodo.izquierdo = eliminarRecursivo(nodo.izquierdo, clave, eliminado);
            return nodo;
        }
        if (clave > nodo.clave) {
            nodo.derecho = eliminarRecursivo(nodo.derecho, clave, eliminado);
            return nodo;
        }

        eliminado[0] = true;
        if (nodo.izquierdo == null) {
            return nodo.derecho;
        }
        if (nodo.derecho == null) {
            return nodo.izquierdo;
        }

        NodoDiccionario<V> sucesor = minimo(nodo.derecho);
        nodo.clave = sucesor.clave;
        nodo.valor = sucesor.valor;
        nodo.derecho = eliminarMinimo(nodo.derecho);
        return nodo;
    }

    private NodoDiccionario<V> eliminarMinimo(NodoDiccionario<V> nodo) {
        if (nodo.izquierdo == null) {
            return nodo.derecho;
        }
        nodo.izquierdo = eliminarMinimo(nodo.izquierdo);
        return nodo;
    }

    private NodoDiccionario<V> minimo(NodoDiccionario<V> nodo) {
        NodoDiccionario<V> actual = nodo;
        while (actual != null && actual.izquierdo != null) {
            actual = actual.izquierdo;
        }
        return actual;
    }

    @Override
    public V recuperar(int clave) {
        if (clave < 0) {
            return null;
        }
        NodoDiccionario<V> actual = raiz;
        while (actual != null) {
            if (clave == actual.clave) {
                return actual.valor;
            }
            if (clave < actual.clave) {
                actual = actual.izquierdo;
            } else {
                actual = actual.derecho;
            }
        }
        return null;
    }

    @Override
    public boolean existeClave(int clave) {
        return recuperar(clave) != null;
    }

    @Override
    public boolean diccionarioVacio() {
        return raiz == null;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    public void recorrerEnOrden(Visitante<V> visitante) {
        if (visitante == null) {
            return;
        }
        recorrerEnOrdenRecursivo(raiz, visitante);
    }

    private void recorrerEnOrdenRecursivo(NodoDiccionario<V> nodo, Visitante<V> visitante) {
        if (nodo == null) {
            return;
        }
        recorrerEnOrdenRecursivo(nodo.izquierdo, visitante);
        visitante.visitar(nodo.clave, nodo.valor);
        recorrerEnOrdenRecursivo(nodo.derecho, visitante);
    }
}
