package com.carlos.minitwitter.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UserResponse {

    @Expose
    @SerializedName("id_user")
    private int idUser;

    @Expose
    @SerializedName("user_name")
    private String userName;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("lastname")
    private String lastname;

    @Expose
    @SerializedName("photo_url")
    private String photoUrl;

    @Expose
    @SerializedName("genre")
    private String genre;

    @Expose
    @SerializedName("role")
    private String role;

    @Expose
    @SerializedName("created_date")
    private Date createdDate;

    private String message;

    public UserResponse() {}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
}
