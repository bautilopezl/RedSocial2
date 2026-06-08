package src.sistema;

import src.tda.Lista;

// JERARQUÍA DE HABILIDADES
public class JerarquiaHabilidades {

    private class CategoriaHabilidad {
        String nombre;
        Lista<CategoriaHabilidad> hijos;

        CategoriaHabilidad(String nombre) {
            this.nombre = nombre;
            this.hijos = new Lista<>();
        }

        boolean esHoja() {
            return hijos.estaVacia();
        }
    }

    private CategoriaHabilidad raiz;

    public JerarquiaHabilidades(String nombreRaiz) {
        this.raiz = new CategoriaHabilidad(nombreRaiz);
    }

    public boolean insertar(String nombrePadre, String nombreHijo) {
        CategoriaHabilidad padre = buscarNodo(raiz, nombrePadre);
        if (padre == null) {
            System.out.println("Categoria no encontrada: " + nombrePadre);
            return false;
        }
        padre.hijos.agregar(new CategoriaHabilidad(nombreHijo));
        return true;
    }

    private CategoriaHabilidad buscarNodo(CategoriaHabilidad nodo, String nombre) {
        if (nodo == null) return null;
        if (nodo.nombre.equalsIgnoreCase(nombre)) return nodo;

        for (int i = 0; i < nodo.hijos.tamanio(); i++) {
            CategoriaHabilidad resultado = buscarNodo(nodo.hijos.obtener(i), nombre);
            if (resultado != null) return resultado;
        }
        return null;
    }

    public Lista<String> buscarPorCategoria(String categoria) {
        Lista<String> resultado = new Lista<>();
        CategoriaHabilidad nodo = buscarNodo(raiz, categoria);
        if (nodo == null) {
            System.out.println("Categoria no encontrada: " + categoria);
            return resultado;
        }
        recolectarHojas(nodo, resultado);
        return resultado;
    }

    private void recolectarHojas(CategoriaHabilidad nodo, Lista<String> resultado) {
        if (nodo.esHoja()) {
            resultado.agregar(nodo.nombre);
            return;
        }
        for (int i = 0; i < nodo.hijos.tamanio(); i++) {
            recolectarHojas(nodo.hijos.obtener(i), resultado);
        }
    }

    public void mostrar() {
        System.out.println("── Jerarquia de Habilidades ──");
        mostrarRecursivo(raiz, 0);
        System.out.println();
    }

    private void mostrarRecursivo(CategoriaHabilidad nodo, int nivel) {
        if (nodo == null) return;

        String indentacion = "";
        for (int i = 0; i < nivel; i++) {
            indentacion += "    ";
        }

        String prefijo = nivel == 0 ? "" : "└── ";
        System.out.println(indentacion + prefijo + nodo.nombre);

        for (int i = 0; i < nodo.hijos.tamanio(); i++) {
            mostrarRecursivo(nodo.hijos.obtener(i), nivel + 1);
        }
    }
}
