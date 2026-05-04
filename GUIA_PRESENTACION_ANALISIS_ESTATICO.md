# Guion de presentación: solución del análisis estático

## 1. Apertura

> Buen día. Voy a explicar cómo identifiqué y corregí los problemas de análisis estático del proyecto, centrados en duplicación, seguridad, fiabilidad y complejidad. La idea fue dejar el código más limpio, más mantenible y listo para que SonarCloud marque menos hallazgos.

## 2. Qué problema tenía el proyecto

Antes del refactor, el proyecto tenía deuda técnica intencional para el análisis:
- `Main.java` concentraba demasiadas responsabilidades.
- Había secretos hardcodeados y uso inseguro de `Random` y `MD5`.
- El repositorio repetía lógica de filtrado.
- Existía un `catch` vacío que ocultaba errores.
- El servicio tenía duplicación innecesaria.

## 3. Qué corregí en cada archivo

### `src/main/java/org/mock/Main.java`

> Este fue el archivo más importante porque concentraba complejidad, seguridad y duplicación.

Qué hice:
- Separé el `main(...)` en métodos pequeños y con una sola responsabilidad.
- Dividí la salida en bloques como encabezado, estadísticas, búsquedas, CRUD y demo de excepción.
- Eliminé los secretos hardcodeados.
- Reemplacé la generación insegura del token por `SecureRandom` y `SHA-256`.
- Quité métodos duplicados de impresión.

Qué decir en la exposición:
> Aquí reduje la complejidad cognitiva: el método principal ya no hace todo junto, sino que coordina funciones pequeñas. Eso mejora lectura, mantenimiento y análisis estático.

### `src/main/java/org/mock/repository/PlayerRepositoryImpl.java`

> En este archivo el problema principal era la repetición de lógica y el manejo débil de errores.

Qué hice:
- Unifiqué el filtrado con un método genérico `filterBy(...)`.
- Eliminé métodos legados y duplicados.
- Eliminé `riskyOperation()` porque ocultaba errores con un `catch` vacío.
- Hice `playerDatabase` inmutable como referencia interna con `final`.
- Cambié `findAll()` para devolver una copia y no exponer la lista interna.
- Agregué validaciones con `Objects.requireNonNull(...)` en `save(...)` y `update(...)`.
- Simplifiqué `deleteById(...)` usando `removeIf(...)`.

Qué decir en la exposición:
> Aquí el objetivo fue bajar duplicación y mejorar fiabilidad. Ahora el repositorio tiene una lógica más clara y menos puntos de fallo.

### `src/main/java/org/mock/service/PlayerServiceImpl.java`

> El servicio era simple, pero tenía duplicación innecesaria que no aportaba valor.

Qué hice:
- Eliminé métodos auxiliares duplicados.
- Dejé la clase como una capa de delegación limpia.
- Hice el repositorio `final`.
- Validé el constructor con `Objects.requireNonNull(...)`.

Qué decir en la exposición:
> El servicio quedó como debería estar: una capa delgada que delega al repositorio y no repite lógica.

### `pom.xml`

> Aquí no cambié la funcionalidad del negocio, pero sí mantuve la base para CI y cobertura.

Qué recordar:
- Java 21 sigue configurado.
- JaCoCo sigue activo.
- El umbral quedó en `0.00` para no bloquear el build mientras la cobertura se usa como referencia.

Qué decir en la exposición:
> La cobertura sigue midiendo el código, pero la meta no bloquea la compilación hasta que la suite crezca más.

### `.github/workflows/sonar.yml`

> Este archivo ya estaba corregido para que GitHub Actions encuentre el `pom.xml` de forma dinámica.

Qué decir en la exposición:
> Eso evita fallos por rutas fijas y hace más robusto el análisis en CI.

## 4. Validación realizada

Ejecuté:

```powershell
mvn test
```

Resultado:
- 14 pruebas ejecutadas.
- 0 fallos.
- 0 errores.
- BUILD SUCCESS.

Qué decir en la exposición:
> La solución no solo quedó bien escrita, también quedó validada con pruebas verdes.

## 5. Qué mejoró con el refactor

- Menos complejidad en `Main.java`.
- Menos duplicación en repositorio y servicio.
- Menos riesgo de seguridad por secretos o algoritmos débiles.
- Menos riesgo de ocultar errores.
- Mejor mantenibilidad general.

## 6. Cómo lo explicaría en 1 minuto

> El proyecto tenía deuda técnica intencional para que SonarCloud detectara problemas reales. La corrección se centró en cuatro ejes: seguridad, fiabilidad, duplicación y complejidad. En `Main.java` separé responsabilidades y eliminé secretos inseguros. En `PlayerRepositoryImpl` unifiqué filtros, eliminé código repetido y corregí el manejo de errores. En `PlayerServiceImpl` dejé solo delegación limpia. Finalmente, validé todo con `mvn test`, que pasó con 14 pruebas exitosas.

## 7. Cierre

> Con estos cambios, el proyecto quedó más claro, más mantenible y mejor preparado para análisis estático y presentación.

## 8. Orden sugerido para mostrar en diapositivas

1. Problema original.
2. `Main.java` antes y después.
3. `PlayerRepositoryImpl.java` antes y después.
4. `PlayerServiceImpl.java` simplificado.
5. Validación con `mvn test`.
6. Conclusión final.
