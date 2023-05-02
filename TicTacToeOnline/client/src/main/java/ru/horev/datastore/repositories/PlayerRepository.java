package ru.horev.datastore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.horev.datastore.models.Player;


@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
