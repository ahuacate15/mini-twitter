package com.carlos.minitwitter.data;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.carlos.minitwitter.common.ConvertToGson;
import com.carlos.minitwitter.common.MyApplication;
import com.carlos.minitwitter.retrofit.UserClient;
import com.carlos.minitwitter.retrofit.UserService;
import com.carlos.minitwitter.retrofit.response.ErrorResponse;
import com.carlos.minitwitter.retrofit.response.GenericResponse;
import com.carlos.minitwitter.retrofit.response.UserResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private UserService userService;
    private UserClient userClient;
    private MutableLiveData<UserResponse> userProfile;

    private static final String TAG = "UserRepository";

    public UserRepository() {
        this.userClient = UserClient.getInstance();
        this.userService = this.userClient.getUserService();
    }

    public MutableLiveData<UserResponse> fetchProfile() {
        if(userProfile == null) {
            userProfile = new MutableLiveData<>();
        }

        Call<UserResponse> call = userService.getProfile();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()) {
                    userProfile.setValue(response.body());
                } else {
                    try {
                        ErrorResponse error = ConvertToGson.toError(response.errorBody().string());
                        Toast.makeText(MyApplication.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "Problemas con el internet, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });

        return userProfile;
    }

    public MutableLiveData<UserResponse> updateProfile(String field, String value) {
        if(userProfile == null) {
            userProfile = new MutableLiveData<>();
        }

        Call<UserResponse> call = userService.updateProfile(field, value);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()) {
                    UserResponse tmpUser = response.body();
                    tmpUser.setMessage(null);
                    userProfile.setValue(tmpUser);
                } else {
                    try {
                        UserResponse tmpUser = userProfile.getValue();

                        /**
                         * la entidad User contiene un campo llamado message.
                         * cuando hay un error en la carga del perfil, se llena este campo
                         * */
                        ErrorResponse error = ConvertToGson.toError(response.errorBody().string());
                        tmpUser.setMessage(error.getMessage());
                        userProfile.setValue(tmpUser);
                    } catch(IOException e) {
                        UserResponse tmpUser = userProfile.getValue();
                        tmpUser.setMessage("Error al actualizar el dato");
                        userProfile.setValue(tmpUser);

                        Log.d(TAG, "error to updateProfile: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                UserResponse tmpUser = userProfile.getValue();
                tmpUser.setMessage("Problemas con el internet, intenta de nuevo");
                userProfile.setValue(tmpUser);
            }
        });

        return userProfile;

    }
}
