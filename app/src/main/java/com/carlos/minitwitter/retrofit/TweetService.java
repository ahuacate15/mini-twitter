package com.carlos.minitwitter.retrofit;

import com.carlos.minitwitter.retrofit.response.TweetResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TweetService {

    @GET("tweet/all")
    public Call<List<TweetResponse>> getAllTweets();

}
