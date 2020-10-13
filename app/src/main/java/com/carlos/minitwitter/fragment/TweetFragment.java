package com.carlos.minitwitter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.adapter.TweetAdapter;
import com.carlos.minitwitter.common.Constant;
import com.carlos.minitwitter.data.TweetViewModel;
import com.carlos.minitwitter.retrofit.response.TweetResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class TweetFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private int tweetListType = Constant.TWEET_ALL; //por defecto de muestran todos los tweets
    private List<TweetResponse> listTweet;
    private TweetAdapter tweetAdapter;
    private SwipeRefreshLayout swTweetList;
    private RecyclerView rTweetList;
    private TweetViewModel tweetViewModel;
    private BottomNavigationView bottomNavigationView;

    private static final String TAG = "TweetFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);
        Log.d(TAG, "onCreateView");
        tweetListType = getArguments().getInt("tweetListType");
        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        rTweetList = view.findViewById(R.id.rTweetList);
        swTweetList = view.findViewById(R.id.swTweetList);

        swTweetList.setOnRefreshListener(this::onRefresh);
        swTweetList.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        Context context = view.getContext();
        rTweetList.setLayoutManager(new LinearLayoutManager(context));

        tweetAdapter = new TweetAdapter(getActivity(), listTweet);

        rTweetList.setAdapter(tweetAdapter);

        /* cargo todos los tweets */
        if(tweetListType == Constant.TWEET_ALL) {
            fetchAllTweets();
        } else {
            getFavTweets();
        }
        return view;
    }

    private void fetchAllTweets() {
        tweetViewModel.fetchAllTweets().observe(getActivity(), tweetResponses -> {
            swTweetList.setRefreshing(false);
            listTweet = tweetResponses;
            tweetAdapter.setData(listTweet);
            Log.d(TAG, "fetchAllTweets");
        });
    }

    private void getFavTweets() {
        tweetViewModel.getFavTweets().observe(getActivity(), tweetResponses -> {
            swTweetList.setRefreshing(false);
            listTweet = tweetResponses;
            tweetAdapter.setData(listTweet);
            Log.d(TAG, "getFavTweets");
        });
    }

    private void fetchFavTweets() {
        tweetViewModel.fetchFavTweets().observe(getActivity(), new Observer<List<TweetResponse>>() {
            @Override
            public void onChanged(List<TweetResponse> tweetResponses) {
                swTweetList.setRefreshing(false);
                listTweet = tweetResponses;
                tweetAdapter.setData(listTweet);
                Log.d(TAG, "fetchFavTweets");
            }
        });
    }
    @Override
    public void onRefresh() {
        swTweetList.setRefreshing(true);
        if(tweetListType == Constant.TWEET_ALL) {
            fetchAllTweets();
        } else {
            fetchFavTweets();
        }

    }
}
