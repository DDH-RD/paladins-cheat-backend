package dev.luzifer.data.repository;

import dev.luzifer.data.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Integer> {}
