package persistencia;

public class SequenceRepository {
    private static final String ARCHIVO = "sequences.json";
    private final JsonManager jsonManager;

    public SequenceRepository(JsonManager jsonManager) {
        this.jsonManager = jsonManager;
    }

    public int siguienteUsuario() {
        long next = jsonManager.leerSecuencia(ARCHIVO, "usuarios") + 1;
        jsonManager.guardarSecuencia(ARCHIVO, "usuarios", next);
        return (int) next;
    }

    public int siguienteEmpresa() {
        long next = jsonManager.leerSecuencia(ARCHIVO, "empresas") + 1;
        jsonManager.guardarSecuencia(ARCHIVO, "empresas", next);
        return (int) next;
    }

    public int siguienteOferta() {
        long next = jsonManager.leerSecuencia(ARCHIVO, "ofertas") + 1;
        jsonManager.guardarSecuencia(ARCHIVO, "ofertas", next);
        return (int) next;
    }
}
