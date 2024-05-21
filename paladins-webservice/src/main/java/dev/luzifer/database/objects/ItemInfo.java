package dev.luzifer.database.objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ItemInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  int item1;
  int item2;
  int item3;
  int item4;
  int item1Level;
  int item2Level;
  int item3Level;
  int item4Level;

  @ManyToOne
  @JoinColumn(name = "champId", referencedColumnName = "id")
  PlayedChampInfo champ;
}
