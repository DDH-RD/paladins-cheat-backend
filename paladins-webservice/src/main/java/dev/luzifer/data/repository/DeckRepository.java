package dev.luzifer.data.repository;

import dev.luzifer.data.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Integer> {}
