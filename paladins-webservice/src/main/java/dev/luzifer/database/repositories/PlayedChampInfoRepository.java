package dev.luzifer.database.repositories;

import dev.luzifer.database.objects.PlayedChampInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayedChampInfoRepository extends JpaRepository<PlayedChampInfo, Integer> {}
