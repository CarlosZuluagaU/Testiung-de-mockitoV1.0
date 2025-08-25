package org.mock;

import org.mock.persistence.entity.Player;

import java.util.List;

public class DataProvider {

    public static List<Player> playerListMock(){
        System.out.println("Lista Player");

        return  List.of(
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

    public static Player playerMock(){
        return  new Player(1L, "Lionel Messi", "Inter Miami", "Delantero");
    }
    public static Player newPlayerMock(){
        return new Player(12L, "David Cardona", "Nacional","Medio Campo");
    }


}
