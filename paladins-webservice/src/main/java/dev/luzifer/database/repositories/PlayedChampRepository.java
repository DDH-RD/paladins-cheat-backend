package dev.luzifer.database.repositories;

import dev.luzifer.database.objects.PlayedChamp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayedChampRepository extends JpaRepository<PlayedChamp, Integer> {}
