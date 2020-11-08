package com.carlos.minitwitter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.common.Constant;
import com.carlos.minitwitter.common.ConvertToGson;
import com.carlos.minitwitter.common.SharedPreferencesManager;
import com.carlos.minitwitter.fragment.BottomModalTweetFragment;
import com.carlos.minitwitter.retrofit.MiniTwitterClient;
import com.carlos.minitwitter.retrofit.MiniTwitterService;
import com.carlos.minitwitter.retrofit.request.LoginRequest;
import com.carlos.minitwitter.retrofit.response.ErrorResponse;
import com.carlos.minitwitter.retrofit.response.LoginResponse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin;
    Button btnOpenSingUp;
    EditText txtEmailLogin;
    EditText txtPasswordLogin;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        String userName = SharedPreferencesManager.getString(Constant.PREF_USERNAME);

        /* sesion iniciado, inicia el dashboard */
        if(userName == null) {
            initRetrofit();
            findViewToElements();
            setActions();
        } else {
            openDashboard();
        }
    }


    public void initRetrofit() {
        miniTwitterClient = new MiniTwitterClient();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    public void findViewToElements() {
        btnLogin = findViewById(R.id.btnLogin);
        btnOpenSingUp = findViewById(R.id.btnOpenSingUp);
        txtEmailLogin = findViewById(R.id.txtEmailLogin);
        txtPasswordLogin = findViewById(R.id.txtPasswordLogin);
    }

    public void setActions() {
        btnLogin.setOnClickListener(this);
        btnOpenSingUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int idButton = view.getId();
        switch (idButton) {
            case R.id.btnLogin:
                try {
                    doLogin();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnOpenSingUp:
                openSingUp();
                break;
        }
    }

    public void doLogin() throws JSONException {
        String key = txtEmailLogin.getText().toString();
        String password = txtPasswordLogin.getText().toString();

        LoginRequest request = new LoginRequest(key, password);

        Call<LoginResponse> call = miniTwitterService.doLogin(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "usuario: " + response.body());
                    SharedPreferencesManager.setString(Constant.PREF_TOKEN, response.body().getJwt());
                    SharedPreferencesManager.setString(Constant.PREF_USERNAME, response.body().getUserName());
                    SharedPreferencesManager.setString(Constant.PREF_EMAIL, response.body().getEmail());
                    SharedPreferencesManager.setString(Constant.PREF_PHOTO, response.body().getPhotoUrl());
                    Toast.makeText(LoginActivity.this, "Inicio sesion exitoso" , Toast.LENGTH_SHORT).show();
                    openDashboard();
                } else {
                    try {
                        ErrorResponse error = ConvertToGson.toError(response.errorBody().string());
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Problemas con el internet, intenta de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openSingUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void openDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}