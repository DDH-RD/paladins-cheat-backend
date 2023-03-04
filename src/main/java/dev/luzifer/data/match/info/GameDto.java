package dev.luzifer.data.match.info;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    int bannedChamp1;
    int bannedChamp2;
    int bannedChamp3;
    int bannedChamp4;
    int bannedChamp5;
    int bannedChamp6;
    /* Punktestand der Teams */
    int team1Points;
    int team2Points;
    /* Champs */
    ChampDto[] champs;
    /* Wie lang das Spiel in Millisekunden ging */
    long duration;
    /* Der Zeitpunkt wann das Match stattgefunden hat in Millisekunden. */
    long timestamp;
}
