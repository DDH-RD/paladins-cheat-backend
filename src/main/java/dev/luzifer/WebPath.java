package dev.luzifer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebPath {

    // fun stats wie zb. total played (Maeve : 117), total banned (Vatu : 10239), total scanned matches, total X, total Y

    public static final String MATCH = "/match";
    public static final String CHAMP = "/champ";
    public static final String POST_MULTIPLE_MATCH_INFO = "/post";
    public static final String POST_MATCH_INFO = "/post/{id}";
    public static final String GET_MATCH_IDS = "/get";
    public static final String GET_MATCH_INFO = "/get/{id}";
    public static final String GET_CHAMP_COUNTER = "/get/{id}/counter";
    public static final String GET_CHAMP_WINGMAN = "/get/{id}/wingman";
}
