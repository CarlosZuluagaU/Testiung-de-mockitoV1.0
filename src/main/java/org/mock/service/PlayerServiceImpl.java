package org.mock.service;

import org.mock.persistence.entity.Player;
import org.mock.repository.IPlayerRepository;

import java.util.List;

public class PlayerServiceImpl implements IPlayerService {

    private IPlayerRepository playerRepository;

    public PlayerServiceImpl(IPlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> findAll() {
        return this.playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
        return this.playerRepository.findById(id);
    }

    @Override
    public void save(Player player) {
        this.playerRepository.save(player);
    }

    @Override
    public void update(Player player) {
        this.playerRepository.update(player);
    }

    @Override
    public void deleteById(Long id) {
        this.playerRepository.deleteById(id);
    }

    @Override
    public List<Player> findByTeam(String team) {
        return this.playerRepository.findByTeam(team);
    }

    @Override
    public List<Player> findByPosition(String position) {
        return this.playerRepository.findByPosition(position);
    }

    @Override
    public boolean existsById(Long id) {
        return this.playerRepository.existsById(id);
    }
}
