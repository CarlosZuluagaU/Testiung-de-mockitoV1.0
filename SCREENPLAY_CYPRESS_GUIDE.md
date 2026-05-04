# Patrón Screenplay con Cypress - Guía de Uso

## Estructura del Proyecto

```
cypress/
├── e2e/                              # Tests (especificaciones)
│   ├── ejemplo.cy.js               # Ejemplo de test
│   └── [tus-tests].cy.js           # Tus tests aquí
├── support/
│   └── screenplay/
│       ├── Actor.js                # Personaje principal
│       ├── Task.js                 # Interfaz de tareas
│       ├── Question.js             # Interfaz de preguntas
│       ├── interactions/           # Interacciones (Click, Type, Navigate)
│       │   ├── Click.js
│       │   ├── Type.js
│       │   └── Navigate.js
│       ├── actors/                 # Actores específicos (opcional)
│       ├── questions/              # Preguntas específicas
│       │   ├── Visibility.js
│       │   └── TextContent.js
│       └── tasks/                  # Tareas específicas (opcional)
└── scaffold.js                      # Generador de tests

cypress.config.js                     # Configuración de Cypress
package.json                          # Dependencias
```

## Conceptos Clave

### 1. **Actor** (El usuario)
Representa a la persona que realiza acciones en la aplicación.

```javascript
const user = new Actor('Carlos');
```

### 2. **Task** (Tarea)
Una acción que el actor realiza (hacer clic, escribir, navegar).

```javascript
await actor.performTask(Click.on('#btn-login'));
await actor.performTask(Type.into('input').theText('password'));
await actor.performTask(Navigate.to('http://localhost:3000'));
```

### 3. **Question** (Pregunta)
Una verificación del estado de la aplicación.

```javascript
await actor.askQuestion(Visibility.of('#welcome-message'));
```

### 4. **Interactions** (Interacciones)
Operaciones básicas (Click, Type, Navigate, etc.).

## Cómo Usar el Scaffold (Generador)

### Opción 1: Generador Automático
```bash
node cypress/scaffold.js --name=loginTest --url=http://localhost:3000 --actions=navigate,click,type,verify
```

### Opción 2: Usando npm
```bash
npm run test:new -- --name=loginTest --url=http://localhost:3000 --actions=navigate,click,type
```

Esto creará un nuevo archivo `cypress/e2e/loginTest.cy.js` con la estructura lista para llenar.

## Ejemplo Completo

```javascript
import Actor from '../support/screenplay/Actor.js';
import Navigate from '../support/screenplay/interactions/Navigate.js';
import Click from '../support/screenplay/interactions/Click.js';
import Type from '../support/screenplay/interactions/Type.js';
import Visibility from '../support/screenplay/questions/Visibility.js';

describe('Login Flow', () => {
  let user;

  beforeEach(() => {
    user = new Actor('Usuario');
  });

  it('Debería hacer login correctamente', async () => {
    // Arrange
    await user.performTask(Navigate.to('http://localhost:3000/login'));

    // Act
    await user.performTask(Type.into('input[name="email"]').theText('user@example.com'));
    await user.performTask(Type.into('input[name="password"]').theText('password123'));
    await user.performTask(Click.on('button[type="submit"]'));

    // Assert
    await user.askQuestion(Visibility.of('.welcome-message'));
    cy.contains('Bienvenido').should('be.visible');
  });
});
```

## Pasos para Ejecutar

### 1. Instalar dependencias
```bash
npm install
```

### 2. Crear un nuevo test
```bash
node cypress/scaffold.js --name=miTest --url=http://localhost:3000 --actions=navigate,click
```

### 3. Editar el test generado
Abre `cypress/e2e/miTest.cy.js` y reemplaza los selectores.

### 4. Ejecutar los tests
```bash
npm run cypress:open    # Modo interactivo
npm run cypress:run     # Modo headless
npm run cypress:chrome  # Con Chrome
```

## Ventajas del Patrón Screenplay

✓ **Legibilidad**: El código se lee como una historia natural.
✓ **Reutilización**: Las Tasks se pueden usar en múltiples tests.
✓ **Mantenimiento**: Los cambios se hacen en un solo lugar.
✓ **Escalabilidad**: Fácil de extender con nuevas acciones y preguntas.

## Crear Nuevas Interacciones

Si necesitas una nueva acción:

```javascript
// cypress/support/screenplay/interactions/SelectDropdown.js
import Task from '../Task.js';

class SelectDropdown extends Task {
  constructor(selector, value) {
    super(`Seleccionar en dropdown`);
    this.selector = selector;
    this.value = value;
  }

  async perform(actor) {
    cy.get(this.selector).select(this.value);
    return this;
  }

  static with(selector) {
    return {
      theValue: (value) => new SelectDropdown(selector, value)
    };
  }
}

export default SelectDropdown;
```

Luego úsala:
```javascript
await actor.performTask(SelectDropdown.with('#country').theValue('MX'));
```

## Notas Finales

- El scaffold crea tests con TODO comments para ayudarte a saber qué cambiar.
- Adapta los selectores a tu aplicación.
- El patrón Screenplay es flexible: úsalo como mejor te venga.
