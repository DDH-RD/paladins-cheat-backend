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
public class GameInfo {

    /* Der Name der Map auf der das Game stattgefunden hat. */
    String mapName;
    /* Die Informationen des Teams, welches gewonnen hat. */
    TeamInfo winnerTeam;
    /* Die Informationen des Teams, welches kacke war. */
    TeamInfo loserTeam;
    /* Wie lang das Spiel in Minuten ging */
    int minutesPlayed;
    /* Der Zeitpunkt wann das Match stattgefunden hat in Millisekunden. */
    long timestamp;
}
