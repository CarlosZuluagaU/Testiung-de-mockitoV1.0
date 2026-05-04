package org.mock;

import org.mock.persistence.entity.Player;
import org.mock.repository.PlayerRepositoryImpl;
import org.mock.service.PlayerServiceImpl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {
        private static final SecureRandom SECURE_RANDOM = new SecureRandom();
        private static final int TOKEN_SIZE_BYTES = 32;
        private static final List<String> POSITION_ORDER = List.of(
                        "Portero",
                        "Defensa",
                        "Centrocampista",
                        "Delantero"
        );
        private static final List<String> TEAM_ORDER = List.of(
                        "Real Madrid",
                        "FC Barcelona",
                        "Liverpool"
        );

        public static void main(String[] args) {
                PlayerServiceImpl playerService = new PlayerServiceImpl(new PlayerRepositoryImpl());
                List<Player> players = playerService.findAll();

                printHeader();
                printStatistics(players);
                printPlayersByPosition(playerService);
                printPlayersByTeam(playerService);
                runCrudDemo(playerService);
                runMissingPlayerDemo(playerService);
                printSecureTokenDemo();
        }

        private static void printHeader() {
                System.out.println("╔══════════════════════════════════════════════╗");
                System.out.println("║       BASE DE DATOS DE JUGADORES             ║");
                System.out.println("╚══════════════════════════════════════════════╝");
        }

        private static void printStatistics(List<Player> players) {
                System.out.println("Total de jugadores: " + players.size());

                printGroupedCounts(
                                "\nJugadores por posición:",
                                groupBy(players, Player::getPosition),
                                false,
                                "  %-20s %d%n"
                );

                printGroupedCounts(
                                "\nEquipos con más jugadores:",
                                groupBy(players, Player::getTeam),
                                true,
                                "  %-25s %d jugadores%n"
                );
        }

        private static Map<String, Long> groupBy(List<Player> players, Function<Player, String> classifier) {
                return players.stream().collect(Collectors.groupingBy(classifier, Collectors.counting()));
        }

        private static void printGroupedCounts(
                        String title,
                        Map<String, Long> counts,
                        boolean onlyMoreThanOne,
                        String format
        ) {
                System.out.println(title);
                counts.entrySet().stream()
                                .filter(entry -> !onlyMoreThanOne || entry.getValue() > 1)
                                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                                                .thenComparing(Map.Entry.comparingByKey()))
                                .forEach(entry -> System.out.printf(format, entry.getKey() + ":", entry.getValue()));
        }

        private static void printPlayersByPosition(PlayerServiceImpl playerService) {
                POSITION_ORDER.forEach(position -> printPositionSection(playerService, position));
        }

        private static void printPositionSection(PlayerServiceImpl playerService, String position) {
                System.out.println("\n─── " + position.toUpperCase() + " ─────────────────────────────────");
                playerService.findByPosition(position).forEach(player ->
                                System.out.printf("  [%2d] %-25s %s%n", player.getId(), player.getName(), player.getTeam()));
        }

        private static void printPlayersByTeam(PlayerServiceImpl playerService) {
                TEAM_ORDER.forEach(team -> printTeamSection(playerService, team));
        }

        private static void printTeamSection(PlayerServiceImpl playerService, String team) {
                System.out.println("\n─── " + team.toUpperCase() + " ───────────────────────────────");
                playerService.findByTeam(team).forEach(player ->
                                System.out.printf("  %-25s (%s)%n", player.getName(), player.getPosition()));
        }

        private static void runCrudDemo(PlayerServiceImpl playerService) {
                System.out.println("\n─── SAVE ───────────────────────────────────────");
                Player player = new Player(50L, "Lamine Yamal", "FC Barcelona", "Delantero");
                playerService.save(player);
                System.out.println("  Guardado: " + player.getName() + " | Total: " + playerService.findAll().size());

                System.out.println("\n─── UPDATE ─────────────────────────────────────");
                playerService.update(new Player(50L, "Lamine Yamal", "FC Barcelona", "Extremo"));
                System.out.println("  Actualizado: " + playerService.findById(50L));

                System.out.println("\n─── DELETE ─────────────────────────────────────");
                playerService.deleteById(50L);
                System.out.println("  Eliminado id=50. Total: " + playerService.findAll().size());
        }

        private static void runMissingPlayerDemo(PlayerServiceImpl playerService) {
                System.out.println("\n─── EXCEPCION (id=999) ─────────────────────────");
                try {
                        playerService.findById(999L);
                } catch (org.mock.exception.PlayerNotFoundException exception) {
                        System.out.println("  " + exception.getMessage());
                }
        }

        private static void printSecureTokenDemo() {
                String token = generateToken();
                System.out.println("\nToken seguro generado: " + token);
        }

        public static String generateToken() {
                byte[] bytes = new byte[TOKEN_SIZE_BYTES];
                SECURE_RANDOM.nextBytes(bytes);

                try {
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        byte[] hashedBytes = digest.digest(bytes);
                        return toHex(hashedBytes);
                } catch (NoSuchAlgorithmException exception) {
                        throw new IllegalStateException("No se pudo generar un token seguro.", exception);
                }
        }

        private static String toHex(byte[] bytes) {
                StringBuilder builder = new StringBuilder(bytes.length * 2);
                for (byte value : bytes) {
                        builder.append(String.format("%02x", value));
                }
                return builder.toString();
        }
}
