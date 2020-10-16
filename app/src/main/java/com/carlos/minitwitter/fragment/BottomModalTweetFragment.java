package com.carlos.minitwitter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.common.Constant;
import com.carlos.minitwitter.data.TweetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomModalTweetFragment extends BottomSheetDialogFragment implements NavigationView.OnNavigationItemSelectedListener {

    private TweetViewModel tweetViewModel;
    private NavigationView navTweetBottom;
    private int idTweet;

    public static BottomModalTweetFragment newInstance(int idTweet) {
        BottomModalTweetFragment fragment = new BottomModalTweetFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.TWEET_ID_PARAM, idTweet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            idTweet = getArguments().getInt(Constant.TWEET_ID_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweet_bottom_modal, container, false);
        navTweetBottom = view.findViewById(R.id.nav_tweet_bottom);
        navTweetBottom.setNavigationItemSelectedListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_tweet:
                tweetViewModel.delete(idTweet);
                getDialog().dismiss();
                return true;
        }
        return false;
    }
}
