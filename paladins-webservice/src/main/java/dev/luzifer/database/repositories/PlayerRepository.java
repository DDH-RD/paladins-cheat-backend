package dev.luzifer.database.repositories;

import dev.luzifer.database.objects.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {}
