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

    /* Die gespielten Champs des jeweiligen Teams. Es sind immer 5. */
    long[] playedChampIds;
    /* Die gebannten Champs des jeweiligen Teams. Kann variieren. */
    long[] bannedChampIds;

}
