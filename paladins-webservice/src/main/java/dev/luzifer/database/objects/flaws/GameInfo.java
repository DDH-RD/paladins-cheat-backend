package dev.luzifer.database.objects.flaws;

import dev.luzifer.database.objects.MapInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class GameInfo {

  @Id int matchId;

  int ranked;
  int averageRank;

  @OneToOne
  @JoinColumn(name = "mapId", referencedColumnName = "id")
  MapInfo map;

  int[] bannedChamps;
  int team1Points;
  int team2Points;
  long duration;
  long timestamp;
  double season;
}
