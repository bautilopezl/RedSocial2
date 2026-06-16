package tda;

public interface DiccionarioUsuariosTDA<V> {
    void inicializar();
    boolean agregar(int clave, V valor);
    boolean eliminar(int clave);
    V recuperar(int clave);
    boolean existeClave(int clave);
    boolean diccionarioVacio();
    int tamanio();
}
