package dev.luzifer.data.gamestats;

import dev.luzifer.data.gamestats.champ.Category;
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
public class Champ {

    int id;
    String name;
    Category category;

}
