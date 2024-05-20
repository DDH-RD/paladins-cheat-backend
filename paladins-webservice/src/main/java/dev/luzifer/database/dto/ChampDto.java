package dev.luzifer.database.dto;

import lombok.Data;

@Data
public class ChampDto {

  private int champId;
  private int playerId;
  private String playerName;
  private String region;
  private int platformId;
  private int leagueTier;
  private int leaguePoints;
  private int champLevel;
  private int won;
  private int categoryId;
  private int goldEarned;
  private int talentId;
  private int deckCard1;
  private int deckCard2;
  private int deckCard3;
  private int deckCard4;
  private int deckCard5;
  private int deckCard1Level;
  private int deckCard2Level;
  private int deckCard3Level;
  private int deckCard4Level;
  private int deckCard5Level;
  private int item1;
  private int item2;
  private int item3;
  private int item4;
  private int item1Level;
  private int item2Level;
  private int item3Level;
  private int item4Level;
  private int killingSpree;
  private int kills;
  private int deaths;
  private int assists;
  private int damageDone;
  private int damageTaken;
  private int damageShielded;
  private int heal;
  private int selfHeal;
}
