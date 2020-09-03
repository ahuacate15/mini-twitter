package com.carlos.minitwitter.common;

import com.carlos.minitwitter.retrofit.response.ErrorResponse;
import com.google.gson.Gson;

public class ConvertToGson {

    public static ErrorResponse toError(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ErrorResponse.class);
    }
}
