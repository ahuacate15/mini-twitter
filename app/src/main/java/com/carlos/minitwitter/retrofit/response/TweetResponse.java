package com.carlos.minitwitter.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TweetResponse {

    @Expose
    @SerializedName("id")
    private int id;

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
    @SerializedName("user_name")
    private String userName;

    public TweetResponse() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
