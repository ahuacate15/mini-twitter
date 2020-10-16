package com.carlos.minitwitter.data;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.carlos.minitwitter.common.ConvertToGson;
import com.carlos.minitwitter.common.MyApplication;
import com.carlos.minitwitter.retrofit.TweetClient;
import com.carlos.minitwitter.retrofit.TweetService;
import com.carlos.minitwitter.retrofit.request.TweetRequest;
import com.carlos.minitwitter.retrofit.response.ErrorResponse;
import com.carlos.minitwitter.retrofit.response.GenericResponse;
import com.carlos.minitwitter.retrofit.response.TweetResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    private TweetService tweetService;
    private TweetClient tweetClient;
    private MutableLiveData<List<TweetResponse>> allTweets;
    private MutableLiveData<List<TweetResponse>> favTweets;

    private static final String TAG = "TweetRepository";

    public TweetRepository() {
        this.tweetClient = TweetClient.getInstance();
        this.tweetService = this.tweetClient.getTweetService();
        fetchTweets();
    }

    public MutableLiveData<List<TweetResponse>> fetchTweets() {

        if(allTweets == null) {
            allTweets = new MutableLiveData<>();
        }

        if(favTweets == null) {
            favTweets = new MutableLiveData<>();
        }

        Call<List<TweetResponse>> call = tweetService.getAllTweets();
        call.enqueue(new Callback<List<TweetResponse>>() {
            @Override
            public void onResponse(Call<List<TweetResponse>> call, Response<List<TweetResponse>> response) {
                if(response.isSuccessful()) {
                    allTweets.setValue(response.body());
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
            public void onFailure(Call<List<TweetResponse>> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "Problemas con el internet, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });

        return allTweets;
    }

    public MutableLiveData<List<TweetResponse>> getFavTweets() {
        favTweets.setValue(getFavoriteTweetsFromList(allTweets.getValue()));
        return favTweets;
    }

    public MutableLiveData<List<TweetResponse>> fetchFavTweets() {
        if(favTweets == null) {
            favTweets = new MutableLiveData<>();
        }

        Call<List<TweetResponse>> call = tweetService.getFavTweets();
        call.enqueue(new Callback<List<TweetResponse>>() {
            @Override
            public void onResponse(Call<List<TweetResponse>> call, Response<List<TweetResponse>> response) {
                if(response.isSuccessful()) {
                    favTweets.setValue(response.body());
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
            public void onFailure(Call<List<TweetResponse>> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "Problemas con el internet, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
        return favTweets;
    }

    public void createNewTweet(String message) {
        TweetRequest tweet = new TweetRequest(message);
        Call<TweetResponse> call = tweetService.create(tweet);

        call.enqueue(new Callback<TweetResponse>() {
            @Override
            public void onResponse(Call<TweetResponse> call, Response<TweetResponse> response) {
                if(response.isSuccessful()) {
                    List<TweetResponse> listClone = new ArrayList<>();
                    listClone.add(response.body());

                    int totalTweets = allTweets.getValue().size();
                    for(int i=0; i<totalTweets; i++) {
                        listClone.add(new TweetResponse(allTweets.getValue().get(i)));
                    }
                    allTweets.setValue(listClone);
                    Toast.makeText(MyApplication.getContext(), "Tu tweet ha sido creado", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<TweetResponse> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "Problemas con el internet, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void like(int idTweet) {

        Call<TweetResponse> call = tweetService.like(idTweet);

        call.enqueue(new Callback<TweetResponse>() {
            @Override
            public void onResponse(Call<TweetResponse> call, Response<TweetResponse> response) {
               if(response.isSuccessful()) {
                   /* marco un tweet como favorito */
                   allTweets.setValue(getUpdatedListTweet(allTweets.getValue(), response.body()));

                   /* agrego un tweet a la lista de favoritos */
                   List<TweetResponse> tmpFavTweets = favTweets.getValue();
                   tmpFavTweets.add(response.body());
                   favTweets.setValue(tmpFavTweets);

                   Log.d(TAG, "like tweet");
               } else {
                    try {
                        ErrorResponse error = ConvertToGson.toError(response.errorBody().string());
                        Toast.makeText(MyApplication.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
               }
            }

            @Override
            public void onFailure(Call<TweetResponse> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "Problemas con el internet, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void unlike(int idTweet) {
        Call<TweetResponse> call = tweetService.unlike(idTweet);

        call.enqueue(new Callback<TweetResponse>() {
            @Override
            public void onResponse(Call<TweetResponse> call, Response<TweetResponse> response) {
                if(response.isSuccessful()) {
                    /* desmarco un tweet favorito */
                    allTweets.setValue(getUpdatedListTweet(allTweets.getValue(), response.body()));

                    /* elimino un tweet a la lista de favoritos */
                    favTweets.setValue(removeTweetFromList(favTweets.getValue(), response.body()));

                    Log.d(TAG, "unlike tweet");
                } else {
                    try {
                        ErrorResponse error = ConvertToGson.toError(response.errorBody().string());
                        Toast.makeText(MyApplication.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TweetResponse> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "Problemas con el internet, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delete(final int idTweet) {
        Call<GenericResponse> call = tweetService.delete(idTweet);
        call.enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if(response.isSuccessful()) {
                    TweetResponse deletedTweet = new TweetResponse(idTweet);
                    /* elimino el tweet de la lista principal */
                    allTweets.setValue(removeTweetFromList(allTweets.getValue(), deletedTweet));
                    /* elimino el tweet de la lista de favoritos */
                    favTweets.setValue(removeTweetFromList(favTweets.getValue(), deletedTweet));
                } else {
                    try {
                        ErrorResponse error = ConvertToGson.toError(response.errorBody().string());
                        Toast.makeText(MyApplication.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                Toast.makeText(MyApplication.getContext(), "Problemas con el internet, intenta de nuevo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * reemplazo el tweet de una lista, segun id_tweet
     */
    private List<TweetResponse> getUpdatedListTweet(List<TweetResponse> listTweet, TweetResponse tweetResponse) {

        if(listTweet == null)
            return new ArrayList<>();

        int totalTweets = listTweet.size();
        for(int i=0; i<totalTweets; i++) {
            if(listTweet.get(i).getIdTweet() == tweetResponse.getIdTweet()) {
                listTweet.set(i, tweetResponse);
                break;
            }
        }

        return listTweet;
    }

    /**
     *
     * elimino un tweet de la lista, segun id_tweet
     */
    private List<TweetResponse> removeTweetFromList(List<TweetResponse> listTweet, TweetResponse tweetResponse)  {
        if(listTweet == null)
            return new ArrayList<>();

        int totalTweets = listTweet.size();
        for(int i=0; i<totalTweets; i++) {
            if(listTweet.get(i).getIdTweet() == tweetResponse.getIdTweet()) {
                listTweet.remove(i);
                break;
            }
        }

        return listTweet;
    }

    /**
     *
     * obtengo la lista de tweets favoritos, a partir de la lista principal
     */
    private List<TweetResponse> getFavoriteTweetsFromList(List<TweetResponse> listTweet) {
        List<TweetResponse> listFavTweets = new ArrayList<>();
        int totalTweets = listTweet.size();

        for(int i=0; i<totalTweets; i++) {
            if(listTweet.get(i).getMyLike() > 0) {
                listFavTweets.add(listTweet.get(i));
            }
        }

        return listFavTweets;
    }
}
