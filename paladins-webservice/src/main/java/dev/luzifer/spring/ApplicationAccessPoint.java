package dev.luzifer.spring;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationAccessPoint {

    public static final String API_KEY = "/{apiKey}"; // api key
    public static final String GAME = API_KEY + "/game"; // main path
    public static final String POST = "/post"; // posting GameDto[]
    public static final String GET = "/get"; // get path
    public static final String GET_COUNT = GET + "/count"; // int
}
