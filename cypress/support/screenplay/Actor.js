/**
 * Patrón Screenplay - Actor
 * 
 * El Actor es el personaje principal que ejecuta tareas y realiza preguntas.
 * Representa a un usuario interactuando con la aplicación.
 */
class Actor {
  constructor(name) {
    this.name = name;
    this.abilities = {};
    this.memories = {};
  }

  /**
   * Asigna una habilidad al actor
   * @param {string} abilityName - Nombre de la habilidad
   * @param {*} ability - La habilidad en sí
   */
  withAbility(abilityName, ability) {
    this.abilities[abilityName] = ability;
    return this;
  }

  /**
   * Ejecuta una tarea
   * @param {Task} task - Tarea a ejecutar
   */
  async performTask(task) {
    cy.log(`${this.name} realiza: ${task.name || 'una tarea'}`);
    return await task.perform(this);
  }

  /**
   * Responde una pregunta
   * @param {Question} question - Pregunta a responder
   */
  async askQuestion(question) {
    cy.log(`${this.name} pregunta: ${question.name || 'una pregunta'}`);
    return await question.ask(this);
  }

  /**
   * Guarda información en memoria
   * @param {string} key - Clave
   * @param {*} value - Valor
   */
  remember(key, value) {
    this.memories[key] = value;
    return this;
  }

  /**
   * Recupera información de memoria
   * @param {string} key - Clave
   */
  recall(key) {
    return this.memories[key];
  }

  /**
   * Obtiene una habilidad por nombre
   * @param {string} abilityName - Nombre de la habilidad
   */
  getAbility(abilityName) {
    return this.abilities[abilityName];
  }
}

export default Actor;
