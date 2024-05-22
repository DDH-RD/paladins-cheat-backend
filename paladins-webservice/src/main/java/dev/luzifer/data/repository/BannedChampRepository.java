package dev.luzifer.data.repository;

import dev.luzifer.data.entity.BannedChamp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannedChampRepository extends JpaRepository<BannedChamp, Integer> {}
