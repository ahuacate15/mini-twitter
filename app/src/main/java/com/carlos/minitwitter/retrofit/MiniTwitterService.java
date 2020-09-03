package com.carlos.minitwitter.retrofit;

import com.carlos.minitwitter.retrofit.request.LoginRequest;
import com.carlos.minitwitter.retrofit.response.LoginResponse;
import com.carlos.minitwitter.retrofit.request.SignupRequest;
import com.carlos.minitwitter.retrofit.response.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MiniTwitterService {

    @POST("auth/login")
    Call<LoginResponse> doLogin(@Body LoginRequest request);

    @POST("auth/signup")
    Call<SignupResponse> doSignup(@Body SignupRequest signupRequest);
}
