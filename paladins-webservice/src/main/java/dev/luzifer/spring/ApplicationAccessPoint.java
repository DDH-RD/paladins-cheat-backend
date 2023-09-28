package dev.luzifer.spring;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationAccessPoint {

    /*
     * Note für später: Die Webpfade werden direkt nach game einen season parameter bekommen.
     * Wenn dieser 0 ist, werden alle Seasons in betracht gezogen.
     * Wenn dieser 1 ist, werden nur die Daten der aktuellen Season in betracht gezogen.
     * Andernfalls wird die Season mit der entsprechenden ID in betracht gezogen.
     */

    public static final String API_KEY = "/{apiKey}"; // /{apiKey}

    // debug
    public static final String DEBUG = API_KEY + "/debug"; // /{apiKey}/debug
    public static final String LATEST_LOG_DOWNLOAD = "/latestlog"; // /{apiKey}/debug/latestlog

    // game - main paths
    public static final String GAME = API_KEY + "/game"; // /{apiKey}/game
    public static final String POST = "/post"; // /{apiKey}/game/post
    public static final String GET = "/get"; // /{apiKey}/game/get

    // latest match id
    public static final String GET_LATEST_MATCH_ID = GET + "/latestmatchid"; // /{apiKey}/game/get/latestmatchid

    // evaluation - main path
    public static final String GET_EVALUATION = GAME + GET + "/evaluation"; // /{apiKey}/game/get/evaluation

    // evaluations
    public static final String GET_EVALUATION_BEST_BANS = "/bestbans"; // /{apiKey}/game/get/evaluation/bestbans
    public static final String GET_EVALUATION_BEST_BANS_FOR_MAP = "/bestbans/{map}"; // /{apiKey}/game/get/evaluation/bestbans/{map}

    // count - main path
    public static final String GET_COUNT = GAME + GET + "/count"; // /{apiKey}/game/get/count

    // counts
    public static final String GET_COUNT_CHAMPS = "/champs"; // /{apiKey}/game/get/count/champs
    public static final String GET_COUNT_ITEM_CRAFTS = "/itemcrafts"; // /{apiKey}/game/get/count/itemcrafts
    public static final String GET_COUNT_DECKS = "/decks"; // /{apiKey}/game/get/count/decks
    public static final String GET_COUNT_PLAYERS = "/players"; // /{apiKey}/game/get/count/players
    public static final String GET_COUNT_BANNED_CHAMPS = "/bannedchamps"; // /{apiKey}/game/get/count/bannedchamps
    public static final String GET_COUNT_REGIONS = "/regions"; // /{apiKey}/game/get/count/regions
    public static final String GET_COUNT_MAPS = "/maps"; // /{apiKey}/game/get/count/maps


}
