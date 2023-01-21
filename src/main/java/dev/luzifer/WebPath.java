package dev.luzifer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebPath {

    public static final String PALADINS_PATH = "/paladins";
    public static final String PALADINS_EVALUATED_FOR_MAP = PALADINS_PATH + "/get";
    public static final String PALADINS_EVALUATED_TOTAL = PALADINS_PATH + "/get/total";
    public static final String PALADINS_RECEIVE_ENTRY = PALADINS_PATH + "/post/champ_map";

}
