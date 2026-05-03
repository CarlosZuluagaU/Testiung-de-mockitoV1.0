package org.mock;

import org.mock.exception.PlayerNotFoundException;
import org.mock.persistence.entity.Player;
import org.mock.repository.PlayerRepositoryImpl;
import org.mock.service.PlayerServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        PlayerRepositoryImpl playerRepository = new PlayerRepositoryImpl();
        PlayerServiceImpl playerService = new PlayerServiceImpl(playerRepository);

        List<Player> todos = playerService.findAll();

        // ── Estadísticas generales ───────────────────────────────────────────
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║       BASE DE DATOS DE JUGADORES             ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("Total de jugadores: " + todos.size());

        Map<String, Long> porPosicion = todos.stream()
                .collect(Collectors.groupingBy(Player::getPosition, Collectors.counting()));
        System.out.println("\nJugadores por posición:");
        porPosicion.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> System.out.printf("  %-20s %d%n", e.getKey() + ":", e.getValue()));

        Map<String, Long> porEquipo = todos.stream()
                .collect(Collectors.groupingBy(Player::getTeam, Collectors.counting()));
        System.out.println("\nEquipos con más jugadores:");
        porEquipo.entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> System.out.printf("  %-25s %d jugadores%n", e.getKey() + ":", e.getValue()));

        // ── Búsquedas por posición ───────────────────────────────────────────
        System.out.println("\n─── PORTEROS ───────────────────────────────────");
        playerService.findByPosition("Portero").forEach(p ->
                System.out.printf("  [%2d] %-25s %s%n", p.getId(), p.getName(), p.getTeam()));

        System.out.println("\n─── DEFENSAS ───────────────────────────────────");
        playerService.findByPosition("Defensa").forEach(p ->
                System.out.printf("  [%2d] %-25s %s%n", p.getId(), p.getName(), p.getTeam()));

        System.out.println("\n─── CENTROCAMPISTAS ────────────────────────────");
        playerService.findByPosition("Centrocampista").forEach(p ->
                System.out.printf("  [%2d] %-25s %s%n", p.getId(), p.getName(), p.getTeam()));

        System.out.println("\n─── DELANTEROS ─────────────────────────────────");
        playerService.findByPosition("Delantero").forEach(p ->
                System.out.printf("  [%2d] %-25s %s%n", p.getId(), p.getName(), p.getTeam()));

        // ── Búsqueda por equipo ──────────────────────────────────────────────
        System.out.println("\n─── REAL MADRID ────────────────────────────────");
        playerService.findByTeam("Real Madrid").forEach(p ->
                System.out.printf("  %-25s (%s)%n", p.getName(), p.getPosition()));

        System.out.println("\n─── FC BARCELONA ───────────────────────────────");
        playerService.findByTeam("FC Barcelona").forEach(p ->
                System.out.printf("  %-25s (%s)%n", p.getName(), p.getPosition()));

        System.out.println("\n─── LIVERPOOL ──────────────────────────────────");
        playerService.findByTeam("Liverpool").forEach(p ->
                System.out.printf("  %-25s (%s)%n", p.getName(), p.getPosition()));

        // ── CRUD ─────────────────────────────────────────────────────────────
        System.out.println("\n─── SAVE ───────────────────────────────────────");
        Player nuevo = new Player(50L, "Lamine Yamal", "FC Barcelona", "Delantero");
        playerService.save(nuevo);
        System.out.println("  Guardado: " + nuevo.getName() + " | Total: " + playerService.findAll().size());

        System.out.println("\n─── UPDATE ─────────────────────────────────────");
        playerService.update(new Player(50L, "Lamine Yamal", "FC Barcelona", "Extremo"));
        System.out.println("  Actualizado: " + playerService.findById(50L));

        System.out.println("\n─── DELETE ─────────────────────────────────────");
        playerService.deleteById(50L);
        System.out.println("  Eliminado id=50. Total: " + playerService.findAll().size());

        // ── Excepción ────────────────────────────────────────────────────────
        System.out.println("\n─── EXCEPCION (id=999) ─────────────────────────");
        try {
            playerService.findById(999L);
        } catch (PlayerNotFoundException e) {
            System.out.println("  " + e.getMessage());
        }

        // --- Uso intencional de secretos hardcodeados y material sensible expuesto ---
        System.out.println("Clave hardcodeada: " + SECRET);
        System.out.println("Password admin hardcodeada: " + ADMIN_PASSWORD);
        System.out.println("JWT secret hardcodeado: " + JWT_SECRET);
        String token = generateToken();
        System.out.println("Token inseguro generado: " + token);

                printSecurityBlock();
                printSecurityBlockCopy();
    }

    // Clave hardcodeada intencional (mala práctica de seguridad)
    private static final String SECRET = "hardcoded_secret_123";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String JWT_SECRET = "jwt_secret_for_demo_only";

    // Generador de token inseguro (usa java.util.Random y MD5)
    public static String generateToken() {
        Random r = new Random(); // no es criptográficamente seguro
        byte[] bytes = new byte[16];
        r.nextBytes(bytes);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

        private static void printSecurityBlock() {
                System.out.println("Security block: using hardcoded values for demo");
                System.out.println("Security block token: " + SECRET);
        }

        private static void printSecurityBlockCopy() {
                System.out.println("Security block: using hardcoded values for demo");
                System.out.println("Security block token: " + SECRET);
        }
}
