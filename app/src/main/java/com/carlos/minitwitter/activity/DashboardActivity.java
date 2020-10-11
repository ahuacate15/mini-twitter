package com.carlos.minitwitter.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.fragment.TweetFragment;
import com.carlos.minitwitter.fragment.NewTweetFragment;
import com.carlos.minitwitter.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton bNewTweet;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bNewTweet = findViewById(R.id.bNewTweet);
        bNewTweet.setOnClickListener(this);

        getSupportActionBar().hide();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_tweet_fav, R.id.navigation_profile).build();
        NavController navController = Navigation.findNavController(this, R.id.fDashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bNewTweet) {
            openNewTweetFragment();
        }
    }

    private void openNewTweetFragment() {
        NewTweetFragment dialog = new NewTweetFragment();
        dialog.show(getSupportFragmentManager(), "NewTweetFragment");
    }
}
