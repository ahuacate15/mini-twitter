package com.carlos.minitwitter.retrofit;

import com.carlos.minitwitter.retrofit.request.TweetRequest;
import com.carlos.minitwitter.retrofit.response.GenericResponse;
import com.carlos.minitwitter.retrofit.response.TweetResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TweetService {

    @POST("tweet")
    public Call<TweetResponse> create(@Body TweetRequest request);

    @GET("tweet/all")
    public Call<List<TweetResponse>> getAllTweets();

}
