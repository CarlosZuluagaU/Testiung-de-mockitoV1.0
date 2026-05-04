import Task from '../Task.js';

/**
 * Interacción Screenplay - Click
 * 
 * Hace clic en un elemento de la página.
 */
class Click extends Task {
  constructor(selector) {
    super(`Hacer clic en ${selector}`);
    this.selector = selector;
  }

  async perform(actor) {
    cy.log(`Click en: ${this.selector}`);
    cy.get(this.selector).click();
    return this;
  }

  static on(selector) {
    return new Click(selector);
  }
}

export default Click;
