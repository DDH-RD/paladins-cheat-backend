package dev.luzifer.data.gamestats;

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

    String mapName;
    long[] playedChampIds;
    long[] bannedChampIds;

}
