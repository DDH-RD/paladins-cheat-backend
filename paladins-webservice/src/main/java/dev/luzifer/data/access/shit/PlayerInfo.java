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
public class PlayerInfo {

    int playerId;
    String playerName;
    String region;
    int platformId;
}
