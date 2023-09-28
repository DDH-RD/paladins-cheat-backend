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

    // game
    public static final String GAME = API_KEY + "/game"; // /{apiKey}/game
    public static final String POST = "/post"; // /{apiKey}/game/post
    public static final String GET = "/get"; // /{apiKey}/game/get

    // latest match id
    public static final String GET_LATEST_MATCH_ID = GET + "/latestmatchid"; // /{apiKey}/game/get/latestmatchid

    // evaluation
    public static final String GET_EVALUATION = GET + "/evaluation"; // /{apiKey}/game/get/evaluation
    public static final String GET_EVALUATION_BEST_BANS = GET_EVALUATION + "/bestbans"; // /{apiKey}/game/get/evaluation/bestbans
    public static final String GET_EVALUATION_BEST_BANS_FOR_MAP = GET_EVALUATION + "/bestbans/{map}"; // /{apiKey}/game/get/evaluation/bestbans/{map}

    // count
    public static final String GET_COUNT = GET + "/count"; // /{apiKey}/game/get/count
    public static final String GET_COUNT_CHAMPS = GET_COUNT + "/champs"; // /{apiKey}/game/get/count/champs
    public static final String GET_COUNT_ITEM_CRAFTS = GET_COUNT + "/itemcrafts"; // /{apiKey}/game/get/count/itemcrafts
    public static final String GET_COUNT_DECKS = GET_COUNT + "/decks"; // /{apiKey}/game/get/count/decks
    public static final String GET_COUNT_PLAYERS = GET_COUNT + "/players"; // /{apiKey}/game/get/count/players
    public static final String GET_COUNT_BANNED_CHAMPS = GET_COUNT + "/bannedchamps"; // /{apiKey}/game/get/count/bannedchamps
    public static final String GET_COUNT_REGIONS = GET_COUNT + "/regions"; // /{apiKey}/game/get/count/regions
    public static final String GET_COUNT_MAPS = GET_COUNT + "/maps"; // /{apiKey}/game/get/count/maps


}
