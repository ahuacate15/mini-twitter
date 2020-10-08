package com.carlos.minitwitter.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TweetRequest {

    @Expose
    @SerializedName("message")
    private String message;

    public TweetRequest() {}

    public TweetRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
