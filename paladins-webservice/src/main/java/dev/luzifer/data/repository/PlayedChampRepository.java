package dev.luzifer.data.repository;

import dev.luzifer.data.entity.PlayedChamp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayedChampRepository extends JpaRepository<PlayedChamp, Integer> {}
