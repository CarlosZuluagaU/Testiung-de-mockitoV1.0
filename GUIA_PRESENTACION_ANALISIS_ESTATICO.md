# Guía completa de exposición (Screenplay + análisis estático + SonarQube + cobertura)

## 1. Objetivo de la exposición

Presentar de forma cronológica todo el trabajo realizado en el proyecto:

1. Implementación de estructura Screenplay con Cypress para E2E.
2. Identificación y corrección de problemas de análisis estático (duplicación, seguridad, fiabilidad, complejidad).
3. Integración de SonarQube/SonarCloud en CI con GitHub Actions.
4. Mejora de cobertura de pruebas con validación técnica final.

## 2. Resumen ejecutivo (para abrir en 30 segundos)

> El proyecto inició con deuda técnica intencional para practicar análisis estático. Luego se implementó una base de automatización E2E con patrón Screenplay en Cypress, se refactorizó Java para eliminar hallazgos críticos de calidad y seguridad, se dejó SonarQube funcionando en CI con Java 17 y finalmente se elevó la cobertura de pruebas desde valores bajos hasta un nivel alto y estable.

## 3. Bloque 1: Screenplay con Cypress

### 3.1 Qué se implementó

Se creó una estructura reutilizable de Screenplay:

- Actor
- Task
- Question
- Interactions (`Click`, `Type`, `Navigate`)
- Questions (`Visibility`, `TextContent`)
- Scaffold para generar pruebas rápidamente

Referencia: [SCREENPLAY_CYPRESS_GUIDE.md](SCREENPLAY_CYPRESS_GUIDE.md)

### 3.2 Qué decir en exposición

> Screenplay organiza la automatización como comportamiento de usuario, no como pasos sueltos. Eso mejora legibilidad, mantenimiento y escalabilidad porque las interacciones quedan centralizadas y reutilizables.

### 3.3 Demo sugerida

1. Mostrar la estructura de carpetas `cypress/`.
2. Abrir un ejemplo de `Actor` y una `Task`.
3. Mostrar cómo se genera un test con el scaffold.

Comandos:

```bash
node cypress/scaffold.js --name=miTest --url=http://localhost:3000 --actions=navigate,click,type,verify
npm run cypress:open
```

## 4. Bloque 2: problemas detectados en análisis estático

Problemas originales del proyecto Java:

- Alta complejidad en `Main`.
- Duplicación en repositorio y servicio.
- Riesgo de seguridad por secretos hardcodeados y generación de token insegura.
- Riesgo de fiabilidad por manejo incorrecto de errores.

## 5. Bloque 3: refactor por archivo (núcleo técnico)

### 5.1 Main

Archivo: [src/main/java/org/mock/Main.java](src/main/java/org/mock/Main.java)

Cambios clave:

- Se dividió el flujo principal en métodos pequeños por responsabilidad.
- Se eliminó lógica insegura y se dejó generación segura de token.
- Se separó la impresión de reportes y demos para reducir complejidad cognitiva.

Mensaje para jurado:

> El `main` dejó de ser un bloque monolítico y pasó a ser un orquestador limpio, más fácil de mantener y de testear.

### 5.2 Repository

Archivo: [src/main/java/org/mock/repository/PlayerRepositoryImpl.java](src/main/java/org/mock/repository/PlayerRepositoryImpl.java)

Cambios clave:

- Unificación de filtros para eliminar duplicación.
- Validaciones con `Objects.requireNonNull(...)`.
- `findAll()` devolviendo copia (`List.copyOf`) para no exponer estado interno.
- `deleteById(...)` con comportamiento consistente y excepción controlada cuando no existe el id.

Mensaje para jurado:

> Aquí se atacó duplicación y robustez de datos. Menos código repetido significa menos bugs futuros.

### 5.3 Service

Archivo: [src/main/java/org/mock/service/PlayerServiceImpl.java](src/main/java/org/mock/service/PlayerServiceImpl.java)

Cambios clave:

- Eliminación de métodos redundantes.
- Servicio como capa de delegación limpia.
- Dependencia `final` y validada en constructor.

Mensaje para jurado:

> Se dejó una arquitectura más clara por capas: el servicio coordina, el repositorio resuelve persistencia.

## 6. Bloque 4: SonarQube/SonarCloud en CI

### 6.1 Configuración final

Archivos:

- [pom.xml](pom.xml)
- [.github/workflows/sonar.yml](.github/workflows/sonar.yml)

Puntos importantes que sí quedaron alineados:

- Organización Sonar: `carloszuluagau`.
- Proyecto: `CarlosZuluagaU_fabrica_2026S1`.
- Workflow con JDK 17.
- Uso de secreto `SONAR_TOKEN` en GitHub Actions.

Mensaje para jurado:

> El análisis no depende ya de ejecución local: queda integrado al pipeline y se valida en cada push/pull request.

## 7. Bloque 5: cobertura y pruebas

### 7.1 Qué se hizo

Se amplió la suite de pruebas unitarias para cubrir capas que antes no estaban contempladas.

Archivos de pruebas relevantes:

- [src/test/java/org/mock/service/PlayerServiceImplTest.java](src/test/java/org/mock/service/PlayerServiceImplTest.java)
- [src/test/java/org/mock/repository/PlayerRepositoryImplTest.java](src/test/java/org/mock/repository/PlayerRepositoryImplTest.java)
- [src/test/java/org/mock/persistence/entity/PlayerTest.java](src/test/java/org/mock/persistence/entity/PlayerTest.java)
- [src/test/java/org/mock/exception/PlayerNotFoundExceptionTest.java](src/test/java/org/mock/exception/PlayerNotFoundExceptionTest.java)
- [src/test/java/org/mock/MainTest.java](src/test/java/org/mock/MainTest.java)

### 7.2 Resultado actual

- 68 pruebas ejecutadas.
- 0 fallos.
- Build en éxito.
- Cobertura JaCoCo total actual: 96%.

Reporte: [target/site/jacoco/index.html](target/site/jacoco/index.html)

### 7.3 Cómo explicar el “por qué no 100%”

> Llegar al 100% es posible, pero no siempre es óptimo forzarlo si implica testear ramas artificiales o de baja probabilidad operativa. En este caso se logró un 96% realista y sólido, con cobertura completa en paquetes críticos como repositorio, servicio y excepciones.

## 8. Línea de tiempo del trabajo (para slide cronológica)

1. Se construyó la base Screenplay con Cypress.
2. Se documentó la arquitectura y forma de uso del framework E2E.
3. Se refactorizó el código Java para eliminar hallazgos de análisis estático.
4. Se ajustó CI para SonarQube/SonarCloud con Java 17 y token seguro.
5. Se incrementó cobertura con nuevas pruebas unitarias hasta 96%.
6. Se validó todo con ejecución de pruebas en verde.

## 9. Script de exposición (versión 5-7 minutos)

### 9.1 Introducción

> Este proyecto combina calidad de código y automatización. Primero estructuramos pruebas E2E con Screenplay en Cypress; luego resolvimos deuda técnica en Java enfocada en seguridad, fiabilidad, complejidad y duplicación.

### 9.2 Screenplay

> En vez de escribir pruebas rígidas, usamos Actor/Task/Question para modelar comportamiento del usuario. Esto permite reutilizar acciones y mantener los tests a largo plazo.

### 9.3 Refactor estático

> En `Main` reducimos complejidad. En repositorio eliminamos duplicación y mejoramos manejo de estado. En servicio dejamos una capa clara y liviana.

### 9.4 CI y Sonar

> El pipeline quedó integrado con SonarCloud, usando JDK 17 y `SONAR_TOKEN`. Así aseguramos control de calidad automático por commit.

### 9.5 Cobertura

> Subimos de cobertura baja a 96% con 68 tests verdes. Esto demuestra que los cambios no solo son estéticos: están verificados.

### 9.6 Cierre

> El resultado final es un proyecto más mantenible, seguro y medible, con automatización funcional tanto en E2E como en unit testing.

## 10. Preguntas frecuentes y respuestas cortas

### ¿Por qué usar Screenplay y no pruebas tradicionales?

Porque separa comportamiento, reduce duplicación de pasos y escala mejor en proyectos reales.

### ¿Qué valor aportó SonarQube si ya había tests?

Los tests validan comportamiento; Sonar detecta deuda técnica y riesgos de calidad que no siempre rompen pruebas.

### ¿Qué fue lo más crítico que se corrigió?

El riesgo de seguridad (lógica insegura), la duplicación en repositorio/servicio y la complejidad de `Main`.

### ¿Qué sigue después de esta entrega?

Conectar los tests Cypress a un flujo CI para ejecutar E2E automáticamente en cada cambio relevante.

## 11. Cierre final (frase lista para usar)

> La mejora no fue solo “pasar Sonar”: se construyó una base de automatización y calidad sostenible, con arquitectura más limpia, análisis continuo y evidencia de estabilidad a través de pruebas.
