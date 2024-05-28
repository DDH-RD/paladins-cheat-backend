package dev.luzifer.data.entity;

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
@Table(name = "banned_champ")
@Data
public class BannedChamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  int id;

  @Column(name = "team", nullable = false)
  int team;

  @ManyToOne
  @JoinColumn(name = "champion_id", referencedColumnName = "champion_id", nullable = false)
  PaladinsChamp champ;

  @ManyToOne
  @JoinColumn(name = "match_id", referencedColumnName = "match_id", nullable = false)
  Match match;
}
