import implementaciones.DiccionarioABB;
import modelo.Usuario;
import ui.layout.MainFrame;
import javax.swing.SwingUtilities;
import controladores.*;
import servicios.*;
import persistencia.*;

public class Main {
    public static void main(String[] args) {
        JsonManager jsonManager = new JsonManager();
        SequenceRepository sequenceRepository = new SequenceRepository(jsonManager);

        RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios(jsonManager);
        RepositorioEmpresas repositorioEmpresas = new RepositorioEmpresas(jsonManager);
        RepositorioOfertas repositorioOfertas = new RepositorioOfertas(jsonManager);
        RepositorioPostulaciones repositorioPostulaciones = new RepositorioPostulaciones(jsonManager);
        RepositorioContactos repositorioContactos = new RepositorioContactos(jsonManager);

        GestorUsuarios gestorUsuarios = new GestorUsuarios(repositorioUsuarios, sequenceRepository);
        GestorContactos gestorContactos = new GestorContactos(repositorioContactos);
        gestorUsuarios.recorrerUsuarios(new DiccionarioABB.Visitante<Usuario>() {
            @Override
            public void visitar(int clave, Usuario valor) {
                if (valor != null) {
                    gestorContactos.registrarUsuario(valor);
                }
            }
        });
        gestorContactos.cargarConexiones();
        GestorEmpleadores gestorEmpleadores = new GestorEmpleadores(repositorioEmpresas, sequenceRepository);
        GestorPostulaciones gestorPostulaciones = new GestorPostulaciones(gestorUsuarios, gestorEmpleadores,
            repositorioOfertas, repositorioPostulaciones, sequenceRepository);
        GestorHabilidades gestorHabilidades = new GestorHabilidades(gestorUsuarios);
        gestorHabilidades.inicializar("Tecnología");
        seedSkills(gestorHabilidades);

        UsuarioController usuarioController = new UsuarioController(gestorUsuarios, gestorContactos);
        EmpleadorController empleadorController = new EmpleadorController(gestorEmpleadores);
        PostulacionController postulacionController = new PostulacionController(gestorPostulaciones, gestorEmpleadores);
        ContactoController contactoController = new ContactoController(gestorContactos);
        HabilidadController habilidadController = new HabilidadController(gestorHabilidades);

        MainFrame menu = new MainFrame(usuarioController, empleadorController, postulacionController, contactoController, habilidadController);
        SwingUtilities.invokeLater(() -> menu.setVisible(true));
    }

    private static void seedSkills(GestorHabilidades gh) {
        String[] cats = {"Programación", "Desarrollo Web", "Datos e IA", "Infraestructura", "Testing",
                         "Diseño", "Negocios", "Idiomas", "Habilidades Blandas"};
        for (String c : cats) {
            gh.agregarCategoria("Tecnología", c);
        }

        gh.agregarCategoria("Programación", "Java");
        gh.agregarCategoria("Programación", "Python");
        gh.agregarCategoria("Programación", "JavaScript");
        gh.agregarCategoria("Programación", "TypeScript");
        gh.agregarCategoria("Programación", "C#");
        gh.agregarCategoria("Programación", "SQL");
        gh.agregarCategoria("Programación", "Kotlin");
        gh.agregarCategoria("Programación", "Go");

        gh.agregarCategoria("Desarrollo Web", "HTML");
        gh.agregarCategoria("Desarrollo Web", "CSS");
        gh.agregarCategoria("Desarrollo Web", "React");
        gh.agregarCategoria("Desarrollo Web", "Angular");
        gh.agregarCategoria("Desarrollo Web", "Node.js");
        gh.agregarCategoria("Desarrollo Web", "Spring Boot");

        gh.agregarCategoria("Datos e IA", "Machine Learning");
        gh.agregarCategoria("Datos e IA", "Big Data");
        gh.agregarCategoria("Datos e IA", "Data Science");
        gh.agregarCategoria("Datos e IA", "Deep Learning");

        gh.agregarCategoria("Infraestructura", "Cloud (AWS/Azure/GCP)");
        gh.agregarCategoria("Infraestructura", "DevOps");
        gh.agregarCategoria("Infraestructura", "Docker");
        gh.agregarCategoria("Infraestructura", "Kubernetes");
        gh.agregarCategoria("Infraestructura", "Linux");

        gh.agregarCategoria("Testing", "Selenium");
        gh.agregarCategoria("Testing", "JUnit");
        gh.agregarCategoria("Testing", "Manual Testing");
        gh.agregarCategoria("Testing", "Cypress");

        gh.agregarCategoria("Diseño", "UX / UI");
        gh.agregarCategoria("Diseño", "Gráfico");
        gh.agregarCategoria("UX / UI", "Figma");
        gh.agregarCategoria("UX / UI", "Adobe XD");
        gh.agregarCategoria("UX / UI", "Sketch");
        gh.agregarCategoria("Gráfico", "Photoshop");
        gh.agregarCategoria("Gráfico", "Illustrator");
        gh.agregarCategoria("Gráfico", "After Effects");

        gh.agregarCategoria("Negocios", "Gestión de Proyectos");
        gh.agregarCategoria("Negocios", "Marketing");
        gh.agregarCategoria("Negocios", "Finanzas");
        gh.agregarCategoria("Gestión de Proyectos", "Agile");
        gh.agregarCategoria("Gestión de Proyectos", "Scrum");
        gh.agregarCategoria("Gestión de Proyectos", "PMP");
        gh.agregarCategoria("Gestión de Proyectos", "Kanban");
        gh.agregarCategoria("Marketing", "SEO");
        gh.agregarCategoria("Marketing", "SEM");
        gh.agregarCategoria("Marketing", "Content Marketing");
        gh.agregarCategoria("Marketing", "Redes Sociales");
        gh.agregarCategoria("Finanzas", "Contabilidad");
        gh.agregarCategoria("Finanzas", "Análisis Financiero");

        gh.agregarCategoria("Idiomas", "Español");
        gh.agregarCategoria("Idiomas", "Inglés");
        gh.agregarCategoria("Idiomas", "Portugués");
        gh.agregarCategoria("Idiomas", "Alemán");
        gh.agregarCategoria("Idiomas", "Francés");

        gh.agregarCategoria("Habilidades Blandas", "Liderazgo");
        gh.agregarCategoria("Habilidades Blandas", "Comunicación");
        gh.agregarCategoria("Habilidades Blandas", "Trabajo en Equipo");
        gh.agregarCategoria("Habilidades Blandas", "Resolución de Problemas");
        gh.agregarCategoria("Habilidades Blandas", "Adaptabilidad");
        gh.agregarCategoria("Habilidades Blandas", "Pensamiento Crítico");
    }
}
