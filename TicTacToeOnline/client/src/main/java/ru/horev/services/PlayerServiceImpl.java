package ru.horev.services;

import org.springframework.stereotype.Service;
import ru.horev.datastore.models.Player;
import ru.horev.datastore.repositories.PlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    public PlayerServiceImpl(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    public Long login(Player player){
        System.out.println(this.playerRepository.save(player));
        return 1L;
    }
}
