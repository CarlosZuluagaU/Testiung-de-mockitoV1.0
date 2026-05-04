/**
 * Patrón Screenplay - Question (Pregunta)
 * 
 * Una Question permite al actor verificar el estado de la aplicación.
 * Las preguntas retornan valores que pueden ser comparados en assertions.
 */
class Question {
  constructor(name) {
    this.name = name;
  }

  /**
   * Debe ser implementado por las subclases
   * @param {Actor} actor - El actor que hace la pregunta
   */
  async ask(actor) {
    throw new Error(`Question.ask() no está implementado en ${this.constructor.name}`);
  }

  /**
   * Factory method para crear una pregunta
   */
  static about(name) {
    const question = new Question(name);
    return question;
  }
}

export default Question;
