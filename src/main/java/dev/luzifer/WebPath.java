package dev.luzifer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebPath {

    // fun stats wie zb. total played (Maeve : 117), total banned (Vatu : 10239), total scanned matches, total X, total Y

    public static final String PALADINS_PATH = "/paladins";
    public static final String PALADINS_POST_MATCH_INFO = PALADINS_PATH + "/post/{id}";
    public static final String PALADINS_GET_MATCH_IDS = PALADINS_PATH + "/get";
    public static final String PALADINS_GET_MATCH_INFO = PALADINS_PATH + "/get/{id}";
}
