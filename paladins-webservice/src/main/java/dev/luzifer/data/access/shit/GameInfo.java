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
    int bannedChamp1;
    int bannedChamp2;
    int bannedChamp3;
    int bannedChamp4;
    int bannedChamp5;
    int bannedChamp6;
    int bannedChamp7;
    int bannedChamp8;
    int team1Points;
    int team2Points;
    long duration;
    long timestamp;
    double season;
}
