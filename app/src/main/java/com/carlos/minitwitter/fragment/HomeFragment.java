package com.carlos.minitwitter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.adapter.TweetAdapter;
import com.carlos.minitwitter.data.TweetViewModel;
import com.carlos.minitwitter.retrofit.response.TweetResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private int mColumn = 1;
    private List<TweetResponse> listTweet;
    private TweetAdapter tweetAdapter;
    private SwipeRefreshLayout swTweetList;
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
        swTweetList = view.findViewById(R.id.swTweetList);

        swTweetList.setOnRefreshListener(this::onRefresh);
        swTweetList.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));

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

            /**
             * la lista de tweets posee 2 observer de forma simultanea, desactivo este
             * una vez cargada se haya cargado la lista. el segundo observer (fetchAllTweets) queda activo
             */
            tweetViewModel.getAllTweets().removeObservers(this);
        });
    }

    private void fetchAllTweets() {
        tweetViewModel.fetchAllTweets().observe(getActivity(), tweetResponses -> {
            swTweetList.setRefreshing(false);
            listTweet = tweetResponses;
            tweetAdapter.setData(listTweet);
        });
    }

    @Override
    public void onRefresh() {
        swTweetList.setRefreshing(true);
        fetchAllTweets();
    }
}
