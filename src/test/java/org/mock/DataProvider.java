package org.mock;

import org.mock.persistence.entity.Player;

import java.util.List;

public class DataProvider {

    public static List<Player> playerListMock() {
        return List.of(
                new Player(1L, "Lionel Messi", "Inter Miami", "Delantero"),
                new Player(2L, "Cristiano Ronaldo", "Al Nassr", "Delantero"),
                new Player(3L, "Kylian Mbappé", "Real Madrid", "Delantero"),
                new Player(4L, "Erling Haaland", "Manchester City", "Delantero"),
                new Player(5L, "Kevin De Bruyne", "Manchester City", "Centrocampista"),
                new Player(6L, "Neymar Jr.", "Al Hilal", "Delantero"),
                new Player(7L, "Luka Modrić", "Real Madrid", "Centrocampista"),
                new Player(8L, "Pelé", "Santos FC", "Delantero"),
                new Player(9L, "Diego Maradona", "Napoli", "Centrocampista"),
                new Player(10L, "Zinédine Zidane", "Real Madrid", "Centrocampista"),
                new Player(11L, "Ronaldinho", "FC Barcelona", "Centrocampista")
        );
    }

    public static Player playerMock() {
        return new Player(1L, "Lionel Messi", "Inter Miami", "Delantero");
    }

    public static Player newPlayerMock() {
        return new Player(12L, "David Cardona", "Nacional", "Medio Campo");
    }

    public static Player updatedPlayerMock() {
        return new Player(1L, "Lionel Messi", "FC Barcelona", "Delantero");
    }

    public static List<Player> realMadridPlayersMock() {
        return List.of(
                new Player(3L,  "Kylian Mbappé",   "Real Madrid", "Delantero"),
                new Player(7L,  "Luka Modrić",     "Real Madrid", "Centrocampista"),
                new Player(10L, "Zinédine Zidane", "Real Madrid", "Centrocampista"),
                new Player(12L, "Vinicius Jr.",    "Real Madrid", "Delantero"),
                new Player(23L, "Toni Kroos",      "Real Madrid", "Centrocampista"),
                new Player(41L, "Thibaut Courtois","Real Madrid", "Portero"),
                new Player(43L, "Iker Casillas",   "Real Madrid", "Portero")
        );
    }

    public static List<Player> delanterosMock() {
        return List.of(
                new Player(1L,  "Lionel Messi",       "Inter Miami",     "Delantero"),
                new Player(2L,  "Cristiano Ronaldo",  "Al Nassr",        "Delantero"),
                new Player(3L,  "Kylian Mbappé",      "Real Madrid",     "Delantero"),
                new Player(4L,  "Erling Haaland",     "Manchester City", "Delantero"),
                new Player(6L,  "Neymar Jr.",          "Al Hilal",        "Delantero"),
                new Player(8L,  "Pelé",               "Santos FC",       "Delantero"),
                new Player(13L, "Robert Lewandowski", "FC Barcelona",    "Delantero"),
                new Player(14L, "Harry Kane",         "Bayern München",  "Delantero")
        );
    }

    public static List<Player> porterosMock() {
        return List.of(
                new Player(39L, "Gianluigi Buffon",  "Juventus",        "Portero"),
                new Player(40L, "Manuel Neuer",      "Bayern München",  "Portero"),
                new Player(41L, "Thibaut Courtois",  "Real Madrid",     "Portero"),
                new Player(42L, "Alisson Becker",    "Liverpool",       "Portero"),
                new Player(43L, "Iker Casillas",     "Real Madrid",     "Portero")
        );
    }

    public static List<Player> defensasMock() {
        return List.of(
                new Player(31L, "Virgil van Dijk",   "Liverpool",       "Defensa"),
                new Player(32L, "Sergio Ramos",      "Sevilla",         "Defensa"),
                new Player(36L, "Paolo Maldini",     "AC Milan",        "Defensa"),
                new Player(38L, "Carles Puyol",      "FC Barcelona",    "Defensa")
        );
    }
}

