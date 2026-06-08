package src.sistema;

import src.modelo.Usuario;
import src.tda.Conjunto;
import src.tda.Diccionario;

// RED DE CONTACTOS
public class RedContactos {

    private Diccionario<String, Conjunto<String>> adyacencia;
    private GestorUsuarios gestorUsuarios;

    public RedContactos(GestorUsuarios gestorUsuarios) {
        this.adyacencia = new Diccionario<>(100);
        this.gestorUsuarios = gestorUsuarios;
    }

    public void agregarUsuario(String id) {
        if (!adyacencia.contiene(id)) {
            adyacencia.agregar(id, new Conjunto<>());
        }
    }

    public void conectar(String id1, String id2) {
        if (!adyacencia.contiene(id1) || !adyacencia.contiene(id2)) {
            System.out.println("Uno de los usuarios no existe en la red.");
            return;
        }
        adyacencia.buscar(id1).agregar(id2);
        adyacencia.buscar(id2).agregar(id1);
        System.out.println("Conectados: " + id1 + " ↔ " + id2);
    }

    public void desconectar(String id1, String id2) {
        if (!adyacencia.contiene(id1) || !adyacencia.contiene(id2)) {
            System.out.println("Uno de los usuarios no existe en la red.");
            return;
        }
        adyacencia.buscar(id1).eliminar(id2);
        adyacencia.buscar(id2).eliminar(id1);
        System.out.println("Desconectados: " + id1 + " y " + id2);
    }

    public boolean sonContactos(String id1, String id2) {
        if (!adyacencia.contiene(id1)) return false;
        return adyacencia.buscar(id1).contiene(id2);
    }

    public void dfs(String idOrigen) {
        if (!adyacencia.contiene(idOrigen)) {
            System.out.println("El usuario no existe en la red.");
            return;
        }

        Conjunto<String> visitado = new Conjunto<>();
        System.out.println("── DFS desde " + nombreDeId(idOrigen) + " ──");
        System.out.println("Usuarios alcanzables:");
        dfsRecursivo(idOrigen, visitado);
        System.out.println();
    }

    private void dfsRecursivo(String id, Conjunto<String> visitado) {
        visitado.agregar(id);
        System.out.println("  Visitando: " + nombreDeId(id));

        Conjunto<String> vecinos = adyacencia.buscar(id);
        for (int i = 0; i < vecinos.tamanio(); i++) {
            String vecino = vecinos.obtener(i);
            if (!visitado.contiene(vecino)) {
                dfsRecursivo(vecino, visitado);
            }
        }
    }

    public void bfs(String idOrigen) {
        if (!adyacencia.contiene(idOrigen)) {
            System.out.println("El usuario no existe en la red.");
            return;
        }

        Conjunto<String> visitado = new Conjunto<>();
        String[] cola = new String[100];
        int[] nivel = new int[100];
        int inicio = 0;
        int fin = 0;

        cola[fin] = idOrigen;
        nivel[fin] = 0;
        visitado.agregar(idOrigen);
        fin++;

        System.out.println("── BFS desde " + nombreDeId(idOrigen) + " ──");

        while (inicio < fin) {
            String id = cola[inicio];
            int nivelActual = nivel[inicio];
            inicio++;

            if (nivelActual == 0) {
                System.out.println("Origen: " + nombreDeId(id));
            } else if (nivelActual == 1) {
                System.out.println("  Nivel 1 (amigo directo):  " + nombreDeId(id));
            } else if (nivelActual == 2) {
                System.out.println("  Nivel 2 (amigo de amigo): " + nombreDeId(id));
            } else {
                System.out.println("  Nivel " + nivelActual + ":               " + nombreDeId(id));
            }

            Conjunto<String> vecinos = adyacencia.buscar(id);
            for (int i = 0; i < vecinos.tamanio(); i++) {
                String vecino = vecinos.obtener(i);
                if (!visitado.contiene(vecino)) {
                    visitado.agregar(vecino);
                    cola[fin] = vecino;
                    nivel[fin] = nivelActual + 1;
                    fin++;
                }
            }
        }
        System.out.println();
    }

    public void recomendarAmigos(String idOrigen) {
        if (!adyacencia.contiene(idOrigen)) {
            System.out.println("El usuario no existe en la red.");
            return;
        }

        Conjunto<String> visitado = new Conjunto<>();
        String[] cola = new String[100];
        int[] nivel = new int[100];
        int inicio = 0;
        int fin = 0;

        cola[fin] = idOrigen;
        nivel[fin] = 0;
        visitado.agregar(idOrigen);
        fin++;

        System.out.println("── Recomendaciones para " + nombreDeId(idOrigen) + " ──");
        boolean hayRecomendaciones = false;

        while (inicio < fin) {
            String id = cola[inicio];
            int nivelActual = nivel[inicio];
            inicio++;

            if (nivelActual >= 2 && !sonContactos(idOrigen, id)) {
                System.out.println("  Recomendado: " + nombreDeId(id) +
                                   " (a " + nivelActual + " pasos)");
                hayRecomendaciones = true;
            }

            Conjunto<String> vecinos = adyacencia.buscar(id);
            for (int i = 0; i < vecinos.tamanio(); i++) {
                String vecino = vecinos.obtener(i);
                if (!visitado.contiene(vecino)) {
                    visitado.agregar(vecino);
                    cola[fin] = vecino;
                    nivel[fin] = nivelActual + 1;
                    fin++;
                }
            }
        }

        if (!hayRecomendaciones) {
            System.out.println("  No hay recomendaciones disponibles.");
        }
        System.out.println();
    }

    public int gradoSeparacion(String id1, String id2) {
        if (!adyacencia.contiene(id1) || !adyacencia.contiene(id2)) return -1;

        Conjunto<String> visitado = new Conjunto<>();
        String[] cola = new String[100];
        int[] nivel = new int[100];
        int inicio = 0;
        int fin = 0;

        cola[fin] = id1;
        nivel[fin] = 0;
        visitado.agregar(id1);
        fin++;

        while (inicio < fin) {
            String id = cola[inicio];
            int nivelActual = nivel[inicio];
            inicio++;

            if (id.equals(id2)) return nivelActual;

            Conjunto<String> vecinos = adyacencia.buscar(id);
            for (int i = 0; i < vecinos.tamanio(); i++) {
                String vecino = vecinos.obtener(i);
                if (!visitado.contiene(vecino)) {
                    visitado.agregar(vecino);
                    cola[fin] = vecino;
                    nivel[fin] = nivelActual + 1;
                    fin++;
                }
            }
        }
        return -1; // no están conectados
    }

    private String nombreDeId(String id) {
        Usuario u = gestorUsuarios.buscar(id);
        if (u == null) return id;
        return u.getNombre() + " (" + id + ")";
    }
}
