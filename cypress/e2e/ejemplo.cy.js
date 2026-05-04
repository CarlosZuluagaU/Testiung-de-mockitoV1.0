/**
 * Ejemplo de Test usando Patrón Screenplay
 * 
 * Este test muestra cómo usar el patrón Screenplay con Cypress.
 * El flujo es: Actor -> Task -> Question -> Assertion
 */

import Actor from '../support/screenplay/Actor.js';
import Navigate from '../support/screenplay/interactions/Navigate.js';
import Click from '../support/screenplay/interactions/Click.js';
import Type from '../support/screenplay/interactions/Type.js';
import Visibility from '../support/screenplay/questions/Visibility.js';

describe('Test ejemplo con patrón Screenplay', () => {
  let user;

  beforeEach(() => {
    // Crear un actor (usuario)
    user = new Actor('Carlos');
  });

  it('Debería navegar a la página principal', async () => {
    // El actor navega a una URL
    await user.performTask(Navigate.to('https://example.com'));
    
    // Verificar que algo es visible
    await user.askQuestion(Visibility.of('body'));
  });

  it('Debería hacer clic en un botón', async () => {
    await user.performTask(Navigate.to('https://example.com'));
    
    // El actor hace clic (adapta el selector a tu aplicación)
    await user.performTask(Click.on('a')); // Primer enlace
    
    // Verificar resultado
    cy.url().should('include', 'example');
  });

  it('Debería escribir en un campo', async () => {
    await user.performTask(Navigate.to('https://example.com'));
    
    // El actor escribe (adapta los selectores)
    // await user.performTask(Type.into('input[type="text"]').theText('Carlos'));
    
    // Verificar
    // cy.get('input[type="text"]').should('have.value', 'Carlos');
  });
});
