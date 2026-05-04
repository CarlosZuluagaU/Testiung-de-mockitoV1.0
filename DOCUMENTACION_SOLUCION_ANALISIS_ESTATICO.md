# Documentación de solución: análisis de código estático

## 1. Objetivo

Este documento resume cómo se corrigieron los problemas detectados por análisis estático en el proyecto. La meta fue eliminar duplicación, mejorar fiabilidad, eliminar riesgos de seguridad y simplificar la estructura del código para que SonarCloud muestre un código más mantenible.

## 2. Resumen ejecutivo

Antes de la corrección, el proyecto tenía problemas intencionales para el análisis: secretos hardcodeados, uso de `Random` y `MD5`, métodos duplicados, bloques `catch` vacíos y un `main(...)` con demasiadas responsabilidades.

Después de la corrección:
- Se removieron los secretos hardcodeados.
- Se reemplazó la generación insegura del token por una versión basada en `SecureRandom` y `SHA-256`.
- Se eliminó la duplicación en el repositorio y en el servicio.
- Se simplificó `Main.java` dividiéndolo en métodos más pequeños.
- La suite de 14 pruebas unitarias sigue pasando.

## 3. Solución aplicada por archivo

### `src/main/java/org/mock/Main.java`

#### Problema original

- El método `main(...)` hacía demasiadas cosas a la vez.
- Había información sensible expuesta en consola.
- Se usaba un generador de token inseguro con `Random` y `MD5`.
- Existían bloques duplicados para imprimir un bloque de seguridad.

#### Solución aplicada

- `main(...)` quedó solo como punto de entrada y coordinación.
- La salida se dividió en métodos pequeños:
  - `printHeader()`
  - `printStatistics(...)`
  - `printPlayersByPosition(...)`
  - `printPlayersByTeam(...)`
  - `runCrudDemo(...)`
  - `runMissingPlayerDemo(...)`
  - `printSecureTokenDemo()`
- La estadística ahora se calcula con helpers genéricos y no repite lógica.
- Se eliminó toda referencia a secretos hardcodeados.
- `generateToken()` ahora usa `SecureRandom` y `SHA-256`.
- Se agregó `toHex(...)` para transformar el hash a formato hexadecimal.

#### Resultado

- Menor complejidad cognitiva.
- Menor riesgo de exposición de secretos.
- Mejor lectura del flujo principal.
- Método más fácil de mantener y explicar en la presentación.

### `src/main/java/org/mock/repository/PlayerRepositoryImpl.java`

#### Problema original

- `findByTeam(...)` y `findByPosition(...)` repetían lógica casi idéntica.
- Había métodos legados y duplicados que no aportaban valor.
- `riskyOperation()` ocultaba errores con un `catch` vacío.
- `deleteById(...)` reconstruía la lista de forma innecesaria.

#### Solución aplicada

- Se creó un helper único `filterBy(Predicate<Player>)`.
- `findByTeam(...)` y `findByPosition(...)` ahora delegan en ese helper.
- Se eliminaron:
  - `findByTeamLegacy(...)`
  - `findByPositionLegacy(...)`
  - `filterByTeamDuplicate(...)`
  - `filterByPositionDuplicate(...)`
  - `riskyOperation()`
- `playerDatabase` quedó como `final`.
- `findAll()` devuelve `List.copyOf(...)` para no exponer la colección interna.
- `save(...)` y `update(...)` validan entradas nulas con `Objects.requireNonNull(...)`.
- `deleteById(...)` usa `removeIf(...)` y lanza `PlayerNotFoundException` si no elimina nada.

#### Resultado

- Menos duplicación.
- Menos riesgo de inconsistencia entre métodos similares.
- Mejor fiabilidad al no ocultar errores.
- Código más corto y directo.

### `src/main/java/org/mock/service/PlayerServiceImpl.java`

#### Problema original

- El servicio tenía métodos auxiliares duplicados que repetían el mismo filtrado que el repositorio.
- La dependencia no estaba declarada como inmutable.

#### Solución aplicada

- Se eliminó toda lógica duplicada auxiliar.
- El repositorio quedó como `final`.
- El constructor usa `Objects.requireNonNull(...)` para fallar rápido si se intenta instanciar el servicio sin repositorio.
- La clase volvió a ser una capa de delegación pura.

#### Resultado

- El servicio quedó limpio y enfocado.
- Se redujo la duplicación total del proyecto.
- La inyección de dependencias es más segura.

### `pom.xml`

#### Estado

- El proyecto sigue compilando con Java 21.
- JaCoCo continúa habilitado para reportar cobertura.
- El umbral mínimo se mantiene en `0.00` para no bloquear el build mientras la cobertura real sigue centrada en la capa de servicio.

#### Comentario para presentación

Si te preguntan por cobertura, explica que el build ya valida pruebas y reporta JaCoCo, pero que la cobertura del proyecto todavía puede crecer si se agregan pruebas para repositorio y flujo de consola.

### `.github/workflows/sonar.yml`

#### Estado

- El workflow ya resuelve `pom.xml` de forma dinámica.
- Eso evita depender de una ruta fija en GitHub Actions.
- El análisis de SonarCloud queda preparado para ejecutarse en CI con `SONAR_TOKEN`.

## 4. Validación realizada

Se ejecutó:

```powershell
mvn test
```

Resultado:
- 14 pruebas ejecutadas.
- 0 fallos.
- 0 errores.
- BUILD SUCCESS.

## 5. Qué problemas de análisis estático quedaron resueltos

- Seguridad: secretos hardcodeados y token inseguro.
- Fiabilidad: `catch` vacío y manejo débil de errores.
- Duplicación: métodos repetidos de filtrado y bloques duplicados.
- Complejidad: `main(...)` reducido a funciones pequeñas.
- Mantenibilidad: mejor separación de responsabilidades.

## 6. Qué deberías mostrar en la presentación

### Orden recomendado de explicación

1. Mostrar el problema original en `Main.java`.
2. Explicar cómo se separó la lógica en métodos pequeños.
3. Mostrar el cambio del repositorio a un filtro genérico.
4. Explicar por qué se eliminó el `catch` vacío.
5. Cerrar con la validación: `mvn test` exitoso.

### Frase corta de cierre

> El proyecto pasó de tener problemas intencionales de seguridad, fiabilidad, duplicación y complejidad a una versión más limpia y mantenible. La lógica se separó por responsabilidades, se eliminaron secretos y duplicaciones, y la validación automática sigue pasando correctamente.

## 7. Resumen por idea clave

- Menos complejidad en `Main.java`.
- Menos duplicación en repository y service.
- Mejor seguridad al eliminar secretos y token inseguro.
- Mejor fiabilidad al no silenciar errores.
- Validación confirmada con pruebas verdes.
