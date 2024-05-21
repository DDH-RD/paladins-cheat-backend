package dev.luzifer.database.objects;

import dev.luzifer.database.objects.flaws.Match;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "played_champion")
@Data
public class PlayedChamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  int id;

  @ManyToOne
  @JoinColumn(name = "champion_id", referencedColumnName = "champion_id", nullable = false)
  PaladinsChamp champInfo;

  @Column(name = "league_tier", nullable = false)
  int leagueTier;

  @Column(name = "league_points", nullable = false)
  int leaguePoints;

  @Column(name = "champ_level", nullable = false)
  int champLevel;

  @Column(name = "won", nullable = false)
  int won;

  @Column(name = "gold_earned", nullable = false)
  int goldEarned;

  @Column(name = "killing_spree", nullable = false)
  int killingSpree;

  @Column(name = "kills", nullable = false)
  int kills;

  @Column(name = "deaths", nullable = false)
  int deaths;

  @Column(name = "assists", nullable = false)
  int assists;

  @Column(name = "damage_done", nullable = false)
  int damageDone;

  @Column(name = "damage_taken", nullable = false)
  int damageTaken;

  @Column(name = "damage_shielded", nullable = false)
  int damageShielded;

  @Column(name = "heal", nullable = false)
  int heal;

  @Column(name = "self_heal", nullable = false)
  int selfHeal;

  @ManyToOne
  @JoinColumn(name = "match_id", referencedColumnName = "match_id", nullable = false)
  Match match;

  @OneToOne
  @JoinColumn(name = "player_id", referencedColumnName = "player_id", nullable = false)
  Player player;
}
