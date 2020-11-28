package com.carlos.minitwitter.fragment;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.carlos.minitwitter.R;
import com.carlos.minitwitter.common.Constant;
import com.carlos.minitwitter.common.MyApplication;
import com.carlos.minitwitter.common.SharedPreferencesManager;
import com.carlos.minitwitter.data.UserViewModel;
import com.carlos.minitwitter.retrofit.response.UserResponse;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView tNameProfile;
    private TextView tLastnameProfile;
    private TextView tEmailProfile;
    private TextView tUsernameProfile;
    private ImageView iPhotoProfile;
    private ImageView iNameProfile;
    private ImageView iLastnameProfile;
    private ImageView iEmailProfile;
    private ImageView iUsernameProfile;
    private Button bChangePasswordProfile;

    private MultiplePermissionsListener permissionListener;

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
        iPhotoProfile = view.findViewById(R.id.iPhotoProfile);
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
        iPhotoProfile.setOnClickListener(this::onClick);
        bChangePasswordProfile.setOnClickListener(this::onClick);

        /* cargo los datos del usuario */
        fetchProfile();

        /* cargo la foto de perfil del usuario */
        fetchPhotoProfile();
        return view;
    }

    @Override
    public void onClick(View view) {

        BottomModalEditProfile fragment = null;


        /* utilizo el mismo fragmento para dintintos campos del perfil de usuario */
        switch (view.getId()) {

            /**
             * cuando se cambia el password se utiliza un modal diferente, por eso se incluye
             * el return para este case*/
            case R.id.bChangePasswordProfile:
                BottomModalChangePassword fragmentChangePass = BottomModalChangePassword.newInstance();
                fragmentChangePass.show(getActivity().getSupportFragmentManager(), TAG);
                return;
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
            case R.id.iPhotoProfile:
                Log.d(TAG, "uplading photo");
                checkPermission();
                break;
            default: /* click a un componente distinto, no debe ocurrir nada */
                return;
        }

        if (fragment != null) {
            fragment.setUserResponse(user);
            fragment.show(getActivity().getSupportFragmentManager(), TAG);
        }
    }

    private void fetchPhotoProfile() {

        //cargo la foto al entrar en el fragmento
        String photoUrlLocal = SharedPreferencesManager.getString(Constant.PREF_PHOTO);
        if(photoUrlLocal != null && !photoUrlLocal.equals("")) {
            Glide.with(MyApplication.getContext())
                    .load(Constant.API_URL + photoUrlLocal)
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .dontAnimate()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iPhotoProfile);
        }

        //refresco el imageView cada vez que se cambie la foto
        userViewModel.fetchPhotoProfile().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String url) {
                if(url != null && !url.equals("")) {
                    Glide.with(MyApplication.getContext())
                            .load(Constant.API_URL + user.getPhotoUrl())
                            .error(R.drawable.ic_baseline_account_circle_24)
                            .dontAnimate()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(iPhotoProfile);
                }
            }
        });
    }

    private void fetchProfile() {
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

    public void checkPermission() {
        MultiplePermissionsListener permissionsListener =
                DialogOnAnyDeniedMultiplePermissionsListener.Builder
                    .withContext(getActivity())
                    .withTitle("Permisos")
                    .withMessage("Los permisos solicitados son necesarios para cargar tu foto de perfil")
                    .build();

        this.permissionListener = new CompositeMultiplePermissionsListener((MultiplePermissionsListener)  getActivity(), permissionsListener);

        Dexter.withContext(getActivity())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(this.permissionListener)
                .check();
    }
}
