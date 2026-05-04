/**
 * Patrón Screenplay - Task (Tarea)
 * 
 * Una Task representa una acción que el actor debe realizar.
 * Las tareas son composables y reutilizables.
 */
class Task {
  constructor(name) {
    this.name = name;
  }

  /**
   * Debe ser implementado por las subclases
   * @param {Actor} actor - El actor que realiza la tarea
   */
  async perform(actor) {
    throw new Error(`Task.perform() no está implementado en ${this.constructor.name}`);
  }

  /**
   * Factory method para crear una tarea
   */
  static named(name) {
    const task = new Task(name);
    return task;
  }
}

export default Task;
