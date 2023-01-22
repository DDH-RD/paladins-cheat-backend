package dev.luzifer.data.gamestats.champ;

public enum Category {

    PALADINS_SUPPORT(0),
    PALADINS_FRONT_LINE(1),
    PALADINS_FLANKER(2),
    PALADINS_DAMAGE(3);

    private final int id;

    Category(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
