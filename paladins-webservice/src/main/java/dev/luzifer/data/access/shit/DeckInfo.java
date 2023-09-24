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
public class DeckInfo {
    
    int talentId;
    int deckCard1;
    int deckCard2;
    int deckCard3;
    int deckCard4;
    int deckCard5;
    int deckCard1Level;
    int deckCard2Level;
    int deckCard3Level;
    int deckCard4Level;
    int deckCard5Level;
    int matchId;
    int champId;
}
