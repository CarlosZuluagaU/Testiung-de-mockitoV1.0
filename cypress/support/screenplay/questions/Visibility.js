import Question from '../Question.js';

/**
 * Pregunta Screenplay - Visibility
 * 
 * Verifica si un elemento es visible en la página.
 */
class Visibility extends Question {
  constructor(selector) {
    super(`Verificar visibilidad de ${selector}`);
    this.selector = selector;
  }

  async ask(actor) {
    cy.log(`Verificando visibilidad de: ${this.selector}`);
    return cy.get(this.selector).should('be.visible');
  }

  static of(selector) {
    return new Visibility(selector);
  }
}

export default Visibility;
