package org.mock;

import org.mock.exception.PlayerNotFoundException;
import org.mock.persistence.entity.Player;
import org.mock.repository.PlayerRepositoryImpl;
import org.mock.service.PlayerServiceImpl;

import java.util.List;
import java.util.Map;
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
    }
}
