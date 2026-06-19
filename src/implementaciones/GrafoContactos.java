package implementaciones;

import modelo.Usuario;
import tda.GrafoContactosTDA;

public class GrafoContactos implements GrafoContactosTDA {
    private NodoGrafo primerVertice;
    private int cantidadUsuarios;

    public GrafoContactos() {
        inicializar();
    }

    @Override
    public void inicializar() {
        primerVertice = null;
        cantidadUsuarios = 0;
    }

    @Override
    public boolean agregarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId() < 0 || existeUsuario(usuario.getId())) {
            return false;
        }
        NodoGrafo nuevo = new NodoGrafo(usuario);
        nuevo.setSiguiente(primerVertice);
        primerVertice = nuevo;
        cantidadUsuarios++;
        return true;
    }

    @Override
    public boolean eliminarUsuario(int idUsuario) {
        if (idUsuario < 0 || primerVertice == null) {
            return false;
        }
        NodoGrafo objetivo = buscarVertice(idUsuario);
        if (objetivo == null) {
            return false;
        }
        NodoGrafo actual = primerVertice;
        while (actual != null) {
            if (actual.getIdUsuario() != idUsuario) {
                eliminarContactoDeLista(actual, idUsuario);
            }
            actual = actual.getSiguiente();
        }
        if (primerVertice.getIdUsuario() == idUsuario) {
            primerVertice = primerVertice.getSiguiente();
        } else {
            NodoGrafo anterior = primerVertice;
            while (anterior.getSiguiente() != null && anterior.getSiguiente().getIdUsuario() != idUsuario) {
                anterior = anterior.getSiguiente();
            }
            if (anterior.getSiguiente() != null) {
                anterior.setSiguiente(anterior.getSiguiente().getSiguiente());
            }
        }
        cantidadUsuarios--;
        return true;
    }

    @Override
    public boolean agregarConexion(int idUsuario1, int idUsuario2) {
        if (idUsuario1 < 0 || idUsuario2 < 0 || idUsuario1 == idUsuario2) {
            return false;
        }
        NodoGrafo usuario1 = buscarVertice(idUsuario1);
        NodoGrafo usuario2 = buscarVertice(idUsuario2);
        if (usuario1 == null || usuario2 == null || sonContactos(idUsuario1, idUsuario2)) {
            return false;
        }
        agregarContacto(usuario1, usuario2);
        agregarContacto(usuario2, usuario1);
        return true;
    }

    @Override
    public boolean eliminarConexion(int idUsuario1, int idUsuario2) {
        if (idUsuario1 < 0 || idUsuario2 < 0) {
            return false;
        }
        NodoGrafo usuario1 = buscarVertice(idUsuario1);
        NodoGrafo usuario2 = buscarVertice(idUsuario2);
        if (usuario1 == null || usuario2 == null) {
            return false;
        }
        boolean eliminado1 = eliminarContactoDeLista(usuario1, idUsuario2);
        boolean eliminado2 = eliminarContactoDeLista(usuario2, idUsuario1);
        return eliminado1 && eliminado2;
    }

    @Override
    public boolean sonContactos(int idUsuario1, int idUsuario2) {
        if (idUsuario1 < 0 || idUsuario2 < 0) {
            return false;
        }
        NodoGrafo usuario1 = buscarVertice(idUsuario1);
        if (usuario1 == null) {
            return false;
        }
        NodoGrafo contacto = usuario1.getSiguienteContacto();
        while (contacto != null) {
            if (contacto.getIdUsuario() == idUsuario2) {
                return true;
            }
            contacto = contacto.getSiguienteContacto();
        }
        return false;
    }

    @Override
    public int gradoSeparacion(int idUsuario1, int idUsuario2) {
        if (idUsuario1 < 0 || idUsuario2 < 0) {
            return -1;
        }
        if (idUsuario1 == idUsuario2) {
            return existeUsuario(idUsuario1) ? 0 : -1;
        }
        if (!existeUsuario(idUsuario1) || !existeUsuario(idUsuario2)) {
            return -1;
        }

        ColaPostulaciones<Integer> cola = new ColaPostulaciones<>();
        int[] visitados = new int[cantidadUsuarios];
        int cantidadVisitados = 0;
        cola.acolar(idUsuario1);
        visitados[cantidadVisitados] = idUsuario1;
        cantidadVisitados++;
        int distancia = 0;

        while (!cola.colaVacia()) {
            int elementosNivel = cola.tamanio();
            for (int i = 0; i < elementosNivel; i++) {
                int actualId = cola.desacolar();
                if (actualId == idUsuario2) {
                    return distancia;
                }
                NodoGrafo actual = buscarVertice(actualId);
                if (actual == null) {
                    continue;
                }
                NodoGrafo contacto = actual.getSiguienteContacto();
                while (contacto != null) {
                    int vecinoId = contacto.getIdUsuario();
                    if (!estaVisitado(visitados, cantidadVisitados, vecinoId)) {
                        if (cantidadVisitados < visitados.length) {
                            visitados[cantidadVisitados] = vecinoId;
                            cantidadVisitados++;
                        }
                        cola.acolar(vecinoId);
                    }
                    contacto = contacto.getSiguienteContacto();
                }
            }
            distancia++;
        }
        return -1;
    }

    @Override
    public Usuario recuperarUsuario(int idUsuario) {
        NodoGrafo nodo = buscarVertice(idUsuario);
        if (nodo == null) {
            return null;
        }
        return nodo.getUsuario();
    }

    @Override
    public boolean existeUsuario(int idUsuario) {
        return buscarVertice(idUsuario) != null;
    }

    @Override
    public int[] obtenerContactosDirectos(int idUsuario) {
        NodoGrafo nodo = buscarVertice(idUsuario);
        if (nodo == null) {
            return new int[0];
        }
        int cantidad = 0;
        NodoGrafo contacto = nodo.getSiguienteContacto();
        while (contacto != null) {
            cantidad++;
            contacto = contacto.getSiguienteContacto();
        }
        int[] contactos = new int[cantidad];
        int indice = 0;
        contacto = nodo.getSiguienteContacto();
        while (contacto != null) {
            contactos[indice] = contacto.getIdUsuario();
            indice++;
            contacto = contacto.getSiguienteContacto();
        }
        return contactos;
    }

    @Override
    public int[] obtenerIdsUsuarios() {
        int[] ids = new int[cantidadUsuarios];
        NodoGrafo actual = primerVertice;
        int indice = 0;
        while (actual != null) {
            ids[indice] = actual.getIdUsuario();
            indice++;
            actual = actual.getSiguiente();
        }
        return ids;
    }

    public int cantidadUsuarios() {
        return cantidadUsuarios;
    }

    private NodoGrafo buscarVertice(int idUsuario) {
        NodoGrafo actual = primerVertice;
        while (actual != null) {
            if (actual.getIdUsuario() == idUsuario) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    private void agregarContacto(NodoGrafo origen, NodoGrafo destino) {
        NodoGrafo nuevo = new NodoGrafo(destino.getUsuario());
        nuevo.setSiguienteContacto(origen.getSiguienteContacto());
        origen.setSiguienteContacto(nuevo);
    }

    private boolean eliminarContactoDeLista(NodoGrafo origen, int idDestino) {
        NodoGrafo actual = origen.getSiguienteContacto();
        NodoGrafo anterior = null;
        while (actual != null) {
            if (actual.getIdUsuario() == idDestino) {
                if (anterior == null) {
                    origen.setSiguienteContacto(actual.getSiguienteContacto());
                } else {
                    anterior.setSiguienteContacto(actual.getSiguienteContacto());
                }
                return true;
            }
            anterior = actual;
            actual = actual.getSiguienteContacto();
        }
        return false;
    }

    private boolean estaVisitado(int[] visitados, int cantidadVisitados, int idUsuario) {
        for (int i = 0; i < cantidadVisitados; i++) {
            if (visitados[i] == idUsuario) {
                return true;
            }
        }
        return false;
    }
}
