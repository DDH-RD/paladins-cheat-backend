package dev.luzifer.paladins;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Getter
@Setter
@Value
@ToString
public class PaladinsChampion {

    int id;
    String name;
    String role;
    int role_id;
    String title;
    int health;
    int Speed;
    String artwork;
    List<Ability> abilities;
    List<Talent> talents;
    List<Card> cards;

    @Setter
    @Getter
    @Value
    public static class Ability {

        String name;
        String description;
        int id;
        String artwork;
        int cooldown;
    }

    @Setter
    @Getter
    @Value
    public static class Talent {

        String name;
        String description;
        String artwork;
        int card_id1;
        int card_id2;
    }
    
    @Setter
    @Getter
    @Value
    public static class Card {
        
        String name;
        String description;
        String artwork;
        int card_id1;
        int card_id2;
    }
}
