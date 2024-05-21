package dev.luzifer.database.objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class DeckInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  int talentId;
  int deckCard1;
  int deckCard2;
  int deckCard3;
  int deckCard4;
  int deckCard5;
  int deckCard1Level;
  int deckCard2Level;
  int deckCard3Level;
  int deckCard4Level;
  int deckCard5Level;

  @OneToOne
  @JoinColumn(name = "champId", referencedColumnName = "id")
  PlayedChampInfo champ;
}
