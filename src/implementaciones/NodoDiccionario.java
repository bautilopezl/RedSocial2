package implementaciones;

public class NodoDiccionario<V> {
    int clave;
    V valor;
    NodoDiccionario<V> izquierdo;
    NodoDiccionario<V> derecho;

    public NodoDiccionario(int clave, V valor) {
        this.clave = clave;
        this.valor = valor;
    }
}

