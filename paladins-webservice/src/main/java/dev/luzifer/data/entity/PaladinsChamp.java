package dev.luzifer.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * This class represents a Paladins champion info, so not a played game but the info of a champion.
 */
@Entity
@Table(name = "paladins_champion")
@Data
public class PaladinsChamp {

  @Id
  @Column(name = "champion_id", nullable = false)
  int id;

  @Column(name = "category_id", nullable = false)
  int categoryId;
}
