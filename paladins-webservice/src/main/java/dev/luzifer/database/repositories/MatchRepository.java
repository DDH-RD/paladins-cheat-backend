package dev.luzifer.database.repositories;

import dev.luzifer.database.objects.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Integer> {}
