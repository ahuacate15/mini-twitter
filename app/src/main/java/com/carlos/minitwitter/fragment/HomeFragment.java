package com.carlos.minitwitter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.adapter.TweetAdapter;
import com.carlos.minitwitter.data.TweetViewModel;
import com.carlos.minitwitter.retrofit.response.TweetResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeFragment extends Fragment {

    private int mColumn = 1;
    private List<TweetResponse> listTweet;
    private TweetAdapter tweetAdapter;
    private RecyclerView rTweetList;
    private TweetViewModel tweetViewModel;
    private BottomNavigationView bottomNavigationView;

    public HomeFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_list, container, false);

        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        rTweetList = view.findViewById(R.id.rTweetList);

        Context context = view.getContext();
        if(mColumn <= 1) {
            rTweetList.setLayoutManager(new LinearLayoutManager(context));
        } else {
            rTweetList.setLayoutManager(new GridLayoutManager(context, mColumn));
        }

        tweetAdapter = new TweetAdapter(getActivity(), listTweet);
        rTweetList.setAdapter(tweetAdapter);

        loadTweetData();
        return view;
    }

    private void loadTweetData() {
        tweetViewModel.getAllTweets().observe(getActivity(), tweetResponses -> {
            listTweet = tweetResponses;
            tweetAdapter.setData(listTweet);
        });
    }
}
