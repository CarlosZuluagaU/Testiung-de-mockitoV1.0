package org.mock.repository;

import org.mock.persistence.entity.Player;

import java.util.List;
import java.util.Optional;

public interface IPlayerRepository {

    List<Player> findAll();
    Player findById(Long id);
    void save(Player player);
    void deleteById(Long id);
}
