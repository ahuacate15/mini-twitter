package com.carlos.minitwitter.retrofit;

import com.carlos.minitwitter.common.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserClient {

    private static UserClient instance = null;
    public UserService userService;
    private Retrofit retrofit;

    public UserClient() {
        /* incluir JWT a las peticiones */
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient client = clientBuilder.build();

        /* parse de fechas */
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        userService = retrofit.create(UserService.class);
    }

    public static UserClient getInstance() {
        if(instance == null)
            return new UserClient();
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }
}
