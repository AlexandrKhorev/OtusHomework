package ru.horev.datastore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.horev.datastore.models.Game;


import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
}
