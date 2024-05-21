package dev.luzifer.database.objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "player")
@Data
public class Player {

  @Id
  @Column(name = "player_id", nullable = false)
  int playerId;

  @Column(name = "player_name", nullable = false)
  String playerName;

  @ManyToOne
  @JoinColumn(name = "region_id", referencedColumnName = "id", nullable = false)
  Region region;

  @Column(name = "platform_id", nullable = false)
  int platformId;
}
