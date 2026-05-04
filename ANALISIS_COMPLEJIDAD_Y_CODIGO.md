# Análisis de complejidad y revisión de código

## 1. Complejidad ciclomática

La complejidad ciclomática mide cuántos caminos lógicos distintos puede seguir un método. Aumenta cuando aparecen estructuras como `if`, `else if`, `for`, `while`, `switch` o validaciones encadenadas.

En este proyecto, la complejidad ciclomática se concentra sobre todo en:

### `src/main/java/org/mock/Main.java`
El método `main(...)` concentra varias acciones distintas: inicialización de objetos, consultas, estadísticas, CRUD, manejo de excepciones y salida por consola. Aunque no tenga muchas decisiones anidadas, sí agrupa demasiadas rutas de ejecución en un solo sitio.

### `src/main/java/org/mock/repository/PlayerRepositoryImpl.java`
Métodos como `deleteById(...)`, `findByTeam(...)` y `findByPosition(...)` añaden decisiones y recorrido de colecciones. Esto aumenta la cantidad de caminos posibles dentro del repositorio.

### Cómo bajar la complejidad ciclomática
- Dividir `main(...)` en métodos más pequeños.
- Mover la lógica de estadísticas a una clase auxiliar.
- Extraer la validación previa de `deleteById(...)` si se puede simplificar.
- Reutilizar el filtrado de jugadores en un solo método genérico.

---

## 2. Complejidad cognitiva

La complejidad cognitiva mide qué tan difícil es entender el código. No solo importa cuántos caminos hay, sino también cuánta carga mental exige leerlo.

En este proyecto, la complejidad cognitiva aparece especialmente en:

### `src/main/java/org/mock/Main.java`
`main(...)` tiene demasiadas responsabilidades juntas. Hace todo en un mismo método, lo que dificulta seguir el flujo, localizar errores y mantener el código.

### `src/main/java/org/mock/repository/PlayerRepositoryImpl.java`
`deleteById(...)` obliga a leer una validación y luego una reconstrucción de la lista. Además, `findByTeam(...)` y `findByPosition(...)` repiten patrones muy parecidos, lo que hace el código más pesado de entender.

### `src/main/java/org/mock/Main.java` y `src/main/java/org/mock/repository/PlayerRepositoryImpl.java`
Las funciones duplicadas y los bloques repetidos aumentan la carga mental, porque obligan a revisar lo mismo varias veces.

### Cómo bajar la complejidad cognitiva
- Separar cada responsabilidad en un método específico.
- Evitar bloques grandes con muchas tareas diferentes.
- Eliminar duplicación de lógica.
- Usar nombres claros para que el flujo se entienda rápido.

---

## 3. Partes del código que conviene revisar

### `src/main/java/org/mock/Main.java`
#### `main(...)`
Es la parte más importante para revisar porque reúne demasiada lógica. Conviene dividirla en métodos como:
- `printHeader()`
- `printStats()`
- `printPlayersByPosition()`
- `printPlayersByTeam()`
- `runCrudDemo()`
- `runExceptionDemo()`

#### `generateToken()`
Usa `Random` y `MD5`, lo cual es inseguro. Conviene reemplazarlo por un generador seguro.

#### `SECRET`, `ADMIN_PASSWORD`, `JWT_SECRET`
Son secretos hardcodeados. Lo correcto sería moverlos a variables de entorno o configuración externa.

#### `printSecurityBlock()` y `printSecurityBlockCopy()`
Son duplicados casi idénticos. Se pueden unificar en un solo método.

### `src/main/java/org/mock/repository/PlayerRepositoryImpl.java`
#### `findByTeam(...)` y `findByPosition(...)`
Repiten una lógica muy parecida de filtrado. Se puede extraer una función común para evitar duplicación.

#### `findByTeamLegacy(...)` y `findByPositionLegacy(...)`
Repiten la misma lógica que los métodos principales, así que aumentan la duplicación del proyecto.

#### `filterByTeamDuplicate(...)` y `filterByPositionDuplicate(...)`
También repiten el filtrado y deberían eliminarse o reutilizarse.

#### `riskyOperation()`
Tiene un `catch` vacío. Eso oculta errores y dificulta detectar fallos reales.

#### `deleteById(...)`
Puede simplificarse si se reorganiza la validación y la eliminación.

### `src/main/java/org/mock/service/PlayerServiceImpl.java`
#### Métodos auxiliares duplicados
Hay lógica repetida que no aporta valor real y puede eliminarse para reducir duplicación.

---

## 4. Orden recomendado para corregir errores

1. Seguridad: eliminar secretos hardcodeados y sustituir el generador inseguro.
2. Fiabilidad: evitar el `catch` vacío y revisar validaciones.
3. Duplicación: unificar métodos repetidos.
4. Complejidad: dividir `main(...)` en métodos pequeños.

---

## 5. Ejemplo de explicación para entregar

Puedes decir algo como esto:

> En mi proyecto, la complejidad ciclomática y cognitiva se concentra principalmente en `Main.main(...)` y en algunos métodos de `PlayerRepositoryImpl`. `main(...)` agrupa demasiadas responsabilidades en un solo método, lo que dificulta su lectura y mantenimiento. Además, en el repositorio hay métodos con lógica repetida y validaciones que incrementan el coste de comprensión. Para reducir estas métricas, conviene dividir el código en métodos más pequeños, reutilizar lógica común y eliminar duplicación.

---

## 6. Criterio para saber si mejoró

- El método queda más corto.
- El flujo se entiende más rápido.
- Hay menos código repetido.
- SonarCloud muestra menos advertencias.
- Cada método tiene una sola responsabilidad.

---

## 7. Deuda técnica

La deuda técnica es el costo acumulado de tomar atajos en el código o en la arquitectura para avanzar más rápido en el corto plazo. No siempre es un error; a veces se acepta de forma intencional, pero luego exige más tiempo para corregir, probar o extender el sistema.

En este proyecto, la deuda técnica aparece sobre todo en:

### `src/main/java/org/mock/Main.java`
- Tiene demasiadas responsabilidades en un solo método.
- Mezcla demostración, lógica de negocio, estadísticas y seguridad.
- Usa valores sensibles hardcodeados y generación insegura de token.

### `src/main/java/org/mock/repository/PlayerRepositoryImpl.java`
- Repite lógica de filtrado en varios métodos.
- Incluye código legado que duplica comportamiento.
- Tiene un `catch` vacío que oculta problemas reales.

### `src/main/java/org/mock/service/PlayerServiceImpl.java`
- Conserva métodos auxiliares que no aportan valor y aumentan la duplicación.

### Cómo se nota la deuda técnica
- El código tarda más en entenderse.
- Cambiar una parte obliga a revisar varias más.
- Aumenta el riesgo de errores al modificar métodos largos o repetidos.
- SonarCloud detecta duplicación, problemas de seguridad y fiabilidad.

### Orden recomendado para pagarla
1. Quitar credenciales y secretos hardcodeados.
2. Reemplazar lógica insegura y bloques de error vacíos.
3. Eliminar duplicación de métodos y filtros.
4. Separar responsabilidades grandes en funciones pequeñas.

### Frase corta para entregar

> El proyecto tiene deuda técnica principalmente por duplicación, seguridad y complejidad en `Main.java` y `PlayerRepositoryImpl.java`. Esa deuda no impide ejecutar la aplicación, pero sí dificulta su mantenimiento y aumenta el riesgo de errores. Para reducirla, conviene primero corregir los problemas de seguridad y fiabilidad, y después simplificar la estructura del código.

---

## 8. Cómo lo ve SonarCloud

SonarCloud no mide solo si el código compila; también evalúa cuánto cuesta mantenerlo y qué riesgos introduce. En este proyecto, la deuda técnica se traduce en hallazgos concretos que SonarCloud suele marcar como issues o code smells.

| Tipo de deuda técnica | Ejemplo en el proyecto | Qué reporta SonarCloud |
| --- | --- | --- |
| Duplicación | Métodos repetidos en `Main.java` y `PlayerRepositoryImpl.java` | `Duplicated Lines` o duplicación de bloques |
| Seguridad | `Random`, `MD5` y secretos hardcodeados | Issues de seguridad y credenciales expuestas |
| Fiabilidad | `catch` vacío en `riskyOperation()` | Problemas de robustez o código que oculta fallos |
| Complejidad | `main(...)` con demasiadas responsabilidades | `Cognitive Complexity` alta |

### Prioridad recomendada en SonarCloud
1. Corregir los issues de seguridad.
2. Corregir fiabilidad y manejo de errores.
3. Reducir duplicación.
4. Partir los métodos grandes para bajar complejidad.

### Regla práctica

Si SonarCloud marca algo como seguridad o fiabilidad, eso se corrige antes que un code smell de estilo o estructura. Si marca duplicación o complejidad, conviene dividir el código y reutilizar lógica común.
