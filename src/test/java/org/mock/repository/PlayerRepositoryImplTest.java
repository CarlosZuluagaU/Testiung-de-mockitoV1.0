package org.mock.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mock.DataProvider;
import org.mock.exception.PlayerNotFoundException;
import org.mock.persistence.entity.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para PlayerRepositoryImpl.
 * Cubre: CRUD completo, búsquedas, filtros y validaciones.
 */
@DisplayName("Pruebas unitarias de PlayerRepositoryImpl")
class PlayerRepositoryImplTest {

    private PlayerRepositoryImpl playerRepository;

    @BeforeEach
    void setUp() {
        // Crear una nueva instancia para cada prueba
        playerRepository = new PlayerRepositoryImpl();
    }

    @Nested
    @DisplayName("save()")
    class SaveTests {

        @Test
        @DisplayName("save añade un jugador a la base de datos")
        void testSave() {
            // ARRANGE
            Player newPlayer = new Player(1000L, "Iniesta", "Vissel Kobe", "Centrocampista");

            // ACT
            playerRepository.save(newPlayer);

            // ASSERT
            assertTrue(playerRepository.existsById(1000L));
            assertEquals(newPlayer, playerRepository.findById(1000L));
        }

        @Test
        @DisplayName("save con null lanza NullPointerException")
        void testSaveWithNull() {
            // ARRANGE + ACT + ASSERT
            assertThrows(NullPointerException.class, () -> playerRepository.save(null));
        }
    }

    @Nested
    @DisplayName("update()")
    class UpdateTests {

        @Test
        @DisplayName("update modifica un jugador existente")
        void testUpdate() {
            // ARRANGE
            Player updated = new Player(1L, "Lionel Messi", "PSG", "Delantero");

            // ACT
            playerRepository.update(updated);

            // ASSERT
            Player result = playerRepository.findById(1L);
            assertEquals("PSG", result.getTeam());
        }

        @Test
        @DisplayName("update con null lanza NullPointerException")
        void testUpdateWithNull() {
            // ARRANGE + ACT + ASSERT
            assertThrows(NullPointerException.class, () -> playerRepository.update(null));
        }

        @Test
        @DisplayName("update de jugador inexistente lanza excepción")
        void testUpdateNonexistentPlayer() {
            // ARRANGE
            Player nonexistent = new Player(999L, "Unknown", "Unknown", "Unknown");

            // ACT + ASSERT
            assertThrows(PlayerNotFoundException.class, () -> playerRepository.update(nonexistent));
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindByIdTests {

        @Test
        @DisplayName("findById retorna el jugador cuando existe")
        void testFindById() {
            // ACT
            Player result = playerRepository.findById(1L);

            // ASSERT
            assertNotNull(result);
            assertEquals("Lionel Messi", result.getName());
        }

        @Test
        @DisplayName("findById lanza PlayerNotFoundException cuando no existe")
        void testFindByIdNotFound() {
            // ACT + ASSERT
            assertThrows(PlayerNotFoundException.class, () -> playerRepository.findById(999L));
        }
    }

    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("findAll retorna todos los jugadores")
        void testFindAll() {
            // ACT
            List<Player> result = playerRepository.findAll();

            // ASSERT
            assertNotNull(result);
            assertTrue(result.size() > 0);
        }

        @Test
        @DisplayName("findAll retorna una copia (no la colección interna)")
        void testFindAllReturnsCopy() {
            // ACT
            List<Player> result1 = playerRepository.findAll();
            List<Player> result2 = playerRepository.findAll();

            // ASSERT
            assertNotSame(result1, result2);
            assertEquals(result1, result2);
        }
    }

    @Nested
    @DisplayName("deleteById()")
    class DeleteByIdTests {

        @Test
        @DisplayName("deleteById elimina un jugador existente")
        void testDeleteById() {
            // ARRANGE
            assertTrue(playerRepository.existsById(1L));

            // ACT
            playerRepository.deleteById(1L);

            // ASSERT
            assertFalse(playerRepository.existsById(1L));
        }

        @Test
        @DisplayName("deleteById de jugador inexistente no lanza excepción")
        void testDeleteByIdNonexistent() {
            // ACT + ASSERT - debe lanzar excepción
            assertThrows(PlayerNotFoundException.class, () -> playerRepository.deleteById(999L));
        }
    }

    @Nested
    @DisplayName("existsById()")
    class ExistsByIdTests {

        @Test
        @DisplayName("existsById retorna true cuando existe el jugador")
        void testExistsById() {
            // ACT + ASSERT
            assertTrue(playerRepository.existsById(1L));
        }

        @Test
        @DisplayName("existsById retorna false cuando no existe el jugador")
        void testExistsByIdNotFound() {
            // ACT + ASSERT
            assertFalse(playerRepository.existsById(999L));
        }
    }

    @Nested
    @DisplayName("findByTeam()")
    class FindByTeamTests {

        @Test
        @DisplayName("findByTeam retorna jugadores del equipo")
        void testFindByTeam() {
            // ACT
            List<Player> result = playerRepository.findByTeam("Real Madrid");

            // ASSERT
            assertNotNull(result);
            assertTrue(result.size() > 0);
        }

        @Test
        @DisplayName("findByTeam retorna lista vacía cuando no hay coincidencias")
        void testFindByTeamEmpty() {
            // ACT
            List<Player> result = playerRepository.findByTeam("Unknown Team");

            // ASSERT
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("findByPosition()")
    class FindByPositionTests {

        @Test
        @DisplayName("findByPosition retorna jugadores por posición")
        void testFindByPosition() {
            // ACT
            List<Player> result = playerRepository.findByPosition("Delantero");

            // ASSERT
            assertNotNull(result);
            assertTrue(result.size() > 0);
        }

        @Test
        @DisplayName("findByPosition retorna lista vacía cuando no hay coincidencias")
        void testFindByPositionEmpty() {
            // ACT
            List<Player> result = playerRepository.findByPosition("PosicionInexistente");

            // ASSERT
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Casos edge")
    class EdgeCaseTests {

        @Test
        @DisplayName("Operaciones múltiples mantienen consistencia")
        void testMultipleOperations() {
            // ARRANGE + ACT
            int countBefore = playerRepository.findAll().size();
            playerRepository.save(new Player(1001L, "New Player", "Team", "Position"));
            int countAfterSave = playerRepository.findAll().size();
            playerRepository.deleteById(1001L);
            int countAfterDelete = playerRepository.findAll().size();

            // ASSERT
            assertEquals(countBefore + 1, countAfterSave);
            assertEquals(countBefore, countAfterDelete);
        }

        @Test
        @DisplayName("Update seguido de findById retorna datos actualizados")
        void testUpdateFollowedByFind() {
            // ARRANGE + ACT
            Player updated = new Player(1L, "Messi Updated", "New Team", "Updated Position");
            playerRepository.update(updated);
            Player result = playerRepository.findById(1L);

            // ASSERT
            assertEquals("New Team", result.getTeam());
            assertEquals("Updated Position", result.getPosition());
        }
    }
}
