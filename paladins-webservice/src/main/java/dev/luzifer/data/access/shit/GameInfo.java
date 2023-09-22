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
public class GameInfo {

    int matchId;
    int ranked;
    int averageRank;
    int mapId;
    int[] bannedChamps;
    int team1Points;
    int team2Points;
    long duration;
    long timestamp;
    double season;
}
