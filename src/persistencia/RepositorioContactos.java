package persistencia;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;

public class RepositorioContactos {
    private static final String ARCHIVO = "contactos.json";
    private final JsonManager jsonManager;

    public RepositorioContactos(JsonManager jsonManager) {
        this.jsonManager = jsonManager;
    }

    public List<int[]> cargarTodos() {
        List<int[]> conexiones = jsonManager.leer(ARCHIVO, new TypeReference<List<int[]>>() {});
        return conexiones != null ? conexiones : new ArrayList<>();
    }

    public void guardarTodos(List<int[]> conexiones) {
        jsonManager.guardar(ARCHIVO, conexiones);
    }
}
