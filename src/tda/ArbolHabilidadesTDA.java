package tda;

public interface ArbolHabilidadesTDA {
    void inicializar(String categoriaRaiz);

    boolean agregarCategoria(String categoriaPadre, String nuevaCategoria);

    boolean buscarCategoria(String categoria);

    void mostrarJerarquia();
}

