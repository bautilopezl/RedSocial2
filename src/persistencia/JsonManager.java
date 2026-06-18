package persistencia;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonManager {
    private static final String RESOURCES_PATH = "resources/data/";
    private final ObjectMapper mapper;

    public JsonManager() {
        this.mapper = JsonMapper.builder()
            .findAndAddModules()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        asegurarDirectorio();
    }

    private void asegurarDirectorio() {
        try {
            Files.createDirectories(Path.of(RESOURCES_PATH));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio resources/data", e);
        }
    }

    public <T> T leer(String archivo, Class<T> clase) {
        File file = new File(RESOURCES_PATH + archivo);
        if (!file.exists()) {
            return crearVacio(archivo, clase);
        }
        try {
            return mapper.readValue(file, clase);
        } catch (IOException e) {
            System.err.println("Error leyendo " + archivo + ": " + e.getMessage());
            return crearVacio(archivo, clase);
        }
    }

    public <T> T leer(String archivo, TypeReference<T> typeRef) {
        File file = new File(RESOURCES_PATH + archivo);
        if (!file.exists()) {
            try {
                T vacio = mapper.readValue("[]", typeRef);
                guardar(archivo, vacio);
                return vacio;
            } catch (IOException e) {
                return null;
            }
        }
        try {
            return mapper.readValue(file, typeRef);
        } catch (IOException e) {
            System.err.println("Error leyendo " + archivo + ": " + e.getMessage());
            try {
                T vacio = mapper.readValue("[]", typeRef);
                guardar(archivo, vacio);
                return vacio;
            } catch (IOException ex) {
                return null;
            }
        }
    }

    public <T> void guardar(String archivo, T datos) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(RESOURCES_PATH + archivo), datos);
        } catch (IOException e) {
            System.err.println("Error guardando " + archivo + ": " + e.getMessage());
        }
    }

    public long leerSecuencia(String archivo, String clave) {
        try {
            File file = new File(RESOURCES_PATH + archivo);
            if (!file.exists()) {
                return 0;
            }
            ObjectNode node = mapper.readValue(file, ObjectNode.class);
            return node.has(clave) ? node.get(clave).asLong() : 0;
        } catch (IOException e) {
            return 0;
        }
    }

    public void guardarSecuencia(String archivo, String clave, long valor) {
        try {
            File file = new File(RESOURCES_PATH + archivo);
            ObjectNode node;
            if (file.exists()) {
                node = mapper.readValue(file, ObjectNode.class);
            } else {
                node = mapper.createObjectNode();
            }
            node.put(clave, valor);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, node);
        } catch (IOException e) {
            System.err.println("Error guardando secuencia: " + e.getMessage());
        }
    }

    private <T> T crearVacio(String archivo, Class<T> clase) {
        try {
            return mapper.readValue("[]", clase);
        } catch (IOException e) {
            return null;
        }
    }
}
