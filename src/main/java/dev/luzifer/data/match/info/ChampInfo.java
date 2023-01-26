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
public class ChampInfo {

    /* Die ID des Champs */
    long id;
    /* Die ID der Champ-Category
    *  0: TANK
    *  1: HEAL
    *  2: FLANK
    *  3: DAMAGE
    */
    long categoryId;
    /* Die ID der einzelnen Karten. Reihenfolge egal. */
    long[] cardIds;

}
