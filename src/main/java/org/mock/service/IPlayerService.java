package org.mock.service;

import org.mock.persistence.entity.Player;

import java.util.List;

public interface IPlayerService {
    List<Player> findAll();
    Player findById(Long id);
    void save(Player player);
    void update(Player player);
    void deleteById(Long id);
    List<Player> findByTeam(String team);
    List<Player> findByPosition(String position);
    boolean existsById(Long id);
}

