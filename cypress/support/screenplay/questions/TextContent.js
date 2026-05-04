import Question from '../Question.js';

/**
 * Pregunta Screenplay - TextContent
 * 
 * Obtiene el contenido de texto de un elemento.
 */
class TextContent extends Question {
  constructor(selector) {
    super(`Obtener texto de ${selector}`);
    this.selector = selector;
  }

  async ask(actor) {
    cy.log(`Obteniendo texto de: ${this.selector}`);
    return cy.get(this.selector).then(el => el.text());
  }

  static of(selector) {
    return new TextContent(selector);
  }
}

export default TextContent;
