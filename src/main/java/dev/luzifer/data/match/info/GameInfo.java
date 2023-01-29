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
@EqualsAndHashCode(exclude = {"mapName", "averageRank", "bannedChamps", "winnerTeam", "loserTeam", "duration", "timestamp"})
public class GameInfo {

    /* Match-ID */
    int id;
    /* Der Name der Map auf der das Game stattgefunden hat. */
    String mapName;
    /* Der durchschnittl. Rank in dem Match */
    int averageRank;
    /* IDs gebannter Champs */
    long[] bannedChamps;
    /* Die Informationen des Teams, welches gewonnen hat. */
    TeamInfo winnerTeam;
    /* Die Informationen des Teams, welches kacke war. */
    TeamInfo loserTeam;
    /* Wie lang das Spiel in Millisekunden ging */
    long duration;
    /* Der Zeitpunkt wann das Match stattgefunden hat in Millisekunden. */
    long timestamp;
}
