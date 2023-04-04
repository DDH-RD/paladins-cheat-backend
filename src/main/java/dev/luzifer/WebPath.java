package dev.luzifer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebPath {

    public static final String GAME = "/game";
    public static final String POST = "/post";
    public static final String GET = "/get";
    public static final String GET_COUNT = GET + "/count";
    public static final String GET_BEST_CHAMP_FOR_MAP = GET + "/bestChampForMap/{mapName}";
    public static final String GET_BEST_CHAMP_OF_CATEGORY_FOR_MAP = GET + "/bestChampOfCategoryForMap/{mapName}/{champCategory}";
    public static final String GET_BEST_COUNTER_CHAMP_FOR_CHAMP = GET + "/bestCounterChampForChamp/{champId}";
    public static final String GET_BEST_COUNTER_CHAMP_OF_CATEGORY_FOR_CHAMP = GET + "/bestCounterChampOfCategoryForChamp/{champId}/{champCategory}";
}
