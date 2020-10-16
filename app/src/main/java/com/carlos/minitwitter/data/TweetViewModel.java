package com.carlos.minitwitter.data;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlos.minitwitter.fragment.BottomModalTweetFragment;
import com.carlos.minitwitter.retrofit.response.TweetResponse;

import java.util.List;

public class TweetViewModel extends ViewModel {

    private TweetRepository tweetRepository;
    private LiveData<List<TweetResponse>> listTweet;
    private LiveData<List<TweetResponse>> listFavTweets;

    private static final String TAG = "TweetViewModel";

    public TweetViewModel() {
        tweetRepository = new TweetRepository();
        listTweet = new MutableLiveData<>();
        listFavTweets = new MutableLiveData<>();
    }

    public LiveData<List<TweetResponse>> fetchAllTweets() {
        listTweet = tweetRepository.fetchTweets();
        return listTweet;
    }

    public LiveData<List<TweetResponse>> getFavTweets() {
        listFavTweets = tweetRepository.getFavTweets();
        return listFavTweets;
    }

    public LiveData<List<TweetResponse>> fetchFavTweets() {
        listFavTweets = tweetRepository.fetchFavTweets();
        return listFavTweets;
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

    public void delete(final int idTweet) {
        tweetRepository.delete(idTweet);
    }

    public void openDialogMenuTweet(Context context, int idTweet) {
        BottomModalTweetFragment fragment = BottomModalTweetFragment.newInstance(idTweet);
        fragment.show(((AppCompatActivity) context).getSupportFragmentManager(), TAG);
    }

}
