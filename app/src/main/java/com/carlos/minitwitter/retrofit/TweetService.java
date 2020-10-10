package com.carlos.minitwitter.retrofit;

import com.carlos.minitwitter.retrofit.request.TweetRequest;
import com.carlos.minitwitter.retrofit.response.GenericResponse;
import com.carlos.minitwitter.retrofit.response.TweetResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TweetService {

    @POST("tweet")
    public Call<TweetResponse> create(@Body TweetRequest request);

    @GET("tweet/all")
    public Call<List<TweetResponse>> getAllTweets();

    @PUT("tweet/like/{id}")
    public Call<TweetResponse> like(@Path("id") int id);

    @DELETE("tweet/like/{id}")
    public Call<TweetResponse> unlike(@Path("id") int id);
}
