package implementaciones;

import tda.ArbolHabilidadesTDA;

public class ArbolHabilidades implements ArbolHabilidadesTDA {
    private NodoArbol raiz;

    public ArbolHabilidades() {
        raiz = null;
    }

    @Override
    public void inicializar(String categoriaRaiz) {
        String raizLimpia = limpiar(categoriaRaiz);
        if (raizLimpia.isEmpty()) {
            raiz = null;
            return;
        }
        raiz = new NodoArbol(raizLimpia);
    }

    @Override
    public boolean agregarCategoria(String categoriaPadre, String nuevaCategoria) {
        String padreLimpio = limpiar(categoriaPadre);
        String nuevaLimpia = limpiar(nuevaCategoria);
        if (nuevaLimpia.isEmpty()) {
            return false;
        }
        if (raiz == null) {
            if (!padreLimpio.isEmpty()) {
                return false;
            }
            raiz = new NodoArbol(nuevaLimpia);
            return true;
        }
        if (buscarCategoria(nuevaLimpia)) {
            return false;
        }
        NodoArbol padre = buscarNodo(raiz, padreLimpio);
        if (padre == null) {
            return false;
        }
        NodoArbol nuevo = new NodoArbol(nuevaLimpia);
        if (padre.getHijoIzquierdo() == null) {
            padre.setHijoIzquierdo(nuevo);
            return true;
        }
        NodoArbol actual = padre.getHijoIzquierdo();
        while (actual.getHermanoDerecho() != null) {
            actual = actual.getHermanoDerecho();
        }
        actual.setHermanoDerecho(nuevo);
        return true;
    }

    @Override
    public boolean buscarCategoria(String categoria) {
        if (raiz == null) {
            return false;
        }
        return buscarNodo(raiz, limpiar(categoria)) != null;
    }

    @Override
    public void mostrarJerarquia() {
        if (raiz == null) {
            System.out.println("No hay categorías registradas.");
            return;
        }
        System.out.println(obtenerJerarquiaTexto());
    }

    /**
     * Devuelve la jerarquía completa en formato de texto con sangrías.
     */
    public String obtenerJerarquiaTexto() {
        if (raiz == null) {
            return "No hay categorías registradas.";
        }
        StringBuilder sb = new StringBuilder();
        llenarJerarquiaTexto(raiz, 0, sb);
        return sb.toString();
    }

    private void llenarJerarquiaTexto(NodoArbol nodo, int nivel, StringBuilder sb) {
        if (nodo == null) {
            return;
        }
        for (int i = 0; i < nivel; i++) {
            sb.append("  ");
        }
        sb.append("- ").append(nodo.getCategoria()).append(System.lineSeparator());
        llenarJerarquiaTexto(nodo.getHijoIzquierdo(), nivel + 1, sb);
        llenarJerarquiaTexto(nodo.getHermanoDerecho(), nivel, sb);
    }

    public String[] obtenerCategorias() {
        int cantidad = contarNodos(raiz);
        String[] categorias = new String[cantidad];
        llenarCategorias(raiz, categorias, new int[]{0});
        return categorias;
    }

    private NodoArbol buscarNodo(NodoArbol nodo, String categoria) {
        if (nodo == null || categoria.isEmpty()) {
            return null;
        }
        if (nodo.getCategoria().equalsIgnoreCase(categoria)) {
            return nodo;
        }
        NodoArbol encontrado = buscarNodo(nodo.getHijoIzquierdo(), categoria);
        if (encontrado != null) {
            return encontrado;
        }
        return buscarNodo(nodo.getHermanoDerecho(), categoria);
    }

    private void mostrarRecursivo(NodoArbol nodo, int nivel) {
        if (nodo == null) {
            return;
        }
        for (int i = 0; i < nivel; i++) {
            System.out.print("  ");
        }
        System.out.println("- " + nodo.getCategoria());
        mostrarRecursivo(nodo.getHijoIzquierdo(), nivel + 1);
        mostrarRecursivo(nodo.getHermanoDerecho(), nivel);
    }

    private int contarNodos(NodoArbol nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarNodos(nodo.getHijoIzquierdo()) + contarNodos(nodo.getHermanoDerecho());
    }

    private void llenarCategorias(NodoArbol nodo, String[] categorias, int[] indice) {
        if (nodo == null) {
            return;
        }
        categorias[indice[0]] = nodo.getCategoria();
        indice[0]++;
        llenarCategorias(nodo.getHijoIzquierdo(), categorias, indice);
        llenarCategorias(nodo.getHermanoDerecho(), categorias, indice);
    }

    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }
}

