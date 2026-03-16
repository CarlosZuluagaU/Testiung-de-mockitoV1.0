package org.mock.exception;

public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(Long id) {
        super("Jugador con id " + id + " no encontrado.");
    }
}
