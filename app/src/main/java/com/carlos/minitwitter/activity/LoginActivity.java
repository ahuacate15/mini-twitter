package com.carlos.minitwitter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.carlos.minitwitter.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLogin;
    Button btnOpenSingUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnOpenSingUp = findViewById(R.id.btnOpenSingUp);

        btnLogin.setOnClickListener(this);
        btnOpenSingUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int idButton = view.getId();
        switch (idButton) {
            case R.id.btnLogin:
                break;
            case R.id.btnOpenSingUp:
                openSingUp();
                break;
        }
    }

    private void openSingUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}