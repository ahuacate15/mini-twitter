package com.carlos.minitwitter.retrofit;

import com.carlos.minitwitter.common.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TweetClient {

    private static TweetClient instance = null;
    private TweetService tweetService;
    private Retrofit retrofit;

    public TweetClient() {

        //incluir JWT a las peticiones
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient client = clientBuilder.build();

        //parseo de las fechas
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        tweetService = retrofit.create(TweetService.class);
    }

    public static  TweetClient getInstance() {
        if(instance == null)
            return new TweetClient();
        return instance;
    }

    public TweetService getTweetService() {
        return tweetService;
    }
}
