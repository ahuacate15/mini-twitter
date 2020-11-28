package com.carlos.minitwitter.retrofit;

import com.carlos.minitwitter.retrofit.request.LoginRequest;
import com.carlos.minitwitter.retrofit.response.GenericResponse;
import com.carlos.minitwitter.retrofit.response.LoginResponse;
import com.carlos.minitwitter.retrofit.request.SignupRequest;
import com.carlos.minitwitter.retrofit.response.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface MiniTwitterService {

    @POST("auth/login")
    Call<LoginResponse> doLogin(@Body LoginRequest request);

    @POST("auth/signup")
    Call<SignupResponse> doSignup(@Body SignupRequest signupRequest);

    @PUT("auth/password")
    Call<GenericResponse> changePassword(@Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);

}
