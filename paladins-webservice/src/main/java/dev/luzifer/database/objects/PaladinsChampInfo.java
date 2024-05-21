package dev.luzifer.database.objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * This class represents a Paladins champion info, so not a played game but the info of a champion.
 */
@Entity
@Data
public class PaladinsChampInfo {

  @Id private int id;

  private int categoryId;
}
