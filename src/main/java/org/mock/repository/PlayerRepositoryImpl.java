package org.mock.repository;

import org.mock.exception.PlayerNotFoundException;
import org.mock.persistence.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerRepositoryImpl implements IPlayerRepository {

    private List<Player> playerDatabase = new ArrayList<>(List.of(
            // ── Delanteros ──────────────────────────────────────────────────
            new Player(1L,  "Lionel Messi",        "Inter Miami",      "Delantero"),
            new Player(2L,  "Cristiano Ronaldo",   "Al Nassr",         "Delantero"),
            new Player(3L,  "Kylian Mbappé",       "Real Madrid",      "Delantero"),
            new Player(4L,  "Erling Haaland",      "Manchester City",  "Delantero"),
            new Player(6L,  "Neymar Jr.",           "Al Hilal",         "Delantero"),
            new Player(8L,  "Pelé",                "Santos FC",        "Delantero"),
            new Player(12L, "Vinicius Jr.",         "Real Madrid",      "Delantero"),
            new Player(13L, "Robert Lewandowski",  "FC Barcelona",     "Delantero"),
            new Player(14L, "Harry Kane",          "Bayern München",   "Delantero"),
            new Player(15L, "Karim Benzema",       "Al Ittihad",       "Delantero"),
            new Player(16L, "Romelu Lukaku",       "Napoli",           "Delantero"),
            new Player(17L, "Gabriel Jesus",       "Arsenal",          "Delantero"),
            new Player(18L, "Luis Suárez",         "Inter Miami",      "Delantero"),
            new Player(19L, "Didier Drogba",       "Chelsea",          "Delantero"),
            new Player(20L, "Thierry Henry",       "Arsenal",          "Delantero"),
            new Player(21L, "Ronaldo Nazário",     "Real Madrid",      "Delantero"),
            new Player(22L, "Zlatan Ibrahimovic",  "AC Milan",         "Delantero"),
            // ── Centrocampistas ─────────────────────────────────────────────
            new Player(5L,  "Kevin De Bruyne",     "Manchester City",  "Centrocampista"),
            new Player(7L,  "Luka Modrić",         "Real Madrid",      "Centrocampista"),
            new Player(9L,  "Diego Maradona",      "Napoli",           "Centrocampista"),
            new Player(10L, "Zinédine Zidane",     "Real Madrid",      "Centrocampista"),
            new Player(11L, "Ronaldinho",          "FC Barcelona",     "Centrocampista"),
            new Player(23L, "Toni Kroos",          "Real Madrid",      "Centrocampista"),
            new Player(24L, "Pedri",               "FC Barcelona",     "Centrocampista"),
            new Player(25L, "Frenkie de Jong",     "FC Barcelona",     "Centrocampista"),
            new Player(26L, "N'Golo Kanté",        "Al Ittihad",       "Centrocampista"),
            new Player(27L, "Paul Pogba",          "Juventus",         "Centrocampista"),
            new Player(28L, "Xavi Hernández",      "FC Barcelona",     "Centrocampista"),
            new Player(29L, "Andrés Iniesta",      "FC Barcelona",     "Centrocampista"),
            new Player(30L, "Steven Gerrard",      "Liverpool",        "Centrocampista"),
            // ── Defensas ────────────────────────────────────────────────────
            new Player(31L, "Virgil van Dijk",     "Liverpool",        "Defensa"),
            new Player(32L, "Sergio Ramos",        "Sevilla",          "Defensa"),
            new Player(33L, "Trent Alexander-Arnold","Liverpool",      "Defensa"),
            new Player(34L, "Marquinhos",          "Paris SG",         "Defensa"),
            new Player(35L, "Raphaël Varane",      "Como",             "Defensa"),
            new Player(36L, "Paolo Maldini",       "AC Milan",         "Defensa"),
            new Player(37L, "Franz Beckenbauer",   "Bayern München",   "Defensa"),
            new Player(38L, "Carles Puyol",        "FC Barcelona",     "Defensa"),
            // ── Porteros ────────────────────────────────────────────────────
            new Player(39L, "Gianluigi Buffon",    "Juventus",         "Portero"),
            new Player(40L, "Manuel Neuer",        "Bayern München",   "Portero"),
            new Player(41L, "Thibaut Courtois",    "Real Madrid",      "Portero"),
            new Player(42L, "Alisson Becker",      "Liverpool",        "Portero"),
            new Player(43L, "Iker Casillas",       "Real Madrid",      "Portero"),
            new Player(44L, "Peter Schmeichel",    "Manchester United","Portero")
    ));

    @Override
    public List<Player> findAll() {
        System.out.println("-> findAll()");
        return this.playerDatabase;
    }

    @Override
    public Player findById(Long id) {
        System.out.println("-> findById(" + id + ")");
        return this.playerDatabase.stream()
                .filter(player -> player.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new PlayerNotFoundException(id));
    }

    @Override
    public void save(Player player) {
        System.out.println("-> save(" + player.getName() + ")");
        this.playerDatabase.add(player);
    }

    @Override
    public void update(Player updated) {
        System.out.println("-> update(" + updated.getName() + ")");
        Player existing = findById(updated.getId());
        existing.setName(updated.getName());
        existing.setTeam(updated.getTeam());
        existing.setPosition(updated.getPosition());
    }

    @Override
    public void deleteById(Long id) {
        System.out.println("-> deleteById(" + id + ")");
        if (!existsById(id)) throw new PlayerNotFoundException(id);
        this.playerDatabase = this.playerDatabase.stream()
                .filter(player -> !player.getId().equals(id))
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Player> findByTeam(String team) {
        System.out.println("-> findByTeam(" + team + ")");
        return this.playerDatabase.stream()
                .filter(player -> player.getTeam().equalsIgnoreCase(team))
                .toList();
    }

    @Override
    public List<Player> findByPosition(String position) {
        System.out.println("-> findByPosition(" + position + ")");
        return this.playerDatabase.stream()
                .filter(player -> player.getPosition().equalsIgnoreCase(position))
                .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return this.playerDatabase.stream()
                .anyMatch(player -> player.getId().equals(id));
    }
}
