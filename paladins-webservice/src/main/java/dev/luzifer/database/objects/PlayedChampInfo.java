package dev.luzifer.database.objects;

import dev.luzifer.database.objects.flaws.GameInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class PlayedChampInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  int champId;
  int leagueTier;
  int leaguePoints;
  int champLevel;
  int won;
  int goldEarned;
  int killingSpree;
  int kills;
  int deaths;
  int assists;
  int damageDone;
  int damageTaken;
  int damageShielded;
  int heal;
  int selfHeal;

  @ManyToOne
  @JoinColumn(name = "matchId", referencedColumnName = "matchId")
  GameInfo match;

  @OneToOne
  @JoinColumn(name = "playerId", referencedColumnName = "playerId")
  PlayerInfo player;
}
