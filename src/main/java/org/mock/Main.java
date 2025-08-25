package org.mock;

import org.mock.persistence.entity.Player;
import org.mock.repository.IPlayerRepository;
import org.mock.repository.PlayerRepositoryImpl;
import org.mock.service.PlayerServiceImpl;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        PlayerRepositoryImpl playerRepository = new PlayerRepositoryImpl();
        PlayerServiceImpl playerService = new PlayerServiceImpl(playerRepository);
        System.out.println(playerService.findAll());

        System.out.println(playerService.findById(2L));


        Player player = new Player(7L, "Luis Diaz","Liverpool","dealntero");
        System.out.println(playerService.findAll());

    }
}