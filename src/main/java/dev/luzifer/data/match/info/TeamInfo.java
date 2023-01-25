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
public class TeamInfo {

    /* Die Punkte, zu wie viel das Team verloren/gewonnen hat */
    int points; // gehört eigentlich in GameInfo aber fuck it
    /* Die Champs die von diesem Team gespielt wurden. Max. 5 */
    ChampInfo[] playedChamps;
    /* Die Champs, die in der Vorauswahl von dem Team gebannt wurden */
    ChampInfo[] bannedChamps;

}
