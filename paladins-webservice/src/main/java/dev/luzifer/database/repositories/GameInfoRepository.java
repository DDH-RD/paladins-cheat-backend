package dev.luzifer.database.repositories;

import dev.luzifer.database.objects.flaws.GameInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameInfoRepository extends JpaRepository<GameInfo, Integer> {}
