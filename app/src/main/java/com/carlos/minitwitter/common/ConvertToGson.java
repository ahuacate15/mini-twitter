package com.carlos.minitwitter.common;

import com.carlos.minitwitter.retrofit.response.ErrorResponse;
import com.carlos.minitwitter.retrofit.response.GenericResponse;
import com.google.gson.Gson;

public class ConvertToGson {

    public static ErrorResponse toError(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ErrorResponse.class);
    }

    public static GenericResponse toGenericResponse(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, GenericResponse.class);
    }
}
