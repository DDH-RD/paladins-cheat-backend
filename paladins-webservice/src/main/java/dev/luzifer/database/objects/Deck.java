package dev.luzifer.database.objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "deck")
@Data
public class Deck {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  int id;

  @Column(name = "talent_id", nullable = false)
  int talentId;

  @Column(name = "deck_card_1", nullable = false)
  int deckCard1;

  @Column(name = "deck_card_2", nullable = false)
  int deckCard2;

  @Column(name = "deck_card_3", nullable = false)
  int deckCard3;

  @Column(name = "deck_card_4", nullable = false)
  int deckCard4;

  @Column(name = "deck_card_5", nullable = false)
  int deckCard5;

  @Column(name = "deck_card_1_level", nullable = false)
  int deckCard1Level;

  @Column(name = "deck_card_2_level", nullable = false)
  int deckCard2Level;

  @Column(name = "deck_card_3_level", nullable = false)
  int deckCard3Level;

  @Column(name = "deck_card_4_level", nullable = false)
  int deckCard4Level;

  @Column(name = "deck_card_5_level", nullable = false)
  int deckCard5Level;

  @OneToOne
  @JoinColumn(name = "played_champion_reference_id", referencedColumnName = "id")
  PlayedChamp champ;
}
