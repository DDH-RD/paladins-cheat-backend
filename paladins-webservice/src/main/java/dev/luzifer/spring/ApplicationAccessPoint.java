package dev.luzifer.spring;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationAccessPoint {

    public static final String API_KEY = "/{apiKey}"; // /{apiKey}
    public static final String GAME = API_KEY + "/game"; // /{apiKey}/game
    public static final String POST = "/post"; // /{apiKey}/game/post
    public static final String GET = "/get"; // /{apiKey}/game/get
    public static final String GET_COUNT = GET + "/count"; // /{apiKey}/game/get/count
}
