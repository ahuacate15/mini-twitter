package com.carlos.minitwitter.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.carlos.minitwitter.R;
import com.carlos.minitwitter.data.UserViewModel;
import com.carlos.minitwitter.retrofit.response.UserResponse;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView tNameProfile;
    private TextView tLastnameProfile;
    private TextView tEmailProfile;
    private TextView tUsernameProfile;
    private ImageView iNameProfile;
    private ImageView iLastnameProfile;
    private ImageView iEmailProfile;
    private ImageView iUsernameProfile;
    private Button bChangePasswordProfile;

    private UserResponse user;
    private UserViewModel userViewModel;

    private static String TAG = "ProfileFragment";

    public ProfileFragment() {}

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tNameProfile = view.findViewById(R.id.tNameProfile);
        tLastnameProfile = view.findViewById(R.id.tLastnameProfile);
        tEmailProfile = view.findViewById(R.id.tEmailProfile);
        tUsernameProfile = view.findViewById(R.id.tUsernameProfile);
        iNameProfile = view.findViewById(R.id.iNameProfile);
        iLastnameProfile = view.findViewById(R.id.iLastnameProfile);
        iEmailProfile = view.findViewById(R.id.iEmailProfile);
        iUsernameProfile = view.findViewById(R.id.iUsernameProfile);
        bChangePasswordProfile = view.findViewById(R.id.bChangePasswordProfile);

        /* eventos de los controles */
        iNameProfile.setOnClickListener(this::onClick);
        iLastnameProfile.setOnClickListener(this::onClick);
        iEmailProfile.setOnClickListener(this::onClick);
        iUsernameProfile.setOnClickListener(this::onClick);

        /* cargo los datos del usuario */
        fetchUProfile();

        return view;
    }

    @Override
    public void onClick(View view) {

        BottomModalEditProfile fragment = null;

        /* utilizo el mismo fragmento para dintintos campos del perfil de usuario */
        switch (view.getId()) {
            case R.id.iNameProfile:
                fragment = BottomModalEditProfile.newInstance(BottomModalEditProfile.FIELD_NAME);
                break;
            case R.id.iLastnameProfile:
                fragment = BottomModalEditProfile.newInstance(BottomModalEditProfile.FIELD_LASTNAME);
                break;
            case R.id.iUsernameProfile:
                fragment = BottomModalEditProfile.newInstance(BottomModalEditProfile.FIELD_USERNAME);
                break;
            case R.id.iEmailProfile:
                fragment = BottomModalEditProfile.newInstance(BottomModalEditProfile.FIELD_EMAIL);
                break;
            default: /* click a un componente distinto, no debe ocurrir nada */
                return;
        }

        if(fragment != null) {
            fragment.setUserResponse(user);
            fragment.show(getActivity().getSupportFragmentManager(), TAG);
        }
    }

    private void fetchUProfile() {
        userViewModel.fetchProfile().observe(getActivity(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                user = userResponse;
                tNameProfile.setText(user.getName());
                tLastnameProfile.setText(user.getLastname());
                tEmailProfile.setText(user.getEmail());
                tUsernameProfile.setText(user.getUserName());

                Log.d(TAG, "perfil de usuario: " + user);
            }
        });
    }
}
