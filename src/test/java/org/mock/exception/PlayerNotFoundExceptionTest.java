package org.mock.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para PlayerNotFoundException.
 * Cubre: constructores y mensaje de excepción.
 */
@DisplayName("Pruebas unitarias de PlayerNotFoundException")
class PlayerNotFoundExceptionTest {

    @Nested
    @DisplayName("Constructor y mensaje")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor crea excepción con mensaje que contiene el id")
        void testConstructorWithId() {
            // ARRANGE + ACT
            long playerId = 99L;
            PlayerNotFoundException exception = new PlayerNotFoundException(playerId);

            // ASSERT
            assertNotNull(exception);
            assertTrue(exception.getMessage().contains("99"));
            assertTrue(exception.getMessage().contains("no encontrado"));
        }

        @Test
        @DisplayName("Constructor crea excepción con id diferente en mensaje")
        void testConstructorWithDifferentId() {
            // ARRANGE + ACT
            long playerId = 42L;
            PlayerNotFoundException exception = new PlayerNotFoundException(playerId);

            // ASSERT
            assertTrue(exception.getMessage().contains("42"));
        }

        @Test
        @DisplayName("Exception es instancia de RuntimeException")
        void testIsRuntimeException() {
            // ARRANGE
            PlayerNotFoundException exception = new PlayerNotFoundException(1L);

            // ACT + ASSERT
            assertInstanceOf(RuntimeException.class, exception);
        }

        @Test
        @DisplayName("getMessage retorna el mensaje esperado")
        void testGetMessage() {
            // ARRANGE
            long playerId = 55L;
            PlayerNotFoundException exception = new PlayerNotFoundException(playerId);

            // ACT
            String message = exception.getMessage();

            // ASSERT
            assertEquals("Jugador con id 55 no encontrado.", message);
        }

        @Test
        @DisplayName("toString contiene el nombre de la excepción y el mensaje")
        void testToString() {
            // ARRANGE
            PlayerNotFoundException exception = new PlayerNotFoundException(10L);

            // ACT
            String result = exception.toString();

            // ASSERT
            assertTrue(result.contains("PlayerNotFoundException"));
            assertTrue(result.contains("10"));
        }

        @Test
        @DisplayName("Se puede lanzar y capturar la excepción")
        void testThrowAndCatchException() {
            // ARRANGE + ACT + ASSERT
            assertThrows(PlayerNotFoundException.class, () -> {
                throw new PlayerNotFoundException(7L);
            });
        }
    }
}
