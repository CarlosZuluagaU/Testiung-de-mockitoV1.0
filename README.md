# Documentación del Proyecto — Testing con Mockito

---

## 1. Descripción general

Proyecto educativo en **Java** que demuestra cómo escribir pruebas unitarias usando **JUnit 5**, **Mockito** y el **patrón AAA (Arrange-Act-Assert)**. El dominio es un CRUD de jugadores de fútbol.

---

## 2. Estructura de paquetes

```
src/
├── main/java/org/mock/
│   ├── persistence/entity/
│   │   └── Player.java                  ← Entidad de dominio
│   ├── exception/
│   │   └── PlayerNotFoundException.java ← Excepción de negocio
│   ├── repository/
│   │   ├── IPlayerRepository.java       ← Contrato del repositorio
│   │   └── PlayerRepositoryImpl.java    ← Implementación en memoria
│   ├── service/
│   │   ├── IPlayerService.java          ← Contrato del servicio
│   │   └── PlayerServiceImpl.java       ← Implementación del servicio
│   └── Main.java
│
└── test/java/org/mock/
    ├── DataProvider.java                ← Datos de prueba (mocks estáticos)
    └── service/
        └── PlayerServiceImplTest.java   ← Suite completa de pruebas
```

---

## 3. Clases principales

### `Player` — `persistence/entity/Player.java`
Entidad de dominio que representa un jugador.

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `Long` | Identificador único |
| `name` | `String` | Nombre completo |
| `team` | `String` | Equipo actual |
| `position` | `String` | Posición en el campo |

Usa anotaciones Lombok: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`.

---

### `PlayerNotFoundException` — `exception/PlayerNotFoundException.java`
Extiende `RuntimeException`. Se lanza cuando no se encuentra un jugador por su `id`.

```java
throw new PlayerNotFoundException(99L);
// Mensaje: "Jugador con id 99 no encontrado."
```

---

### `IPlayerRepository` — `repository/IPlayerRepository.java`
Contrato que define las operaciones de persistencia.

| Método | Retorno | Descripción |
|---|---|---|
| `findAll()` | `List<Player>` | Retorna todos los jugadores |
| `findById(Long id)` | `Player` | Busca por id; lanza `PlayerNotFoundException` si no existe |
| `save(Player)` | `void` | Persiste un jugador nuevo |
| `update(Player)` | `void` | Actualiza un jugador existente |
| `deleteById(Long id)` | `void` | Elimina por id; lanza `PlayerNotFoundException` si no existe |
| `findByTeam(String)` | `List<Player>` | Filtra por equipo (case-insensitive) |
| `findByPosition(String)` | `List<Player>` | Filtra por posición (case-insensitive) |
| `existsById(Long id)` | `boolean` | Verifica si existe un jugador con ese id |

---

### `PlayerRepositoryImpl` — `repository/PlayerRepositoryImpl.java`
Implementación en memoria de `IPlayerRepository`. Usa una `ArrayList` de 44 jugadores precargados agrupados en 4 posiciones: Delanteros, Centrocampistas, Defensas y Porteros.

---

### `IPlayerService` / `PlayerServiceImpl` — `service/`
Capa de servicio que delega todas las operaciones al repositorio. `PlayerServiceImpl` recibe `IPlayerRepository` por constructor (inyección de dependencias), lo que permite reemplazarlo por un mock en las pruebas.

---

## 4. Pruebas — `PlayerServiceImplTest.java`

### Configuración base

```java
@ExtendWith(MockitoExtension.class)   // Integra Mockito con JUnit 5
class PlayerServiceImplTest {

    @Mock
    private IPlayerRepository playerRepository;  // Mock del repositorio

    @InjectMocks
    private PlayerServiceImpl playerService;      // Instancia real con el mock inyectado
}
```

---

### Patrón AAA aplicado en cada test

```
// ARRANGE → preparar datos y configurar el mock
// ACT     → ejecutar el método bajo prueba
// ASSERT  → verificar resultado e interacción con el mock
```

---

### Suite de pruebas (14 tests en 7 clases `@Nested`)

| Clase `@Nested` | Tests | Qué verifica |
|---|---|---|
| `FindAllTests` | 2 | Lista completa / lista vacía |
| `FindByIdTests` | 2 | Jugador encontrado / `PlayerNotFoundException` |
| `SaveTests` | 1 | Delegación al repositorio con `ArgumentCaptor` |
| `UpdateTests` | 1 | Actualización correcta + `verifyNoMoreInteractions` |
| `DeleteByIdTests` | 2 | Eliminación exitosa / excepción en id inexistente |
| `FindByTeamTests` | 2 | Jugadores del equipo / lista vacía |
| `FindByPositionTests` | 1 | Jugadores de la posición indicada |
| `ExistsByIdTests` | 3 | `true`, `false`, y que no haya interacciones extras |

---

### Conceptos de Mockito usados

| Concepto | Uso en el proyecto |
|---|---|
| `@Mock` | Crea el doble de `IPlayerRepository` |
| `@InjectMocks` | Crea `PlayerServiceImpl` e inyecta el mock |
| `when/thenReturn` | Stubbea métodos que retornan valor |
| `when/thenThrow` | Stubbea excepciones en métodos con retorno |
| `doNothing/when` | Stubbea métodos `void` (explícito) |
| `doThrow/when` | Stubbea excepciones en métodos `void` |
| `verify` | Confirma que el mock fue llamado |
| `times(n)` | Verifica cantidad exacta de invocaciones |
| `never()` | Verifica que un método nunca fue llamado |
| `ArgumentCaptor` | Captura el argumento pasado al mock para inspeccionarlo |
| `ArgumentMatchers` | `any()`, `anyLong()`, `anyString()` para coincidencias flexibles |
| `verifyNoMoreInteractions` | Garantiza que no hubo llamadas adicionales no esperadas |

---

### `DataProvider.java`
Clase utilitaria de pruebas con datos estáticos.

| Método | Retorna |
|---|---|
| `playerListMock()` | Lista de 11 jugadores |
| `playerMock()` | Messi (id=1) |
| `newPlayerMock()` | David Cardona (id=12) para `save` |
| `updatedPlayerMock()` | Messi actualizado a FC Barcelona |
| `realMadridPlayersMock()` | 7 jugadores del Real Madrid |
| `delanterosMock()` | 8 delanteros |
| `porterosMock()` | 5 porteros |
| `defensasMock()` | 4 defensas |

---

## 6. Issues intencionales para SonarCloud

Este proyecto incluye algunos problemas dejados a propósito para un trabajo de análisis estático:

| Tipo | Ejemplo |
|---|---|
| Duplicación | Métodos duplicados de filtrado en `PlayerServiceImpl` y `PlayerRepositoryImpl` |
| Fiabilidad | `catch` vacío en `PlayerRepositoryImpl.riskyOperation()` |
| Seguridad | Claves hardcodeadas, password de admin expuesta y uso de `Random` + `MD5` en `Main` |

### Ejecutar el proyecto

```powershell
mvn clean package -DskipTests
java -cp "target/classes;target/MockTestingProject-1.0-SNAPSHOT.jar" org.mock.Main
```

### Ejecutar SonarCloud

```powershell
mvn sonar:sonar -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=TU_TOKEN
```

---

## 7. Partes de código para analizar y corregir

Si tu objetivo es revisar el proyecto y corregir errores detectados por SonarCloud, estas son las zonas más importantes:

| Archivo | Parte concreta | Qué conviene revisar |
|---|---|---|
| `src/main/java/org/mock/Main.java` | `main(...)` | Tiene demasiadas responsabilidades en un solo método. Conviene separarlo en métodos más pequeños para bajar complejidad cognitiva y facilitar mantenimiento. |
| `src/main/java/org/mock/Main.java` | `generateToken()` | Usa `Random` y `MD5`, lo que es inseguro. Para corregirlo, conviene usar `SecureRandom` y un algoritmo más robusto. |
| `src/main/java/org/mock/Main.java` | `SECRET`, `ADMIN_PASSWORD`, `JWT_SECRET` | Son secretos hardcodeados. Lo recomendable es moverlos a variables de entorno o a un sistema de configuración seguro. |
| `src/main/java/org/mock/Main.java` | `printSecurityBlock()` y `printSecurityBlockCopy()` | Son duplicaciones casi idénticas. Se pueden unificar en un solo método reutilizable. |
| `src/main/java/org/mock/repository/PlayerRepositoryImpl.java` | `findByTeam(...)` y `findByPosition(...)` | Repiten lógica de filtrado muy parecida. Se puede extraer un método genérico para reducir duplicación. |
| `src/main/java/org/mock/repository/PlayerRepositoryImpl.java` | `deleteById(...)` | Tiene una validación previa y una recolección adicional. Conviene revisar si puede simplificarse. |
| `src/main/java/org/mock/repository/PlayerRepositoryImpl.java` | `riskyOperation()` | Contiene un `catch` vacío. Debería registrar el error o lanzarlo de nuevo para no ocultar fallos. |
| `src/main/java/org/mock/service/PlayerServiceImpl.java` | Métodos duplicados auxiliares | Hay lógica repetida que se puede eliminar para bajar la duplicación total del proyecto. |

### Orden recomendado de corrección

1. Seguridad: eliminar secretos hardcodeados y sustituir el generador inseguro.
2. Fiabilidad: arreglar el `catch` vacío y validar mejor entradas nulas.
3. Duplicación: unificar métodos repetidos en `Main` y `PlayerRepositoryImpl`.
4. Complejidad: dividir `main(...)` en métodos pequeños y con una sola responsabilidad.

### Criterio práctico para saber si corregiste bien

- El método queda más corto y fácil de leer.
- Se reduce la repetición de código.
- No quedan secretos visibles en el código fuente.
- Los errores no se silencian con `catch` vacíos.
- SonarCloud deja de marcar problemas en esas líneas o los reduce notablemente.

---

## 5. Dependencias requeridas

```xml
<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito + integración JUnit 5 -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```
