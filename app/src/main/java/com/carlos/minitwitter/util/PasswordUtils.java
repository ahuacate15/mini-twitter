package com.carlos.minitwitter.util;

public class PasswordUtils {

    private String originalPassword;
    private String newPassword;
    private String confirmPassword;

    public static final int OK = 0;
    public static final int ORIGINAL_PASSWORD_EMPTY = 1;
    public static final int NEW_PASSWORD_EMPTY = 2;
    public static final int CONFIRM_PASSWORD_EMPTY = 3;
    public static final int NOT_MATCH = 4;

    public static final int PASSWORD_TO_SMALL = 5;

    public PasswordUtils() {
        this.originalPassword = "";
        this.newPassword = "";
        this.confirmPassword = "";
    }

    public int validateChangePassword() {
        if(this.originalPassword.isEmpty()) {
            return ORIGINAL_PASSWORD_EMPTY;
        }

        if(this.newPassword.isEmpty()) {
            return NEW_PASSWORD_EMPTY;
        }

        if(this.confirmPassword.isEmpty()) {
            return CONFIRM_PASSWORD_EMPTY;
        }

        if(!this.newPassword.equals(this.confirmPassword)) {
            return NOT_MATCH;
        }

        return OK;
    }

    public int isValidPassword(String password) {

        //minimo 5 caracteres
        if(password == null || password.length() < 5) {
            return PASSWORD_TO_SMALL;
        }

        return OK;
    }

    public void setOriginalPassword(String originalPassword) {
        this.originalPassword = originalPassword == null ? "" : originalPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword == null ? "" : newPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword == null ? "" : confirmPassword;
    }
}
