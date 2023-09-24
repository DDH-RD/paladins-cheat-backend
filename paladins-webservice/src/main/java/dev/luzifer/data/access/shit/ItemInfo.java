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
public class ItemInfo {
    
    int item1;
    int item2;
    int item3;
    int item4;
    int item1Level;
    int item2Level;
    int item3Level;
    int item4Level;
    int matchId;
    int champId;
}
