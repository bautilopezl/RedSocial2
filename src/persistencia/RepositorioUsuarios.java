package persistencia;

import com.fasterxml.jackson.core.type.TypeReference;
import modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuarios {
    private static final String ARCHIVO = "usuarios.json";
    private final JsonManager jsonManager;

    public RepositorioUsuarios(JsonManager jsonManager) {
        this.jsonManager = jsonManager;
    }

    public List<Usuario> cargarTodos() {
        List<Usuario> usuarios = jsonManager.leer(ARCHIVO, new TypeReference<List<Usuario>>() {});
        return usuarios != null ? usuarios : new ArrayList<>();
    }

    public void guardarTodos(List<Usuario> usuarios) {
        jsonManager.guardar(ARCHIVO, usuarios);
    }
}
