package org.mock.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mock.DataProvider;
import org.mock.exception.PlayerNotFoundException;
import org.mock.persistence.entity.Player;
import org.mock.repository.IPlayerRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de PlayerServiceImpl usando el patrón AAA:
 *   - Arrange  (// Given)  → preparar datos y configurar mocks
 *   - Act      (// When)   → ejecutar el método bajo prueba
 *   - Assert   (// Then)   → verificar resultados e interacciones
 *
 * Conceptos de Mockito aplicados:
 *   @Mock            → crea un doble de prueba de IPlayerRepository
 *   @InjectMocks     → inyecta los @Mock en PlayerServiceImpl automáticamente
 *   @ExtendWith      → integra Mockito con JUnit 5
 *   when/thenReturn  → stubbing: define qué devuelve el mock
 *   when/thenThrow   → stubbing de excepciones
 *   doThrow/when     → stubbing de excepciones en métodos void
 *   doNothing/when   → stubbing explícito de métodos void (no hace nada)
 *   verify           → comprueba que se llamó al mock con los argumentos correctos
 *   times(n)         → verifica que se llamó exactamente n veces
 *   never()          → verifica que NUNCA se llamó
 *   ArgumentCaptor   → captura el argumento que se pasó al mock para inspeccionarlo
 *   ArgumentMatchers → any(), anyLong(), anyString() para coincidencias flexibles
 *   verifyNoMoreInteractions → asegura que no hubo más llamadas al mock
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de PlayerServiceImpl")
class PlayerServiceImplTest {

    // @Mock crea automáticamente un mock de IPlayerRepository
    @Mock
    private IPlayerRepository playerRepository;

    // @InjectMocks crea PlayerServiceImpl e inyecta el @Mock anterior
    @InjectMocks
    private PlayerServiceImpl playerService;

    // ─────────────────────────────────────────────────────────────────────────
    // Con @ExtendWith + @Mock + @InjectMocks ya no necesitamos @BeforeEach.
    // MockitoExtension inicializa los mocks antes de cada test automáticamente.
    // ─────────────────────────────────────────────────────────────────────────

    // =========================================================================
    // findAll
    // =========================================================================

    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("Debe retornar la lista completa de jugadores")
        void testFindAll() {
            // ARRANGE – configuramos qué devolverá el mock
            when(playerRepository.findAll()).thenReturn(DataProvider.playerListMock());

            // ACT – ejecutamos el método bajo prueba
            List<Player> result = playerService.findAll();

            // ASSERT – verificamos el resultado y la interacción con el mock
            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(11, result.size());
            assertEquals("Lionel Messi", result.get(0).getName());
            verify(playerRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay jugadores")
        void testFindAll_Empty() {
            // ARRANGE
            when(playerRepository.findAll()).thenReturn(Collections.emptyList());

            // ACT
            List<Player> result = playerService.findAll();

            // ASSERT
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(playerRepository).findAll();
        }
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Nested
    @DisplayName("findById()")
    class FindByIdTests {

        @Test
        @DisplayName("Debe retornar el jugador cuando existe el id")
        void testFindById() {
            // ARRANGE
            when(playerRepository.findById(1L)).thenReturn(DataProvider.playerMock());

            // ACT
            Player player = playerService.findById(1L);

            // ASSERT
            assertNotNull(player);
            assertEquals(1L, player.getId());
            assertEquals("Lionel Messi", player.getName());
            // ArgumentMatchers: anyLong() acepta cualquier long
            verify(playerRepository, times(1)).findById(anyLong());
        }

        @Test
        @DisplayName("Debe lanzar PlayerNotFoundException cuando el id no existe")
        void testFindById_NotFound() {
            // ARRANGE – configuramos el mock para que lance excepción
            when(playerRepository.findById(99L))
                    .thenThrow(new PlayerNotFoundException(99L));

            // ACT + ASSERT – assertThrows captura la excepción esperada
            PlayerNotFoundException ex = assertThrows(
                    PlayerNotFoundException.class,
                    () -> playerService.findById(99L)
            );

            assertTrue(ex.getMessage().contains("99"));
            verify(playerRepository).findById(99L);
        }
    }

    // =========================================================================
    // save
    // =========================================================================

    @Nested
    @DisplayName("save()")
    class SaveTests {

        @Test
        @DisplayName("Debe guardar el jugador y delegar al repositorio")
        void testSave() {
            // ARRANGE – doNothing es explícito: el mock no hace nada en save()
            // (es el comportamiento por defecto de void, pero es buena práctica documentarlo)
            Player player = DataProvider.newPlayerMock();
            doNothing().when(playerRepository).save(any(Player.class));

            // ACT
            playerService.save(player);

            // ASSERT – ArgumentCaptor captura el argumento que se pasó al mock
            ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
            verify(playerRepository).save(captor.capture());

            Player captured = captor.getValue();
            assertEquals(12L, captured.getId());
            assertEquals("David Cardona", captured.getName());
            assertEquals("Nacional", captured.getTeam());
        }
    }

    // =========================================================================
    // update
    // =========================================================================

    @Nested
    @DisplayName("update()")
    class UpdateTests {

        @Test
        @DisplayName("Debe actualizar el jugador y delegar al repositorio")
        void testUpdate() {
            // ARRANGE
            Player updated = DataProvider.updatedPlayerMock();
            doNothing().when(playerRepository).update(any(Player.class));

            // ACT
            playerService.update(updated);

            // ASSERT
            ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
            verify(playerRepository).update(captor.capture());

            assertEquals(1L, captor.getValue().getId());
            assertEquals("FC Barcelona", captor.getValue().getTeam());
            // verifyNoMoreInteractions: garantiza que no se llamó a ningún otro método del mock
            verifyNoMoreInteractions(playerRepository);
        }
    }

    // =========================================================================
    // deleteById
    // =========================================================================

    @Nested
    @DisplayName("deleteById()")
    class DeleteByIdTests {

        @Test
        @DisplayName("Debe eliminar el jugador cuando existe el id")
        void testDeleteById() {
            // ARRANGE
            Long id = 1L;
            doNothing().when(playerRepository).deleteById(anyLong());

            // ACT
            playerService.deleteById(id);

            // ASSERT
            verify(playerRepository, times(1)).deleteById(id);
        }

        @Test
        @DisplayName("Debe lanzar PlayerNotFoundException al eliminar un id inexistente")
        void testDeleteById_NotFound() {
            // ARRANGE – doThrow se usa para stubs de métodos void
            doThrow(new PlayerNotFoundException(99L))
                    .when(playerRepository).deleteById(99L);

            // ACT + ASSERT
            assertThrows(PlayerNotFoundException.class,
                    () -> playerService.deleteById(99L));

            verify(playerRepository).deleteById(99L);
        }
    }

    // =========================================================================
    // findByTeam
    // =========================================================================

    @Nested
    @DisplayName("findByTeam()")
    class FindByTeamTests {

        @Test
        @DisplayName("Debe retornar los jugadores del equipo indicado")
        void testFindByTeam() {
            // ARRANGE – anyString() acepta cualquier String (ArgumentMatcher)
            when(playerRepository.findByTeam(anyString()))
                    .thenReturn(DataProvider.realMadridPlayersMock());

            // ACT
            List<Player> result = playerService.findByTeam("Real Madrid");

            // ASSERT
            assertNotNull(result);
            assertEquals(7, result.size());
            assertTrue(result.stream().allMatch(p -> p.getTeam().equals("Real Madrid")));
            verify(playerRepository).findByTeam("Real Madrid");
        }

        @Test
        @DisplayName("Debe retornar lista vacía para un equipo sin jugadores")
        void testFindByTeam_Empty() {
            // ARRANGE
            when(playerRepository.findByTeam("Equipo Desconocido"))
                    .thenReturn(Collections.emptyList());

            // ACT
            List<Player> result = playerService.findByTeam("Equipo Desconocido");

            // ASSERT
            assertTrue(result.isEmpty());
            verify(playerRepository).findByTeam("Equipo Desconocido");
        }
    }

    // =========================================================================
    // findByPosition
    // =========================================================================

    @Nested
    @DisplayName("findByPosition()")
    class FindByPositionTests {

        @Test
        @DisplayName("Debe retornar todos los jugadores de la posición indicada")
        void testFindByPosition() {
            // ARRANGE
            when(playerRepository.findByPosition("Delantero"))
                    .thenReturn(DataProvider.delanterosMock());

            // ACT
            List<Player> result = playerService.findByPosition("Delantero");

            // ASSERT
            assertNotNull(result);
            assertEquals(8, result.size());
            assertTrue(result.stream().allMatch(p -> p.getPosition().equals("Delantero")));
            verify(playerRepository, times(1)).findByPosition("Delantero");
        }
    }

    // =========================================================================
    // existsById
    // =========================================================================

    @Nested
    @DisplayName("existsById()")
    class ExistsByIdTests {

        @Test
        @DisplayName("Debe retornar true si el jugador existe")
        void testExistsById_True() {
            // ARRANGE
            when(playerRepository.existsById(1L)).thenReturn(true);

            // ACT
            boolean result = playerService.existsById(1L);

            // ASSERT
            assertTrue(result);
            verify(playerRepository, times(1)).existsById(1L);
        }

        @Test
        @DisplayName("Debe retornar false si el jugador no existe")
        void testExistsById_False() {
            // ARRANGE
            when(playerRepository.existsById(99L)).thenReturn(false);

            // ACT
            boolean result = playerService.existsById(99L);

            // ASSERT
            assertFalse(result);
            verify(playerRepository, times(1)).existsById(99L);
        }

        @Test
        @DisplayName("No debe llamar a otros métodos al verificar existencia")
        void testExistsById_NoOtherInteractions() {
            // ARRANGE
            when(playerRepository.existsById(anyLong())).thenReturn(true);

            // ACT
            playerService.existsById(5L);

            // ASSERT – never() verifica que findAll() nunca fue llamado
            verify(playerRepository, never()).findAll();
            verify(playerRepository, never()).findById(anyLong());
            verifyNoMoreInteractions(playerRepository);
        }
    }
}
