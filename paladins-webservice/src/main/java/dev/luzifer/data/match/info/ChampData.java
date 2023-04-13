package dev.luzifer.data.match.info;

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
public class ChampData {

    /* Match Stats*/
    int matchId;
    /* Der Name der Map auf der das Game stattgefunden hat. */
    String mapName;
    /* 0 = false, 1 = true*/
    int ranked;
    /* Der durchschnittl. Rank in dem Match */
    int averageRank;
    /* IDs gebannter Champs */
    int bannedChamp1;
    int bannedChamp2;
    int bannedChamp3;
    int bannedChamp4;
    int bannedChamp5;
    int bannedChamp6;
    /* Punktestand der Teams */
    int team1Points;
    int team2Points;
    /* Wie lang das Spiel in Millisekunden ging */
    long duration;
    /* Der Zeitpunkt wann das Match stattgefunden hat in Millisekunden. */
    long timestamp;
    /* Die Season in der das Match stattegefunden hat.
     *  Example: Season 5, Split 1 = 5.1
     */
    double season;
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
    long damageDone;
    long damageTaken;
    long damageShielded; /* "migiated" */
    long heal;
    long selfHeal;
}
