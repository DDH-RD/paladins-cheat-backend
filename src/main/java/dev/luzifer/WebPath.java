package dev.luzifer;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebPath {

    public static final String MATCH = "/match";
    public static final String DELETE_MATCH = "/delete/{id}";
    public static final String POST_MULTIPLE_MATCH_INFO = "/post";
    public static final String POST_MATCH_INFO = "/post/{id}";
    public static final String GET_MATCH_IDS = "/get";
    public static final String GET_MATCH_INFO = "/get/{id}";
}
