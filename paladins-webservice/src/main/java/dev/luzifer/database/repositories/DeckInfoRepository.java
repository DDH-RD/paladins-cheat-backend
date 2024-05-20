package dev.luzifer.database.repositories;

import dev.luzifer.database.objects.DeckInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckInfoRepository extends JpaRepository<DeckInfo, Integer> {}
