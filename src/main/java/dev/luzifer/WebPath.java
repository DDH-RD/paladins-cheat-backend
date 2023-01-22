package dev.luzifer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebPath {

    // fun stats wie zb. total played (Maeve : 117), total banned (Vatu : 10239), total scanned matches, total X, total Y

    public static final String PALADINS_PATH = "/paladins";
    public static final String GET_EVALUATED_PLAYED_SPECIFIC = PALADINS_PATH + "/get/evaluated/played";
    public static final String GET_EVALUATED_PLAYED_TOTAL = PALADINS_PATH + "/get/evaluated/played/total";
    public static final String GET_EVALUATED_BANNED_SPECIFIC = PALADINS_PATH + "/get/evaluated/banned";
    public static final String GET_EVALUATED_BANNED_TOTAL = PALADINS_PATH + "/get/evaluated/banned/total";
    public static final String POST_PLAYED_ENTRY = PALADINS_PATH + "/post/played";
    public static final String POST_BANNED_ENTRY = PALADINS_PATH + "/post/banned";

}
