package dev.luzifer.data.repository;

import dev.luzifer.data.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {}
