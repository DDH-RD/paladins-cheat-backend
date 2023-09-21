package dev.luzifer.data.access.shit;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ChampInfo {

    int champId;
    int leagueTier;
    int leaguePoints;
    int champLevel;
    int won;
    int categoryId;
    int goldEarned;
    int talentId;
    int deckCard1;
    int deckCard2;
    int deckCard3;
    int deckCard4;
    int deckCard5;
    int deckCard1Level;
    int deckCard2Level;
    int deckCard3Level;
    int deckCard4Level;
    int deckCard5Level;
    int item1;
    int item2;
    int item3;
    int item4;
    int item1Level;
    int item2Level;
    int item3Level;
    int item4Level;
    int killingSpree;
    int kills;
    int deaths;
    int assists;
    int damageDone;
    int damageTaken;
    int damageShielded;
    int heal;
    int selfHeal;
    int matchId;
    int playerId;
}
