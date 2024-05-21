package dev.luzifer.database.objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "item_draft")
@Data
public class ItemDraft {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  int id;

  @Column(name = "item_1")
  int item1;

  @Column(name = "item_2")
  int item2;

  @Column(name = "item_3")
  int item3;

  @Column(name = "item_4")
  int item4;

  @Column(name = "item_1_level")
  int item1Level;

  @Column(name = "item_2_level")
  int item2Level;

  @Column(name = "item_3_level")
  int item3Level;

  @Column(name = "item_4_level")
  int item4Level;

  @ManyToOne
  @JoinColumn(name = "played_champion_reference_id", referencedColumnName = "id")
  PlayedChamp champ;
}
