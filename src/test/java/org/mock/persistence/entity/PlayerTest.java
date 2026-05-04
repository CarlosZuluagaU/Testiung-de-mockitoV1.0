package org.mock.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la entidad Player.
 * Cubre: constructores, getters, setters, equals, hashCode y toString (generados por Lombok).
 */
@DisplayName("Pruebas unitarias de Player")
class PlayerTest {

    @Nested
    @DisplayName("Constructores")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor sin argumentos crea un Player con valores null")
        void testNoArgsConstructor() {
            // ARRANGE + ACT
            Player player = new Player();

            // ASSERT
            assertNull(player.getId());
            assertNull(player.getName());
            assertNull(player.getTeam());
            assertNull(player.getPosition());
        }

        @Test
        @DisplayName("Constructor con argumentos asigna todos los valores correctamente")
        void testAllArgsConstructor() {
            // ARRANGE + ACT
            Player player = new Player(1L, "Lionel Messi", "Inter Miami", "Delantero");

            // ASSERT
            assertEquals(1L, player.getId());
            assertEquals("Lionel Messi", player.getName());
            assertEquals("Inter Miami", player.getTeam());
            assertEquals("Delantero", player.getPosition());
        }
    }

    @Nested
    @DisplayName("Getters y Setters")
    class GettersSettersTests {

        @Test
        @DisplayName("setId y getId funcionan correctamente")
        void testIdGetterSetter() {
            // ARRANGE
            Player player = new Player();

            // ACT
            player.setId(5L);

            // ASSERT
            assertEquals(5L, player.getId());
        }

        @Test
        @DisplayName("setName y getName funcionan correctamente")
        void testNameGetterSetter() {
            // ARRANGE
            Player player = new Player();

            // ACT
            player.setName("Cristiano Ronaldo");

            // ASSERT
            assertEquals("Cristiano Ronaldo", player.getName());
        }

        @Test
        @DisplayName("setTeam y getTeam funcionan correctamente")
        void testTeamGetterSetter() {
            // ARRANGE
            Player player = new Player();

            // ACT
            player.setTeam("Manchester United");

            // ASSERT
            assertEquals("Manchester United", player.getTeam());
        }

        @Test
        @DisplayName("setPosition y getPosition funcionan correctamente")
        void testPositionGetterSetter() {
            // ARRANGE
            Player player = new Player();

            // ACT
            player.setPosition("Portero");

            // ASSERT
            assertEquals("Portero", player.getPosition());
        }
    }

    @Nested
    @DisplayName("equals y hashCode")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Dos Players con los mismos datos son iguales")
        void testEqualsWithSameData() {
            // ARRANGE
            Player player1 = new Player(1L, "Messi", "Inter Miami", "Delantero");
            Player player2 = new Player(1L, "Messi", "Inter Miami", "Delantero");

            // ACT + ASSERT
            assertEquals(player1, player2);
        }

        @Test
        @DisplayName("Un Player no es igual a null")
        void testEqualsWithNull() {
            // ARRANGE
            Player player = new Player(1L, "Messi", "Inter Miami", "Delantero");

            // ACT + ASSERT
            assertNotEquals(player, null);
        }

        @Test
        @DisplayName("Un Player no es igual a otro objeto de diferente tipo")
        void testEqualsWithDifferentType() {
            // ARRANGE
            Player player = new Player(1L, "Messi", "Inter Miami", "Delantero");

            // ACT + ASSERT
            assertNotEquals(player, "Not a player");
        }

        @Test
        @DisplayName("Dos Players diferentes no son iguales")
        void testEqualsWithDifferentData() {
            // ARRANGE
            Player player1 = new Player(1L, "Messi", "Inter Miami", "Delantero");
            Player player2 = new Player(2L, "Ronaldo", "Manchester", "Delantero");

            // ACT + ASSERT
            assertNotEquals(player1, player2);
        }

        @Test
        @DisplayName("Players iguales tienen el mismo hashCode")
        void testHashCodeWithEqualPlayers() {
            // ARRANGE
            Player player1 = new Player(1L, "Messi", "Inter Miami", "Delantero");
            Player player2 = new Player(1L, "Messi", "Inter Miami", "Delantero");

            // ACT + ASSERT
            assertEquals(player1.hashCode(), player2.hashCode());
        }

        @Test
        @DisplayName("Un Player es igual a sí mismo")
        void testEqualsWithSelf() {
            // ARRANGE
            Player player = new Player(1L, "Messi", "Inter Miami", "Delantero");

            // ACT + ASSERT
            assertEquals(player, player);
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringTests {

        @Test
        @DisplayName("toString contiene todos los atributos del Player")
        void testToString() {
            // ARRANGE
            Player player = new Player(1L, "Messi", "Inter Miami", "Delantero");

            // ACT
            String result = player.toString();

            // ASSERT
            assertNotNull(result);
            assertTrue(result.contains("1"));
            assertTrue(result.contains("Messi"));
            assertTrue(result.contains("Inter Miami"));
            assertTrue(result.contains("Delantero"));
        }

        @Test
        @DisplayName("toString nunca es null")
        void testToStringNotNull() {
            // ARRANGE
            Player player = new Player();

            // ACT
            String result = player.toString();

            // ASSERT
            assertNotNull(result);
        }
    }
}
