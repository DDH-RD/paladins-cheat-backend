package dev.luzifer.database.repositories;

import dev.luzifer.database.objects.PlayerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerInfoRepository extends JpaRepository<PlayerInfo, Integer> {}
