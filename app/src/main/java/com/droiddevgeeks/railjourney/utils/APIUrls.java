package com.droiddevgeeks.railjourney.utils;

/**
 * Created by Vampire on 09-10-2016.
 */
public class APIUrls
{

    private static final String API_KEY = "";
    public static final String BASE_PREFIX_URL = "http://api.railwayapi.com/";
    public static final String BASE_SUFFIX_URL = "/apikey/"+API_KEY;

    public static final String PNR_URL = "pnr_status/pnr/";
    public static final String LIVE_STATUS = "live/train/";
    public static final String TRAIN_ROUTE = "route/train/";
    public static final String TRAIN_BTW_STN = "between/";
    public static final String FARE_ENQUIRY = "fare/";
    public static final String SEAT_AVAIL = "check_seat/";
    public static final String CANCELLED_TRAIN = "pnr_status/pnr/";
    public static final String TRAIN_AT_STATION = "arrivals/";
    public static final String AUTO_SUGGEST_LIST = "suggest_train/trains/";
    public static final String AUTO_SUGGEST_STATION = "suggest_station/name/";


}
