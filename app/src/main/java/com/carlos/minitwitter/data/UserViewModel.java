package com.carlos.minitwitter.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.carlos.minitwitter.retrofit.response.UserResponse;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;
    private MutableLiveData<UserResponse> userProfile;
    private MutableLiveData<String> errorMessageToUpdate;
    private MutableLiveData<String> photoProfile;

    public UserViewModel() {
        userRepository = new UserRepository();
    }

    public MutableLiveData<UserResponse> fetchProfile() {
        userProfile = userRepository.fetchProfile();
        return userProfile;
    }

    public MutableLiveData<UserResponse> updateProfile(String field, String value) {
        userProfile = userRepository.updateProfile(field, value);
        return userProfile;
    }

    public MutableLiveData<String> uploadPhoto(String path) {
        photoProfile = userRepository.uploadPhoto(path);
        return photoProfile;
    }

    public MutableLiveData<String> fetchPhotoProfile() {
        photoProfile = userRepository.getPhotoProfile();
        return photoProfile;
    }
}
