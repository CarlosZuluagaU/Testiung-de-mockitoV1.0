/**
 * Scaffold - Generador de Tests con Patrón Screenplay
 * 
 * Este script genera automáticamente nuevos tests usando el patrón Screenplay.
 * 
 * Uso:
 *   node scaffold.js --name "miTest" --url "http://localhost:3000" --actions "click,type,verify"
 * 
 * Salida:
 *   cypress/e2e/miTest.cy.js
 */

const fs = require('fs');
const path = require('path');

const args = process.argv.slice(2);
const options = {};

// Parsear argumentos
args.forEach(arg => {
  const [key, value] = arg.split('=');
  if (key.startsWith('--')) {
    options[key.substring(2)] = value;
  }
});

if (!options.name) {
  console.error('Error: Se requiere --name=nombreDelTest');
  process.exit(1);
}

const testName = options.name || 'test';
const testUrl = options.url || 'https://example.com';
const actions = options.actions ? options.actions.split(',') : ['navigate'];

// Plantilla del test
const testTemplate = `/**
 * Test Auto-generado con Patrón Screenplay
 * 
 * Nombre: ${testName}
 * URL: ${testUrl}
 * Acciones: ${actions.join(', ')}
 */

import Actor from '../support/screenplay/Actor.js';
import Navigate from '../support/screenplay/interactions/Navigate.js';
import Click from '../support/screenplay/interactions/Click.js';
import Type from '../support/screenplay/interactions/Type.js';
import Visibility from '../support/screenplay/questions/Visibility.js';

describe('${testName}', () => {
  let actor;

  beforeEach(() => {
    actor = new Actor('Usuario de Prueba');
  });

  it('Debería ejecutar las acciones definidas', async () => {
    // TODO: Reemplaza los selectores y URLs con los valores correctos para tu aplicación
    
    // Navegar
    ${actions.includes('navigate') ? `await actor.performTask(Navigate.to('${testUrl}'));` : '// Navegar a URL'}
    
    // Hacer clic
    ${actions.includes('click') ? `// await actor.performTask(Click.on('selector-aqui'));` : ''}
    
    // Escribir
    ${actions.includes('type') ? `// await actor.performTask(Type.into('input-selector').theText('texto'));` : ''}
    
    // Verificar
    ${actions.includes('verify') ? `// await actor.askQuestion(Visibility.of('elemento-selector'));` : ''}
    
    // Assertions
    cy.log('Test completado');
  });
});
`;

// Crear ruta de salida
const outputDir = path.join(__dirname, 'cypress', 'e2e');
const outputFile = path.join(outputDir, `${testName}.cy.js`);

// Asegurar que el directorio existe
if (!fs.existsSync(outputDir)) {
  fs.mkdirSync(outputDir, { recursive: true });
}

// Escribir el archivo
fs.writeFileSync(outputFile, testTemplate);
console.log(`✓ Test creado: ${outputFile}`);
console.log(`\nPróximos pasos:`);
console.log(`1. Abre el archivo: ${outputFile}`);
console.log(`2. Reemplaza los selectores (selector-aqui) con los de tu aplicación`);
console.log(`3. Ejecuta con: npx cypress open`);
