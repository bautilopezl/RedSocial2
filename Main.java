import src.modelo.Postulacion;
import src.modelo.Usuario;
import src.sistema.GestorUsuarios;
import src.sistema.JerarquiaHabilidades;
import src.sistema.RedContactos;
import src.tda.Lista;

public class Main {

    public static void main(String[] args) {

        System.out.println("===========================================");
        System.out.println("   RED SOCIAL PROFESIONAL - ETAPA 2");
        System.out.println("===========================================\n");

        //1. GESTOR DE USUARIOS
        GestorUsuarios gestor = new GestorUsuarios();

        Usuario ana    = new Usuario("U001", "Ana Lopez",    "ana@mail.com",    "Java, Python");
        Usuario carlos = new Usuario("U002", "Carlos Ruiz",  "carlos@mail.com", "Docker, Kubernetes");
        Usuario maria  = new Usuario("U003", "Maria Gomez",  "maria@mail.com",  "SEO, Marketing");
        Usuario juan   = new Usuario("U004", "Juan Perez",   "juan@mail.com",   "Java, Spring Boot");
        Usuario laura  = new Usuario("U005", "Laura Diaz",   "laura@mail.com",  "Python, Data Science");
        Usuario pedro  = new Usuario("U006", "Pedro Sanchez","pedro@mail.com",  "Kubernetes, Docker");

        gestor.registrar(ana);
        gestor.registrar(carlos);
        gestor.registrar(maria);
        gestor.registrar(juan);
        gestor.registrar(laura);
        gestor.registrar(pedro);

        System.out.println();
        gestor.mostrarTodos();

        // Busqueda por ID en O(1)
        System.out.println("\n── Busqueda por ID ──");
        System.out.println(gestor.buscar("U003"));

        //2. RED DE CONTACTOS
        System.out.println("\n===========================================");
        System.out.println(" RED DE CONTACTOS");
        System.out.println("===========================================\n");

        RedContactos red = new RedContactos(gestor);

        red.agregarUsuario("U001");
        red.agregarUsuario("U002");
        red.agregarUsuario("U003");
        red.agregarUsuario("U004");
        red.agregarUsuario("U005");
        red.agregarUsuario("U006");

        red.conectar("U001", "U002");
        red.conectar("U001", "U003");
        red.conectar("U002", "U004");
        red.conectar("U003", "U004");
        red.conectar("U003", "U005");
        red.conectar("U004", "U006");

        System.out.println();

        red.dfs("U001");

        red.bfs("U001");

        red.bfs("U006");

        // Grado de separacion
        System.out.println("── Grado de separacion ──");
        System.out.println("Ana - Pedro: " + red.gradoSeparacion("U001", "U006") + " pasos");
        System.out.println("Ana - Laura: " + red.gradoSeparacion("U001", "U005") + " pasos");
        System.out.println("Carlos - Laura: " + red.gradoSeparacion("U002", "U005") + " pasos");
        System.out.println();

        red.recomendarAmigos("U001");
        red.recomendarAmigos("U002");

        //3. JERARQUÍA DE HABILIDADES
        System.out.println("===========================================");
        System.out.println(" JERARQUIA DE HABILIDADES");
        System.out.println("===========================================\n");

        JerarquiaHabilidades arbol = new JerarquiaHabilidades("Habilidades");

        arbol.insertar("Habilidades", "Tecnologia");
        arbol.insertar("Tecnologia", "Desarrollo");
        arbol.insertar("Desarrollo", "Java");
        arbol.insertar("Desarrollo", "Python");
        arbol.insertar("Desarrollo", "Spring Boot");
        arbol.insertar("Tecnologia", "Infraestructura");
        arbol.insertar("Infraestructura", "Docker");
        arbol.insertar("Infraestructura", "Kubernetes");
        arbol.insertar("Habilidades", "Marketing");
        arbol.insertar("Marketing", "SEO");
        arbol.insertar("Marketing", "Redes Sociales");
        arbol.insertar("Habilidades", "Data");
        arbol.insertar("Data", "Data Science");
        arbol.insertar("Data", "Machine Learning");

        arbol.mostrar();

        // Buscar habilidades por categoría
        System.out.println("── Habilidades de Desarrollo ──");
        Lista<String> habilidadesDesarrollo = arbol.buscarPorCategoria("Desarrollo");
        for (int i = 0; i < habilidadesDesarrollo.tamanio(); i++) {
            System.out.println("  - " + habilidadesDesarrollo.obtener(i));
        }

        System.out.println("\n── Habilidades de Tecnologia ──");
        Lista<String> habilidadesTec = arbol.buscarPorCategoria("Tecnologia");
        for (int i = 0; i < habilidadesTec.tamanio(); i++) {
            System.out.println("  - " + habilidadesTec.obtener(i));
        }

        //4. POSTULACIONES
        System.out.println("\n===========================================");
        System.out.println(" POSTULACIONES");
        System.out.println("===========================================\n");

        ana.postularse(new Postulacion("U001", "Backend Dev", "2026-06-08"));
        ana.postularse(new Postulacion("U001", "Full Stack Dev", "2026-06-08"));
        carlos.postularse(new Postulacion("U002", "DevOps Engineer", "2026-06-08"));

        System.out.println("\nPostulaciones pendientes de Ana: " + ana.cantidadPostulaciones());
        ana.procesarPostulacion();
        ana.procesarPostulacion();
        ana.procesarPostulacion(); // cola vacia

        //5. HISTORIAL
        System.out.println("\n===========================================");
        System.out.println(" HISTORIAL DE CAMBIOS");
        System.out.println("===========================================\n");

        System.out.println("Perfil original de Maria: " + maria);
        maria.editarPerfil("Maria Gomez", "maria.nueva@mail.com", "SEO, Marketing, Analytics");
        maria.editarPerfil("Maria G.", "mg@mail.com", "SEO");
        System.out.println("\nDeshaciendo cambios...");
        maria.deshacerCambio();
        maria.deshacerCambio();
        maria.deshacerCambio(); // pila vacia
    }
}
