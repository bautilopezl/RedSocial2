package persistencia;

import com.fasterxml.jackson.core.type.TypeReference;
import modelo.Postulacion;

import java.util.ArrayList;
import java.util.List;

public class RepositorioPostulaciones {
    private static final String ARCHIVO = "postulaciones.json";
    private final JsonManager jsonManager;

    public RepositorioPostulaciones(JsonManager jsonManager) {
        this.jsonManager = jsonManager;
    }

    public List<Postulacion> cargarTodas() {
        List<Postulacion> postulaciones = jsonManager.leer(ARCHIVO, new TypeReference<List<Postulacion>>() {});
        return postulaciones != null ? postulaciones : new ArrayList<>();
    }

    public void guardarTodas(List<Postulacion> postulaciones) {
        jsonManager.guardar(ARCHIVO, postulaciones);
    }
}
