import Task from '../Task.js';

/**
 * Interacción Screenplay - Type
 * 
 * Escribe texto en un campo de entrada.
 */
class Type extends Task {
  constructor(selector, text) {
    super(`Escribir en ${selector}`);
    this.selector = selector;
    this.text = text;
  }

  async perform(actor) {
    cy.log(`Type "${this.text}" en: ${this.selector}`);
    cy.get(this.selector).type(this.text);
    return this;
  }

  static into(selector) {
    return {
      theText: (text) => new Type(selector, text)
    };
  }
}

export default Type;
