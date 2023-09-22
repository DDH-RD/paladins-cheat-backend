package dev.luzifer.data.dto;

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
public class ChampDto {

    /* Die ID des Champs */
    int champId;
    /* player specific data */
    int playerId;
    String playerName;
    String region;
    int platformId;
    int leagueTier;
    int leaguePoints;
    int champLevel;
    /* 0 = false, 1 = true */
    int won;
    /* Die ID der Champ-Category
     *  0: TANK
     *  1: HEAL
     *  2: FLANK
     *  3: DAMAGE
     */
    int categoryId;
    /* credits */
    int goldEarned;
    /* Das Talent, dass der jeweilige Champ gespielt hat. */
    int talentId;
    /* Die IDs der einzelnen Karten. */
    int deckCard1;
    int deckCard2;
    int deckCard3;
    int deckCard4;
    int deckCard5;
    /* Die Level der einzelnen Karten. */
    int deckCard1Level;
    int deckCard2Level;
    int deckCard3Level;
    int deckCard4Level;
    int deckCard5Level;
    /* Die gekauften Items.*/
    int item1;
    int item2;
    int item3;
    int item4;
    /* Level der Items */
    int item1Level;
    int item2Level;
    int item3Level;
    int item4Level;
    /* Game stats. */
    int killingSpree;
    int kills;
    int deaths;
    int assists;
    int damageDone;
    int damageTaken;
    int damageShielded; /* "migiated" */
    int heal;
    int selfHeal;
}
