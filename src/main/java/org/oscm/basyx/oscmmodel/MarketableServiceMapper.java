package org.oscm.basyx.oscmmodel;

import com.google.gson.Gson;

public class MarketableServiceMapper {



    public static String exportMServiceToJson(MarketableServiceInfo serviceInfo) {
        Gson gson = new Gson();
        return gson.toJson(serviceInfo);
    }

    public static MarketableServiceInfo readMServiceFromJson(String json) {
        Gson gson = new Gson();
        // Convert JSON File to Java Object
        MarketableServiceInfo serviceInfo = gson.fromJson(json, MarketableServiceInfo.class);
        return serviceInfo;
    }
}
