package dev.luzifer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebPath {

    public static final String GAME = "/game";
    public static final String POST = "/post";
    public static final String GET = "/get";
    public static final String GET_COUNT = GET + "/count";
    public static final String GET_COUNT_ON_MAP = GET + "/countMap/{mapName}";
    public static final String GET_COUNT_ON_CHAMP = GET + "/countChamp/{champId}";
    public static final String GET_COUNT_ON_MAP_AND_CHAMP = GET + "/countMapAndChamp/{mapName}/{champId}";
    public static final String GET_BEST_CHAMP_FOR_MAP = GET + "/bestChampForMap/{mapName}";
    public static final String GET_BEST_CHAMP_OF_CATEGORY_FOR_MAP = GET + "/bestChampOfCategoryForMap/{mapName}/{champCategory}";
    public static final String GET_BEST_COUNTER_CHAMP_FOR_CHAMP = GET + "/bestCounterChampForChamp/{champId}";
    public static final String GET_BEST_COUNTER_CHAMP_OF_CATEGORY_FOR_CHAMP = GET + "/bestCounterChampOfCategoryForChamp/{champId}/{champCategory}";
    public static final String GET_BEST_BAN_FOR_MAP = GET + "/bestBanForMap/{mapName}";
    public static final String GET_BEST_TALENT_FOR_CHAMP = GET + "/bestTalentForChamp/{champId}";
    public static final String GET_BEST_DECK_FOR_CHAMP = GET + "/bestDeckForChamp/{champId}";
    public static final String GET_BEST_CHAMP = GET + "/bestChamp";
    public static final String GET_BEST_CHAMP_OF_CATEGORY = GET + "/bestChampOfCategory/{champCategory}";
}
