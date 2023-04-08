package dev.luzifer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebPath {

    public static final String GAME = "/game"; // main path
    public static final String POST = "/post"; // posting GameDto[]
    public static final String GET = "/get"; // get path
    public static final String GET_COUNT = GET + "/count"; // int
    public static final String GET_COUNT_ON_MAP = GET + "/countMap/{mapName}"; // int
    public static final String GET_COUNT_ON_CHAMP = GET + "/countChamp/{champId}"; // int
    public static final String GET_COUNT_ON_MAP_AND_CHAMP = GET + "/countMapAndChamp/{mapName}/{champId}"; // int
    public static final String GET_BEST_CHAMP_FOR_MAP = GET + "/bestChampForMap/{mapName}"; // Map<Integer, Integer
    public static final String GET_BEST_CHAMP_OF_CATEGORY_FOR_MAP = GET + "/bestChampOfCategoryForMap/{mapName}/{champCategory}"; // Map<Integer, Integer
    public static final String GET_BEST_COUNTER_CHAMP_FOR_CHAMP = GET + "/bestCounterChampForChamp/{champId}"; // Map<Integer, Integer
    public static final String GET_BEST_COUNTER_CHAMP_OF_CATEGORY_FOR_CHAMP = GET + "/bestCounterChampOfCategoryForChamp/{champId}/{champCategory}"; // Map<Integer, Integer
    public static final String GET_BEST_BAN_FOR_MAP = GET + "/bestBanForMap/{mapName}"; // Map<Integer, Integer
    public static final String GET_BEST_TALENT_FOR_CHAMP = GET + "/bestTalentForChamp/{champId}"; // Map<Integer, Integer
    public static final String GET_BEST_DECK_FOR_CHAMP = GET + "/bestDeckForChamp/{champId}"; // Map<Integer, Integer
    public static final String GET_BEST_CHAMP = GET + "/bestChamp"; // Map<Integer, Integer
    public static final String GET_BEST_CHAMP_OF_CATEGORY = GET + "/bestChampOfCategory/{champCategory}"; // Map<Integer, Integer
}
