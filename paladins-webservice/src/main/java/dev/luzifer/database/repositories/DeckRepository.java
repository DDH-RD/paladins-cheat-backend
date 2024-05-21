package dev.luzifer.database.repositories;

import dev.luzifer.database.objects.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, Integer> {}
