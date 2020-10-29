package com.carlos.minitwitter.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.data.UserViewModel;
import com.carlos.minitwitter.retrofit.response.UserResponse;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import static android.content.ContentValues.TAG;

public class BottomModalEditProfile extends BottomSheetDialogFragment implements View.OnClickListener{

    private EditText tFieldEditProfile;
    private TextView tMessageEditProfile;
    private TextView tLengthEditProfile;
    private TextInputLayout tlFieldEditProfile;
    private Button bGuardarEditProfile;
    private Button bCancelarEditProfile;

    private UserResponse userResponse;
    private UserViewModel userViewModel;

    public static String PARAM_FIELD = "param_field";
    public static int FIELD_NAME = 1;
    public static int FIELD_LASTNAME = 2;
    public static int FIELD_EMAIL = 3;
    public static int FIELD_USERNAME = 4;

    public static int LENGTH_NAME = 64;
    public static int LENGTH_LASTNAME = 64;
    public static int LENGTH_EMAIL = 256;
    public static int LENGTH_USERNAME = 35;

    public static BottomModalEditProfile newInstance(int field) {
        BottomModalEditProfile fragment = new BottomModalEditProfile();
        Bundle args = new Bundle();
        args.putInt(PARAM_FIELD, field);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_bottom_modal, container,false );

        tFieldEditProfile = view.findViewById(R.id.tFieldEditProfile);
        tMessageEditProfile = view.findViewById(R.id.tMessageEditProfile);
        tLengthEditProfile = view.findViewById(R.id.tLengthEditProfile);
        tlFieldEditProfile = view.findViewById(R.id.tlFieldEditProfile);
        bGuardarEditProfile = view.findViewById(R.id.bGuardarEditProfile);
        bCancelarEditProfile = view.findViewById(R.id.bCancelarEditProfile);

        bGuardarEditProfile.setOnClickListener(this::onClick);
        bCancelarEditProfile.setOnClickListener(this::onClick);
        tFieldEditProfile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(getArguments() != null) {
                    setLengthTextLabel(getArguments().getInt(PARAM_FIELD), tFieldEditProfile.getText().toString());
                }

            }
        });

        if(getArguments() != null) {
            setFields(getArguments().getInt(PARAM_FIELD));
        }

        return view;
    }

    private void setFields(int field) {
        int lengthField = 0;
        String value = "";
        String hint = "";

        if(field == FIELD_NAME) {
            lengthField = LENGTH_NAME;
            value = userResponse.getName();
            hint = "Ingresa tu nombre";
        } else if(field == FIELD_LASTNAME) {
            lengthField = LENGTH_LASTNAME;
            value = userResponse.getLastname();
            hint = "Ingresa tu apellido";
        } else if(field == FIELD_EMAIL) {
            lengthField = LENGTH_EMAIL;
            value = userResponse.getEmail();
            hint = "Ingresa tu correo electrónico";
        } else if(field == FIELD_USERNAME) {
            lengthField = LENGTH_USERNAME;
            value = userResponse.getUserName();
            hint = "Ingresa tu nombre de usuario";
        } else {
            return;
        }

        tFieldEditProfile.setText(value);
        tFieldEditProfile.setFilters(new InputFilter[] {new InputFilter.LengthFilter(lengthField)}); //longitud maxima
        tMessageEditProfile.setVisibility(View.GONE); //oculto el mensaje de error
        tLengthEditProfile.setText(value.length() + "/" + lengthField);
        tlFieldEditProfile.setHint(hint);
    }

    public void setLengthTextLabel(int field, String value) {
        int lengthField = 0;

        if(field == FIELD_NAME) {
            lengthField = LENGTH_NAME;
        } else if(field == FIELD_LASTNAME) {
            lengthField = LENGTH_LASTNAME;
        } else if(field == FIELD_EMAIL) {
            lengthField = LENGTH_EMAIL;
        } else if(field == FIELD_USERNAME) {
            lengthField = LENGTH_USERNAME;
        } else {
            return;
        }

        tLengthEditProfile.setText(value.length() + "/" + lengthField);

        /* caracteres disponibles para escribir */
        if(value.length() < lengthField) {
            tLengthEditProfile.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }
        /* al llegar al limite de longitud de la cadena, el texto se muestra en rojo indicando que ya no se puede escribir */
        else {
            tLengthEditProfile.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRedDarken2));
        }
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    private void updateProfile() {
        String field = "";
        String value = "";


        if(getArguments() == null) {
            return;
        }

        int selectedField = getArguments().getInt(PARAM_FIELD);

        if(selectedField == FIELD_NAME) {
            field = "name";
        } else if(selectedField == FIELD_LASTNAME) {
            field = "lastname";
        } else if(selectedField == FIELD_EMAIL) {
            field = "email";
        } else if(selectedField == FIELD_USERNAME) {
            field = "user_name";
        } else {
            return;
        }

        value = tFieldEditProfile.getText().toString();

        userViewModel.updateProfile(field, value).observe(getActivity(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                Log.d(TAG, "editar perfil");

                if(userResponse.getMessage() != null) {
                    /* error al actualizar el perfil */
                    tMessageEditProfile.setVisibility(View.VISIBLE);
                    tMessageEditProfile.setText(userResponse.getMessage());
                } else {
                    tMessageEditProfile.setVisibility(View.GONE);

                    /* perfil actualizado, cierro el modal */
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bGuardarEditProfile:
                updateProfile();
                break;
            case R.id.bCancelarEditProfile:
                dismiss();
                break;
        }
    }
}
