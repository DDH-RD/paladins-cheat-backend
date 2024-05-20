package dev.luzifer.database.repositories;

import dev.luzifer.database.objects.ChampInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChampInfoRepository extends JpaRepository<ChampInfo, Integer> {}
