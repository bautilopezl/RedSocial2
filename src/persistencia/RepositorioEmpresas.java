package persistencia;

import com.fasterxml.jackson.core.type.TypeReference;
import modelo.Empleador;

import java.util.ArrayList;
import java.util.List;

public class RepositorioEmpresas {
    private static final String ARCHIVO = "empresas.json";
    private final JsonManager jsonManager;

    public RepositorioEmpresas(JsonManager jsonManager) {
        this.jsonManager = jsonManager;
    }

    public List<Empleador> cargarTodos() {
        List<Empleador> empresas = jsonManager.leer(ARCHIVO, new TypeReference<List<Empleador>>() {});
        return empresas != null ? empresas : new ArrayList<>();
    }

    public void guardarTodos(List<Empleador> empresas) {
        jsonManager.guardar(ARCHIVO, empresas);
    }
}
