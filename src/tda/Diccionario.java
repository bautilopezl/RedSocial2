package src.tda;

// TDA DICCIONARIO
public class Diccionario<K, V> {

    private class Entrada {
        K clave;
        V valor;
        Entrada siguiente;

        Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
    }

    private Entrada[] tabla;
    private int capacidad;
    private int tamanio;

    public Diccionario(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = (Entrada[]) new Object[capacidad];
        this.tamanio = 0;
    }

    private int calcularIndice(K clave) {
        String str = clave.toString();
        int hash = 0;
        for (int i = 0; i < str.length(); i++) {
<<<<<<< HEAD
            hash += str.charAt(i);        }
=======
            hash += str.charAt(i);
        }
>>>>>>> 22fa96c309a916cfa27d257fb19aabd4971dcee8
        if (hash < 0) hash = -hash;
        return hash % capacidad;
    }

    public void agregar(K clave, V valor) {
        int indice = calcularIndice(clave);
        Entrada aux = tabla[indice];

        while (aux != null) {
            if (aux.clave.equals(clave)) {
                aux.valor = valor;
                return;
            }
            aux = aux.siguiente;
        }

        Entrada nueva = new Entrada(clave, valor);
        nueva.siguiente = tabla[indice];
        tabla[indice] = nueva;
        tamanio++;
    }

    public V buscar(K clave) {
        int indice = calcularIndice(clave);
        Entrada aux = tabla[indice];

        while (aux != null) {
            if (aux.clave.equals(clave)) return aux.valor;
            aux = aux.siguiente;
        }
        return null;
    }

    public boolean eliminar(K clave) {
        int indice = calcularIndice(clave);

        if (tabla[indice] == null) return false;

        if (tabla[indice].clave.equals(clave)) {
            tabla[indice] = tabla[indice].siguiente;
            tamanio--;
            return true;
        }

        Entrada aux = tabla[indice];
        while (aux.siguiente != null) {
            if (aux.siguiente.clave.equals(clave)) {
                aux.siguiente = aux.siguiente.siguiente;
                tamanio--;
                return true;
            }
            aux = aux.siguiente;
        }
        return false;
    }

    public boolean contiene(K clave) {
        return buscar(clave) != null;
    }

    public int tamanio() { return tamanio; }

    public boolean estaVacio() { return tamanio == 0; }

    public Lista<K> claves() {
        Lista<K> lista = new Lista<>();
        for (int i = 0; i < capacidad; i++) {
            Entrada aux = tabla[i];
            while (aux != null) {
                lista.agregar(aux.clave);
                aux = aux.siguiente;
            }
        }
        return lista;
    }

    public Lista<V> valores() {
        Lista<V> lista = new Lista<>();
        for (int i = 0; i < capacidad; i++) {
            Entrada aux = tabla[i];
            while (aux != null) {
                lista.agregar(aux.valor);
                aux = aux.siguiente;
            }
        }
        return lista;
    }
}
