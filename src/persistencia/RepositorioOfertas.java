package persistencia;

import com.fasterxml.jackson.core.type.TypeReference;
import modelo.OfertaLaboral;

import java.util.ArrayList;
import java.util.List;

public class RepositorioOfertas {
    private static final String ARCHIVO = "ofertas.json";
    private final JsonManager jsonManager;

    public RepositorioOfertas(JsonManager jsonManager) {
        this.jsonManager = jsonManager;
    }

    public List<OfertaLaboral> cargarTodas() {
        List<OfertaLaboral> ofertas = jsonManager.leer(ARCHIVO, new TypeReference<List<OfertaLaboral>>() {});
        return ofertas != null ? ofertas : new ArrayList<>();
    }

    public void guardarTodas(List<OfertaLaboral> ofertas) {
        jsonManager.guardar(ARCHIVO, ofertas);
    }
}
