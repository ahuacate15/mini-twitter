package com.carlos.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.carlos.minitwitter.common.ConvertToGson;
import com.carlos.minitwitter.common.MyApplication;
import com.carlos.minitwitter.retrofit.TweetClient;
import com.carlos.minitwitter.retrofit.TweetService;
import com.carlos.minitwitter.retrofit.request.TweetRequest;
import com.carlos.minitwitter.retrofit.response.ErrorResponse;
import com.carlos.minitwitter.retrofit.response.GenericResponse;
import com.carlos.minitwitter.retrofit.response.TweetResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private TweetService tweetService;
    private TweetClient tweetClient;
    private MutableLiveData<List<TweetResponse>> allTweets;

    public TweetRepository() {
        this.tweetClient = TweetClient.getInstance();
        this.tweetService = this.tweetClient.getTweetService();
        fetchTweets();
    }

    public LiveData<List<TweetResponse>> getAllTweets() {
        return allTweets;
    }

    public MutableLiveData<List<TweetResponse>> fetchTweets() {

        if(allTweets == null) {
            allTweets = new MutableLiveData<>();
        }

        Call<List<TweetResponse>> call = tweetService.getAllTweets();
        call.enqueue(new Callback<List<TweetResponse>>() {
            @Override
            public void onResponse(Call<List<TweetResponse>> call, Response<List<TweetResponse>> response) {
                if(response.isSuccessful()) {
                    allTweets.setValue(response.body());
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

        return allTweets;
    }

    public void createNewTweet(String message) {
        TweetRequest tweet = new TweetRequest(message);
        Call<TweetResponse> call = tweetService.create(tweet);

        call.enqueue(new Callback<TweetResponse>() {
            @Override
            public void onResponse(Call<TweetResponse> call, Response<TweetResponse> response) {
                if(response.isSuccessful()) {
                    List<TweetResponse> listClone = new ArrayList<>();
                    listClone.add(response.body());

                    int totalTweets = allTweets.getValue().size();
                    for(int i=0; i<totalTweets; i++) {
                        listClone.add(new TweetResponse(allTweets.getValue().get(i)));
                    }
                    allTweets.setValue(listClone);
                    Toast.makeText(MyApplication.getContext(), "Tu tweet ha sido creado", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<TweetResponse> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "Problemas con el internet, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void like(int idTweet) {
        Call<TweetResponse> call = tweetService.like(idTweet);

        call.enqueue(new Callback<TweetResponse>() {
            @Override
            public void onResponse(Call<TweetResponse> call, Response<TweetResponse> response) {
               if(response.isSuccessful()) {
                   allTweets.setValue(getUpdatedListTweet(allTweets.getValue(), response.body()));
               } else {
                    try {
                        ErrorResponse error = ConvertToGson.toError(response.errorBody().string());
                        Toast.makeText(MyApplication.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
               }
            }

            @Override
            public void onFailure(Call<TweetResponse> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "Problemas con el internet, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void unlike(int idTweet) {
        Call<TweetResponse> call = tweetService.unlike(idTweet);

        call.enqueue(new Callback<TweetResponse>() {
            @Override
            public void onResponse(Call<TweetResponse> call, Response<TweetResponse> response) {
                if(response.isSuccessful()) {
                    allTweets.setValue(getUpdatedListTweet(allTweets.getValue(), response.body()));
                } else {
                    try {
                        ErrorResponse error = ConvertToGson.toError(response.errorBody().string());
                        Toast.makeText(MyApplication.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TweetResponse> call, Throwable t) {

            }
        });
    }

    /**
     * reemplazo el tweet de una lista, buscando por id
     */
    private List<TweetResponse> getUpdatedListTweet(List<TweetResponse> listTweet, TweetResponse tweetResponse) {

        int totalTweets = listTweet.size();
        for(int i=0; i<totalTweets; i++) {
            if(listTweet.get(i).getIdTweet() == tweetResponse.getIdTweet()) {
                listTweet.set(i, tweetResponse);
                break;
            }
        }

        return listTweet;
    }
}
