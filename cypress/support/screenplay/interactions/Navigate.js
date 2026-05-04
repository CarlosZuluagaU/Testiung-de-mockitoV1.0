import Task from '../Task.js';

/**
 * Interacción Screenplay - Navigate
 * 
 * Navega a una URL específica.
 */
class Navigate extends Task {
  constructor(url) {
    super(`Navegar a ${url}`);
    this.url = url;
  }

  async perform(actor) {
    cy.log(`Navegando a: ${this.url}`);
    cy.visit(this.url);
    return this;
  }

  static to(url) {
    return new Navigate(url);
  }
}

export default Navigate;
