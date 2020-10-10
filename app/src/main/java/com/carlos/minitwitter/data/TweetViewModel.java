package com.carlos.minitwitter.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.carlos.minitwitter.retrofit.response.TweetResponse;

import java.util.List;

public class TweetViewModel extends ViewModel {

    private TweetRepository tweetRepository;
    private LiveData<List<TweetResponse>> listTweet;

    public TweetViewModel() {
        tweetRepository = new TweetRepository();
        listTweet = tweetRepository.fetchTweets();
    }

    public LiveData<List<TweetResponse>> fetchAllTweets() {
        listTweet = tweetRepository.fetchTweets();
        return listTweet;
    }

    public LiveData<List<TweetResponse>> getAllTweets() {
        return listTweet;
    }

    public void createNewTweet(String message) {
        tweetRepository.createNewTweet(message);
    }

    public void like(int idTweet) {
        tweetRepository.like(idTweet);
    }

    public void unlike(int idTweet) {
        tweetRepository.unlike(idTweet);
    }

}
