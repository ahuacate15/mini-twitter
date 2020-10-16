package com.carlos.minitwitter.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TweetResponse {

    @Expose
    @SerializedName("id_tweet")
    private int idTweet;

    @Expose
    @SerializedName("created_date")
    private Date createdDate;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("id_user")
    private int idUser;

    @Expose
    @SerializedName("count_likes")
    private int countLikes;

    @Expose
    @SerializedName("my_like")
    private int myLike;

    @Expose
    @SerializedName("user_name")
    private String userName;

    public TweetResponse() {}

    public TweetResponse(int idTweet) {
        this.idTweet = idTweet;
    }

    public TweetResponse(TweetResponse tweetResponse) {
        this.idTweet = tweetResponse.getIdTweet();
        this.createdDate = tweetResponse.getCreatedDate();
        this.message = tweetResponse.getMessage();
        this.countLikes = tweetResponse.getCountLikes();
        this.userName = tweetResponse.getUserName();
        this.myLike = tweetResponse.myLike;
    }

    public int getIdTweet() {
        return idTweet;
    }

    public void setIdTweet(int idTweet) {
        this.idTweet = idTweet;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCountLikes() {
        return countLikes;
    }

    public void setCountLikes(int countLikes) {
        this.countLikes = countLikes;
    }

    public int getMyLike() {
        return myLike;
    }

    public void setMyLike(int myLike) {
        this.myLike = myLike;
    }
}
