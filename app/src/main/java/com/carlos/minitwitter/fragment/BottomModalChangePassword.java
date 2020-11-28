package com.carlos.minitwitter.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.util.PasswordUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomModalChangePassword extends BottomSheetDialogFragment implements View.OnClickListener {

    private EditText tOriginalPassword;
    private EditText tNewPassword;
    private EditText tConfirmPassword;
    private Button bChangePasswordModal;
    private TextView tErrorOriginalPassword;
    private TextView tErrorNewPassword;
    private TextView tErrorConfirmPassword;
    private PasswordUtils passwordUtils;

    private static final String TAG = "BottomModalChangePassword";

    public static BottomModalChangePassword newInstance() {
        BottomModalChangePassword fragment = new BottomModalChangePassword();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwordUtils = new PasswordUtils();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password_modal, container, false);
        tOriginalPassword = view.findViewById(R.id.tOriginalPassword);
        tNewPassword = view.findViewById(R.id.tNewPassword);
        tConfirmPassword = view.findViewById(R.id.tConfirmPassword);
        bChangePasswordModal = view.findViewById(R.id.bChangePasswordModal);
        tErrorOriginalPassword = view.findViewById(R.id.tErrorOriginalPassword);
        tErrorNewPassword = view.findViewById(R.id.tErrorNewPassword);
        tErrorConfirmPassword = view.findViewById(R.id.tErrorConfirmPassword);
        bChangePasswordModal.setOnClickListener(this::onClick);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bChangePasswordModal:
                changePassword();
                break;
        }
    }

    public void changePassword() {
        String originalPassword = tOriginalPassword.getText().toString();
        String newPassword = tNewPassword.getText().toString();
        String confirmPassword = tConfirmPassword.getText().toString();

        passwordUtils.setOriginalPassword(originalPassword);
        passwordUtils.setNewPassword(newPassword);
        passwordUtils.setConfirmPassword(confirmPassword);

        tErrorOriginalPassword.setVisibility(View.GONE);
        tErrorNewPassword.setVisibility(View.GONE);
        tErrorConfirmPassword.setVisibility(View.GONE);

        switch (passwordUtils.validateChangePassword()) {
            case PasswordUtils.OK:
                Log.d(TAG, "enviando password");
                break;
            case PasswordUtils.ORIGINAL_PASSWORD_EMPTY:
                tOriginalPassword.requestFocus();
                tOriginalPassword.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorRedDarken4), PorterDuff.Mode.SRC_ATOP);
                tErrorOriginalPassword.setText("El campo es obligatorio");
                tErrorOriginalPassword.setVisibility(View.VISIBLE);
                break;
            case PasswordUtils.NEW_PASSWORD_EMPTY:
                tNewPassword.requestFocus();
                tNewPassword.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorRedDarken4), PorterDuff.Mode.SRC_ATOP);
                tErrorNewPassword.setText("El campo es obligatorio");
                tErrorNewPassword.setVisibility(View.VISIBLE);
                break;
            case PasswordUtils.CONFIRM_PASSWORD_EMPTY:
                tConfirmPassword.requestFocus();
                tConfirmPassword.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorRedDarken4), PorterDuff.Mode.SRC_ATOP);
                tErrorConfirmPassword.setText("El campo es obligatorio");
                tErrorConfirmPassword.setVisibility(View.VISIBLE);
                break;
            case PasswordUtils.NOT_MATCH:
                tConfirmPassword.requestFocus();
                tConfirmPassword.getBackground().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorRedDarken4), PorterDuff.Mode.SRC_ATOP);
                tErrorConfirmPassword.setText("Las contraseñas no coinciden");
                tErrorConfirmPassword.setVisibility(View.VISIBLE);
                break;
        }
    }
}
