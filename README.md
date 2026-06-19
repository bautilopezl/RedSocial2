# RedSocial — Red Social Profesional

Sistema de red social profesional desarrollado íntegramente en Java Swing con estructuras de datos implementadas desde cero. Proyecto final de Programación II.

---

## Stack

| Capa | Tecnología |
|---|---|
| UI | Java Swing (pintado personalizado con antialiasing) |
| Lógica de negocio | Java puro (sin frameworks) |
| Estructuras de datos | Implementación manual (ABB, grafo, cola, pila, árbol n-ario) |
| Persistencia | Jackson 2.17.0 — JSON |
| Build | `javac` + `java` (sin Maven/Gradle) |

---

## Arquitectura en capas

```
UI (Swing)
    ↕
Controladores
    ↕
Servicios / Gestores
    ↕
Implementaciones (estructuras de datos)
    ↕
TDA (interfaces abstractas)
    ↕
Modelo (entidades de dominio)
    ↕
Persistencia (Jackson JSON)
```

Cada capa se comunica únicamente con la capa inmediata inferior. Los controladores orquestan múltiples servicios; los servicios operan sobre los TDA y coordinan persistencia.

---

## Estructuras de datos

| TDA (interfaz) | Implementación | Propósito |
|---|---|---|
| `DiccionarioUsuariosTDA<K,V>` | `DiccionarioABB` — Árbol Binario de Búsqueda genérico | Almacenar usuarios, empleadores y ofertas laborales |
| `GrafoContactosTDA` | `GrafoContactos` — Lista de adyacencia enlazada | Gestionar conexiones entre usuarios (BFS para grado de separación) |
| `ColaPostulacionesTDA<T>` | `ColaPostulaciones` — Cola FIFO enlazada | Procesar postulaciones laborales en orden de llegada |
| `PilaHistorialTDA<T>` | `PilaHistorial` — Pila LIFO enlazada | Historial de cambios de perfil (deshacer) |
| `ArbolHabilidadesTDA` | `ArbolHabilidades` — Árbol n-ario (hijo-izquierdo/hermano-derecho) | Jerarquía de categorías y habilidades |

---

## Modelo de dominio

| Entidad | Campos clave |
|---|---|
| `Usuario` | id, nombre, email, profesión, descripción, password, habilidades[] |
| `Empleador` | id, nombreEmpresa, email, rubro, descripción, password |
| `OfertaLaboral` | id, título, descripción, empleador, activa |
| `Postulacion` | usuario, oferta |
| `EstadoPerfil` | Snapshot inmutable de Usuario (para historial) |

---

## Funcionalidades por rol

### Profesional

| Vista | Funcionalidad |
|---|---|
| **Inicio** | Ofertas activas recientes, mini-perfil, sugerencias de contactos |
| **Mi Perfil** | Ver perfil con árbol jerárquico de habilidades. Editar nombre, email, profesión, descripción y marcar/desmarcar habilidades en árbol con checkboxes |
| **Mi Red** | Búsqueda por nombre/email/habilidad, solicitudes de contacto (aceptar/rechazar), contactos directos (eliminar), sugerencias BFS (grado ≤ 3), sugerencias por skills al editar perfil |
| **Buscar Empleos** | Filtrar ofertas por título/empresa, postularse |
| **Mis Postulaciones** | Lista de postulaciones enviadas |

### Empresa

| Vista | Funcionalidad |
|---|---|
| **Dashboard** | Resumen corporativo, acciones rápidas |
| **Mis Ofertas** | CRUD completo: crear, editar, cerrar, eliminar ofertas |
| **Postulaciones** | Cola FIFO de postulaciones con datos del candidato (skills), aceptar/rechazar, buscar profesionales |

---

## Flujo de datos (ejemplo: postulación a oferta)

```
1. EmpleosPanel (UI) → clic en "Postularme"
2. PostulacionController.postularUsuario(userId, ofertaId)
3. GestorPostulaciones.postularUsuario(...)
   a. Busca Usuario en GestorUsuarios (DiccionarioABB)
   b. Busca OfertaLaboral en su DiccionarioABB interno
   c. Verifica duplicado
   d. Crea Postulacion y la acola en ColaPostulaciones
   e. Persiste a JSON vía RepositorioPostulaciones
4. Retorna boolean → UI muestra resultado
```

---

## Roles y navegación

El `RoleLayoutManager` orquesta sidebar y topbar según el rol activo:

```
GUEST → solo Login visible
PROFESIONAL → Inicio · Mi Perfil · Mi Red · Buscar Empleos · Mis Postulaciones · Configuración
EMPRESA → Dashboard · Mis Ofertas · Postulaciones · Configuración
```

---

## Persistencia

Jackson 2.17.0 mapea todas las entidades a JSON en `resources/data/`:

| Archivo | Contenido |
|---|---|
| `usuarios.json` | Lista de Usuario |
| `empresas.json` | Lista de Empleador |
| `ofertas.json` | Lista de OfertaLaboral |
| `postulaciones.json` | Lista de Postulacion |
| `contactos.json` | Pares [id1, id2] de conexiones |
| `sequences.json` | Contadores para IDs auto-incrementales |

---

## Compilación y ejecución

```powershell
# Limpiar y compilar
Remove-Item -Recurse -Force out
javac -d out -sourcepath src -cp "lib\jackson-core-2.17.0.jar;lib\jackson-databind-2.17.0.jar;lib\jackson-annotations-2.17.0.jar" src\Main.java

# Ejecutar
java -cp "out;lib\*" Main
```

---

## Resumen del proyecto

| Métrica | Valor |
|---|---|
| Archivos Java | 71 |
| Líneas de código | ~7.300 |
| Paquetes | 9 (`tda`, `modelo`, `implementaciones`, `servicios`, `controladores`, `persistencia`, `ui`, `ui.components`, `ui.views`) |
| Componentes UI reutilizables | 14 |
| Dependencias externas | Jackson (3 JARs) |
