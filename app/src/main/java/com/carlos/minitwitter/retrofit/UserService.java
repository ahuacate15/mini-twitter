package com.carlos.minitwitter.retrofit;

import com.carlos.minitwitter.retrofit.response.GenericResponse;
import com.carlos.minitwitter.retrofit.response.UserResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserService {

    @GET("user/profile")
    public Call<UserResponse> getProfile();

    @PUT("user/profile")
    public Call<UserResponse> updateProfile(@Query("field") String field, @Query("value") String value);

    @Multipart
    @POST("user/photo")
    public Call<UserResponse> uploadPhoto(@Part("image\"; filename=photo.jpeg\" ")RequestBody file);
}
