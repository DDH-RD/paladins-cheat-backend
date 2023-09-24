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
public class GameDto {

    /* ID vom match */
    int id;
    /* Der Name der Map auf der das Game stattgefunden hat. */
    String mapName;
    /* 0 = false, 1 = true*/
    int ranked;
    /* Der durchschnittl. Rank in dem Match */
    int averageRank;
    /* IDs gebannter Champs */
    int[] bannedChamps;
    /* Punktestand der Teams */
    int team1Points;
    int team2Points;
    /* Champs */
    ChampDto[] champs;
    /* Wie lang das Spiel in Millisekunden ging */
    long duration;
    /* Der Zeitpunkt wann das Match stattgefunden hat in Millisekunden. */
    long timestamp;
    /* Die Season in der das Match stattegefunden hat.
     *  Example: Season 5, Split 1 = 5.1
     * */
    double season;
}