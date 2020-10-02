package com.carlos.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.carlos.minitwitter.common.ConvertToGson;
import com.carlos.minitwitter.common.MyApplication;
import com.carlos.minitwitter.retrofit.TweetClient;
import com.carlos.minitwitter.retrofit.TweetService;
import com.carlos.minitwitter.retrofit.response.ErrorResponse;
import com.carlos.minitwitter.retrofit.response.TweetResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private TweetService tweetService;
    private TweetClient tweetClient;

    public TweetRepository() {
        this.tweetClient = TweetClient.getInstance();
        this.tweetService = this.tweetClient.getTweetService();
        getAllTweets();
    }

    public LiveData<List<TweetResponse>> getAllTweets() {
        final MutableLiveData<List<TweetResponse>> data = new MutableLiveData<>();

        Call<List<TweetResponse>> call = tweetService.getAllTweets();
        call.enqueue(new Callback<List<TweetResponse>>() {
            @Override
            public void onResponse(Call<List<TweetResponse>> call, Response<List<TweetResponse>> response) {
                if(response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    try {
                        ErrorResponse error = ConvertToGson.toError(response.errorBody().string());
                        Toast.makeText(MyApplication.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TweetResponse>> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "Problemas con el internet, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });

        return data;
    }
}
