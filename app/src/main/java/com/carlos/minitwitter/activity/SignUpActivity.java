package com.carlos.minitwitter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.common.Constant;
import com.carlos.minitwitter.common.ConvertToGson;
import com.carlos.minitwitter.common.SharedPreferencesManager;
import com.carlos.minitwitter.retrofit.MiniTwitterClient;
import com.carlos.minitwitter.retrofit.MiniTwitterService;
import com.carlos.minitwitter.retrofit.request.SignupRequest;
import com.carlos.minitwitter.retrofit.response.ErrorResponse;
import com.carlos.minitwitter.retrofit.response.SignupResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtUsernameSignUp;
    EditText txtEmailSignUp;
    EditText txtPasswordSignUp;
    EditText txtPasswordConfirmationSignUp;
    Button btnSignUp;
    Button btnBackToLogin;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        initRetrofit();
        findViewToElements();
        setActions();

    }

    public void initRetrofit() {
        miniTwitterClient = new MiniTwitterClient();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void findViewToElements() {
        this.txtUsernameSignUp = findViewById(R.id.txtUsernameSingUp);
        this.txtEmailSignUp = findViewById(R.id.txtEmailSingUp);
        this.txtPasswordSignUp = findViewById(R.id.txtPasswordSingUp);
        this.txtPasswordConfirmationSignUp = findViewById(R.id.txtPasswordConfirmationSingUp);
        this.btnSignUp = findViewById(R.id.btnSingUp);
    }

    private void setActions() {
        btnBackToLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int idButton = view.getId();

        switch (idButton) {
            case R.id.btnSingUp:
                doSignUp();
                break;
            case R.id.btnBackToLogin:
                goBackToLogin();;
                break;
        }
    }

    private void goBackToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void doSignUp() {
        String userName = this.txtUsernameSignUp.getText().toString();
        String email = this.txtEmailSignUp.getText().toString();
        String password = this.txtPasswordSignUp.getText().toString();
        String passwordConfirmation = this.txtPasswordConfirmationSignUp.getText().toString();

        if(!password.equals(passwordConfirmation)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        SignupRequest request = new SignupRequest();
        request.setUserName(userName);
        request.setEmail(email);
        request.setPassword(password);

        Call<SignupResponse> call = miniTwitterService.doSignup(request);

        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if(response.isSuccessful()) {
                    SharedPreferencesManager.setString(Constant.PREF_TOKEN, response.body().getJwt());
                    SharedPreferencesManager.setString(Constant.PREF_USERNAME, response.body().getUserName());
                    SharedPreferencesManager.setString(Constant.PREF_EMAIL, response.body().getEmail());

                    Toast.makeText(SignUpActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        ErrorResponse error = ConvertToGson.toError(response.errorBody().string());
                        Toast.makeText(SignUpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Problemas con el internet, intenta de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}