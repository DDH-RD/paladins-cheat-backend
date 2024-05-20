package dev.luzifer.database.dto;

import lombok.Data;

@Data
public class GameDto {

  private int id;
  private String mapName;
  private int ranked;
  private int averageRank;
  private int[] bannedChamps;
  private int team1Points;
  private int team2Points;
  private ChampDto[] champs;
  private long duration;
  private long timestamp;
  private double season;
}
