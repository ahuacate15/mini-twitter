package com.carlos.minitwitter.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carlos.minitwitter.R;
import com.carlos.minitwitter.common.Constant;
import com.carlos.minitwitter.common.MyApplication;
import com.carlos.minitwitter.common.SharedPreferencesManager;
import com.carlos.minitwitter.data.UserViewModel;
import com.carlos.minitwitter.fragment.BottomModalTweetFragment;
import com.carlos.minitwitter.fragment.TweetFragment;
import com.carlos.minitwitter.fragment.NewTweetFragment;
import com.carlos.minitwitter.fragment.ProfileFragment;
import com.carlos.minitwitter.retrofit.response.UserResponse;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, NavController.OnDestinationChangedListener, MultiplePermissionsListener {

    private FloatingActionButton bNewTweet;
    private BottomNavigationView bottomNavigationView;
    private Button bCerrarSesion;
    private AppBarLayout appBarLayout;
    private TextView tToolbarUserName;
    private ImageView iToolbarPhoto;
    private UserViewModel userViewModel;
    private View fDashboard;

    private int statusBarHeight = 0;

    private static String TAG = "DashboardActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bCerrarSesion = findViewById(R.id.bCerrarSesion);
        bNewTweet = findViewById(R.id.bNewTweet);
        tToolbarUserName = findViewById(R.id.tToolbarUserName);
        iToolbarPhoto = findViewById(R.id.iToolbarPhoto);
        appBarLayout = findViewById(R.id.appBarLayout);
        fDashboard = findViewById(R.id.fDashboard);

        bNewTweet.setOnClickListener(this::onClick);
        bCerrarSesion.setOnClickListener(this::onClick);

        getSupportActionBar().hide();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration
                .Builder(R.id.navigation_home, R.id.navigation_tweet_fav, R.id.navigation_profile)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.fDashboard);
        navController.addOnDestinationChangedListener(this::onDestinationChanged);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        fetchProfile();
        fetchPhotoProfile();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bNewTweet:
                openNewTweetFragment();
                break;
            case R.id.bCerrarSesion:
                closeSession();
                break;
        }

    }

    private void openNewTweetFragment() {
        NewTweetFragment dialog = new NewTweetFragment();
        dialog.show(getSupportFragmentManager(), "NewTweetFragment");
    }

    private void closeSession() {
        SharedPreferencesManager.clearData();
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void fetchProfile() {
        userViewModel.fetchProfile().observe(this, new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                tToolbarUserName.setText(userResponse.getUserName());

                /* cargo la foto del perfil del usuario logueado */
                if(userResponse.getPhotoUrl() != null && !userResponse.getPhotoUrl().equals("")) {
                    Glide.with(MyApplication.getContext())
                            .load(Constant.API_URL + userResponse.getPhotoUrl())
                            .error(R.drawable.ic_baseline_account_circle_24)
                            .dontAnimate()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(iToolbarPhoto);
                }
            }
        });
    }

    private void fetchPhotoProfile() {
        userViewModel.fetchPhotoProfile().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String photo) {
                /* cargo la foto del perfil del usuario logueado */
                if(photo != null && !photo.equals("")) {
                    Glide.with(MyApplication.getContext())
                            .load(Constant.API_URL + photo)
                            .error(R.drawable.ic_baseline_account_circle_24)
                            .dontAnimate()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(iToolbarPhoto);
                }
            }
        });
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {


        switch (destination.getId()) {

            case R.id.navigation_home:
                //appBarLayout.setVisibility(View.VISIBLE);
                bNewTweet.show();
                break;
            case R.id.navigation_tweet_fav:
                //appBarLayout.setVisibility(View.VISIBLE);
                bNewTweet.hide();
                break;
            case R.id.navigation_profile:
                //appBarLayout.setVisibility(View.GONE);
                bNewTweet.hide();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case Constant.SELECT_PHOTO_GALERY:
                    if(data != null) {
                        Uri imagen = data.getData();
                        Cursor cursor = getContentResolver().query(imagen, new String[] {MediaStore.Images.Media.DATA}, null, null, null);
                        if(cursor != null) {
                            cursor.moveToFirst();
                            int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                            String path = cursor.getString(index);

                            userViewModel.uploadPhoto(path);

                            cursor.close();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constant.SELECT_PHOTO_GALERY);
        Log.d(TAG, "permissionChecked");
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
        Log.d(TAG, "permissionShouldBeShown");
    }

}
