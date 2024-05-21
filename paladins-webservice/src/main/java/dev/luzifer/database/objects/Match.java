package dev.luzifer.database.objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "match")
@Data
public class Match {

  @Id
  @Column(name = "match_id", nullable = false)
  int matchId;

  @Column(name = "ranked", nullable = false)
  int ranked;

  @Column(name = "average_rank", nullable = false)
  int averageRank;

  @OneToOne
  @JoinColumn(name = "map_id", referencedColumnName = "id")
  Map map;

  @OneToMany(mappedBy = "match")
  List<BannedChamp> bannedChamps;

  @Column(name = "team1_points", nullable = false)
  int team1Points;

  @Column(name = "team2_points", nullable = false)
  int team2Points;

  @Column(name = "duration", nullable = false)
  long duration;

  @Column(name = "timestamp", nullable = false)
  long timestamp;

  @Column(name = "season", nullable = false)
  double season;
}
