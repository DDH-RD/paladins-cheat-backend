package dev.luzifer.database.objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class PlayerInfo {

  @Id int playerId;

  String playerName;

  @ManyToOne
  @JoinColumn(name = "regionId", referencedColumnName = "id")
  RegionInfo region;

  int platformId;
}
