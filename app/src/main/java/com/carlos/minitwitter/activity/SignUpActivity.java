package com.carlos.minitwitter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.carlos.minitwitter.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnBackToLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int idButton = view.getId();

        switch (idButton) {
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
}