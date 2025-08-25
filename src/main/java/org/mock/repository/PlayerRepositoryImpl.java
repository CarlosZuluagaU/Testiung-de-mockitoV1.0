package org.mock.repository;

import org.mock.persistence.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerRepositoryImpl implements  IPlayerRepository{

    private List<Player> playerDatabase = new ArrayList<>(List.of(
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
    ));

    @Override
    public List<Player> findAll(){
        System.out.println("-> FindAll real~!");
        return this.playerDatabase;
    }

    @Override
    public Player findById(Long id) {
        System.out.println("Medotodo findById real!!");
        return this.playerDatabase.stream()
                .filter(player -> player.getId()== id)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public void save(Player player) {
        System.out.println("Metodo save real!");
        this.playerDatabase.add(player);

    }

    @Override
    public void deleteById(Long id) {
        this.playerDatabase = this.playerDatabase.stream()
                .filter(player -> player.getId()!= id)
                .toList();

    }
}
