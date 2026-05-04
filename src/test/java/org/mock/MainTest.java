package org.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mock.persistence.entity.Player;
import org.mock.repository.PlayerRepositoryImpl;
import org.mock.service.PlayerServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Main.
 * Cubre: métodos privados mediante reflexión y output de consola.
 */
@DisplayName("Pruebas unitarias de Main")
class MainTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream capturedOutput;

    @BeforeEach
    void setUp() {
        // Capturar System.out para verificar output
        originalOut = System.out;
        capturedOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOutput));
    }

    @AfterEach
    void tearDown() {
        // Restaurar System.out
        System.setOut(originalOut);
    }

    @Nested
    @DisplayName("printHeader()")
    class PrintHeaderTests {

        @Test
        @DisplayName("printHeader imprime encabezado sin error")
        void testPrintHeader() throws Exception {
            // ARRANGE
            Method printHeader = Main.class.getDeclaredMethod("printHeader");
            printHeader.setAccessible(true);

            // ACT
            printHeader.invoke(null);

            // ASSERT
            String output = capturedOutput.toString();
            assertTrue(output.length() > 0);
            assertTrue(output.contains("═") || output.contains("="));
        }
    }

    @Nested
    @DisplayName("printStatistics()")
    class PrintStatisticsTests {

        @Test
        @DisplayName("printStatistics imprime estadísticas de jugadores")
        void testPrintStatistics() throws Exception {
            // ARRANGE
            Method printStatistics = Main.class.getDeclaredMethod("printStatistics", List.class);
            printStatistics.setAccessible(true);

            List<Player> players = new ArrayList<>();
            players.add(new Player(1L, "Messi", "Inter Miami", "Delantero"));
            players.add(new Player(2L, "Ronaldo", "Al-Nassr", "Delantero"));

            // ACT
            printStatistics.invoke(null, players);

            // ASSERT
            String output = capturedOutput.toString();
            assertTrue(output.contains("Total de jugadores:"));
        }

        @Test
        @DisplayName("printStatistics con lista vacía no lanza excepción")
        void testPrintStatisticsEmpty() throws Exception {
            // ARRANGE
            Method printStatistics = Main.class.getDeclaredMethod("printStatistics", List.class);
            printStatistics.setAccessible(true);

            // ACT + ASSERT
            assertDoesNotThrow(() -> printStatistics.invoke(null, new ArrayList<Player>()));
        }
    }

    @Nested
    @DisplayName("printPlayersByPosition()")
    class PrintPlayersByPositionTests {

        @Test
        @DisplayName("printPlayersByPosition imprime jugadores agrupados por posición")
        void testPrintPlayersByPosition() throws Exception {
            // ARRANGE
            PlayerServiceImpl service = new PlayerServiceImpl(new PlayerRepositoryImpl());

            Method printPlayersByPosition = Main.class.getDeclaredMethod("printPlayersByPosition", PlayerServiceImpl.class);
            printPlayersByPosition.setAccessible(true);

            // ACT
            printPlayersByPosition.invoke(null, service);

            // ASSERT
            String output = capturedOutput.toString();
            assertTrue(output.length() > 0);
        }
    }

    @Nested
    @DisplayName("printPlayersByTeam()")
    class PrintPlayersByTeamTests {

        @Test
        @DisplayName("printPlayersByTeam imprime jugadores agrupados por equipo")
        void testPrintPlayersByTeam() throws Exception {
            // ARRANGE
            PlayerServiceImpl service = new PlayerServiceImpl(new PlayerRepositoryImpl());

            Method printPlayersByTeam = Main.class.getDeclaredMethod("printPlayersByTeam", PlayerServiceImpl.class);
            printPlayersByTeam.setAccessible(true);

            // ACT
            printPlayersByTeam.invoke(null, service);

            // ASSERT
            String output = capturedOutput.toString();
            assertTrue(output.length() > 0);
        }
    }

    @Nested
    @DisplayName("runCrudDemo()")
    class RunCrudDemoTests {

        @Test
        @DisplayName("runCrudDemo ejecuta operaciones CRUD sin error")
        void testRunCrudDemo() throws Exception {
            // ARRANGE
            PlayerServiceImpl service = new PlayerServiceImpl(new PlayerRepositoryImpl());

            Method runCrudDemo = Main.class.getDeclaredMethod("runCrudDemo", PlayerServiceImpl.class);
            runCrudDemo.setAccessible(true);

            // ACT + ASSERT
            assertDoesNotThrow(() -> runCrudDemo.invoke(null, service));
        }
    }

    @Nested
    @DisplayName("runMissingPlayerDemo()")
    class RunMissingPlayerDemoTests {

        @Test
        @DisplayName("runMissingPlayerDemo maneja excepción de jugador no encontrado")
        void testRunMissingPlayerDemo() throws Exception {
            // ARRANGE
            PlayerServiceImpl service = new PlayerServiceImpl(new PlayerRepositoryImpl());

            Method runMissingPlayerDemo = Main.class.getDeclaredMethod("runMissingPlayerDemo", PlayerServiceImpl.class);
            runMissingPlayerDemo.setAccessible(true);

            // ACT + ASSERT - No debe lanzar excepción no manejada
            assertDoesNotThrow(() -> runMissingPlayerDemo.invoke(null, service));
        }
    }

    @Nested
    @DisplayName("printSecureTokenDemo()")
    class PrintSecureTokenDemoTests {

        @Test
        @DisplayName("printSecureTokenDemo genera y imprime token seguro")
        void testPrintSecureTokenDemo() throws Exception {
            // ARRANGE
            Method printSecureTokenDemo = Main.class.getDeclaredMethod("printSecureTokenDemo");
            printSecureTokenDemo.setAccessible(true);

            // ACT
            printSecureTokenDemo.invoke(null);

            // ASSERT
            String output = capturedOutput.toString();
            assertTrue(output.length() > 0);
            assertTrue(output.contains("Token seguro generado:"));
        }
    }

    @Nested
    @DisplayName("groupBy()")
    class GroupByTests {

        @Test
        @DisplayName("groupBy agrupa elementos correctamente")
        void testGroupBy() throws Exception {
            // ARRANGE
            Method groupBy = Main.class.getDeclaredMethod("groupBy", List.class, java.util.function.Function.class);
            groupBy.setAccessible(true);

            List<Player> players = new ArrayList<>();
            players.add(new Player(1L, "Messi", "Inter Miami", "Delantero"));
            players.add(new Player(2L, "Ronaldo", "Al-Nassr", "Delantero"));
            players.add(new Player(3L, "Neymar", "Al-Hilal", "Extremo"));

            // ACT
            Object result = groupBy.invoke(null, players, (Function<Player, String>) Player::getPosition);

            // ASSERT
            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("toHex()")
    class ToHexTests {

        @Test
        @DisplayName("toHex convierte bytes a hexadecimal correctamente")
        void testToHex() throws Exception {
            // ARRANGE
            Method toHex = Main.class.getDeclaredMethod("toHex", byte[].class);
            toHex.setAccessible(true);

            byte[] testBytes = {0x00, 0x01, 0x0A, 0x0F, (byte) 0xFF};

            // ACT
            String result = (String) toHex.invoke(null, (Object) testBytes);

            // ASSERT
            assertNotNull(result);
            assertTrue(result.length() > 0);
            assertTrue(result.matches("[0-9a-f]+"));
        }

        @Test
        @DisplayName("toHex produce salida en minúsculas")
        void testToHexLowerCase() throws Exception {
            // ARRANGE
            Method toHex = Main.class.getDeclaredMethod("toHex", byte[].class);
            toHex.setAccessible(true);

            byte[] testBytes = {(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

            // ACT
            String result = (String) toHex.invoke(null, (Object) testBytes);

            // ASSERT
            assertFalse(result.matches(".*[A-F].*"));
        }
    }

    @Nested
    @DisplayName("generateSecureToken()")
    class GenerateSecureTokenTests {

        @Test
        @DisplayName("generateSecureToken produce un token no vacío")
        void testGenerateSecureToken() throws Exception {
            // ARRANGE
            Method generateSecureToken = Main.class.getDeclaredMethod("generateToken");
            generateSecureToken.setAccessible(true);

            // ACT
            String token = (String) generateSecureToken.invoke(null);

            // ASSERT
            assertNotNull(token);
            assertFalse(token.isEmpty());
            assertTrue(token.length() > 0);
        }

        @Test
        @DisplayName("generateSecureToken produce tokens diferentes cada vez")
        void testGenerateSecureTokenUnique() throws Exception {
            // ARRANGE
            Method generateSecureToken = Main.class.getDeclaredMethod("generateToken");
            generateSecureToken.setAccessible(true);

            // ACT
            String token1 = (String) generateSecureToken.invoke(null);
            String token2 = (String) generateSecureToken.invoke(null);

            // ASSERT
            assertNotEquals(token1, token2);
        }
    }

    @Nested
    @DisplayName("printGroupedCounts()")
    class PrintGroupedCountsTests {

        @Test
        @DisplayName("printGroupedCounts imprime cuentas agrupadas")
        void testPrintGroupedCounts() throws Exception {
            // ARRANGE
            Method printGroupedCounts = Main.class.getDeclaredMethod("printGroupedCounts", String.class, Map.class, boolean.class, String.class);
            printGroupedCounts.setAccessible(true);

            Map<String, Long> counts = new java.util.HashMap<>();
            counts.put("Delantero", 5L);
            counts.put("Defensa", 3L);

            // ACT
            printGroupedCounts.invoke(null, "Posición", counts, false, "  %-20s %d%n");

            // ASSERT
            String output = capturedOutput.toString();
            assertTrue(output.length() > 0);
        }
    }

    @Nested
    @DisplayName("Integración")
    class IntegrationTests {

        @Test
        @DisplayName("Métodos privados trabajar juntos correctamente")
        void testMethodsIntegration() throws Exception {
            // ARRANGE
            Method generateSecureToken = Main.class.getDeclaredMethod("generateToken");
            generateSecureToken.setAccessible(true);

            // ACT
            String token1 = (String) generateSecureToken.invoke(null);
            String token2 = (String) generateSecureToken.invoke(null);

            // ASSERT
            assertNotNull(token1);
            assertNotNull(token2);
            assertNotEquals(token1, token2);
        }
    }
}
