package controladores;

import servicios.GestorContactos;

public class ContactoController {
    private final GestorContactos gestorContactos;
    private final java.util.Map<Integer, java.util.List<Integer>> solicitudesPendientes;

    public ContactoController(GestorContactos gestorContactos) {
        this.gestorContactos = gestorContactos;
        this.solicitudesPendientes = new java.util.HashMap<>();
    }

    public boolean agregarContacto(int id1, int id2) {
        if (id1 < 0 || id2 < 0 || id1 == id2) return false;
        return gestorContactos.agregarConexion(id1, id2);
    }

    public boolean eliminarContacto(int id1, int id2) {
        if (id1 < 0 || id2 < 0) return false;
        return gestorContactos.eliminarConexion(id1, id2);
    }

    public boolean enviarSolicitudContacto(int de, int para) {
        if (de < 0 || para < 0 || de == para) return false;
        if (gestorContactos.sonContactos(de, para)) return false;
        if (solicitudExistente(de, para)) return false;

        solicitudesPendientes.putIfAbsent(para, new java.util.ArrayList<>());
        solicitudesPendientes.get(para).add(de);
        return true;
    }

    public int[] obtenerSolicitudesContacto(int usuario) {
        if (usuario < 0) return new int[0];
        java.util.List<Integer> solicitudes = solicitudesPendientes.get(usuario);
        if (solicitudes == null || solicitudes.isEmpty()) return new int[0];
        int[] resultado = new int[solicitudes.size()];
        for (int i = 0; i < solicitudes.size(); i++) {
            resultado[i] = solicitudes.get(i);
        }
        return resultado;
    }

    public boolean aceptarSolicitudContacto(int de, int para) {
        if (de < 0 || para < 0) return false;
        java.util.List<Integer> solicitudes = solicitudesPendientes.get(para);
        if (solicitudes == null || !solicitudes.contains(de)) return false;
        solicitudes.remove((Integer) de);
        return gestorContactos.agregarConexion(de, para);
    }

    public boolean rechazarSolicitudContacto(int de, int para) {
        if (de < 0 || para < 0) return false;
        java.util.List<Integer> solicitudes = solicitudesPendientes.get(para);
        if (solicitudes == null || !solicitudes.contains(de)) return false;
        solicitudes.remove((Integer) de);
        return true;
    }

    public boolean sonContactos(int id1, int id2) {
        if (id1 < 0 || id2 < 0) return false;
        return gestorContactos.sonContactos(id1, id2);
    }

    public int verGradoSeparacion(int id1, int id2) {
        if (id1 < 0 || id2 < 0) return -1;
        return gestorContactos.gradoSeparacion(id1, id2);
    }

    public int[] sugerirContactos(int idUsuario) {
        if (idUsuario < 0) return new int[0];
        return gestorContactos.sugerirContactos(idUsuario);
    }

    public int[] obtenerContactosDirectos(int idUsuario) {
        if (idUsuario < 0) return new int[0];
        return gestorContactos.obtenerContactosDirectos(idUsuario);
    }

    private boolean solicitudExistente(int de, int para) {
        java.util.List<Integer> solicitudes = solicitudesPendientes.get(para);
        return solicitudes != null && solicitudes.contains(de);
    }
}
