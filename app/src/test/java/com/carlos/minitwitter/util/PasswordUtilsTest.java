package com.carlos.minitwitter.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PasswordUtilsTest {

    @Test
    public void validateChangePassword() {
        PasswordUtils password = new PasswordUtils();
        password.setOriginalPassword("12345");
        password.setNewPassword("54321");
        password.setConfirmPassword("54321");
        assertEquals(PasswordUtils.OK, password.validateChangePassword());
    }

    @Test
    public void validateChangePassword_emptyValues() {
        PasswordUtils password = new PasswordUtils();
        assertEquals(PasswordUtils.ORIGINAL_PASSWORD_EMPTY, password.validateChangePassword());

        password.setOriginalPassword("12345");
        assertEquals(PasswordUtils.NEW_PASSWORD_EMPTY, password.validateChangePassword());

        password.setNewPassword("54321");
        assertEquals(PasswordUtils.CONFIRM_PASSWORD_EMPTY, password.validateChangePassword());
    }

    @Test
    public void validateChangePassword_nullValues() {
        PasswordUtils password = new PasswordUtils();
        password.setOriginalPassword(null);
        assertEquals(PasswordUtils.ORIGINAL_PASSWORD_EMPTY, password.validateChangePassword());

        password.setOriginalPassword("12345");
        password.setNewPassword(null);
        assertEquals(PasswordUtils.NEW_PASSWORD_EMPTY, password.validateChangePassword());

        password.setOriginalPassword("12345");
        password.setNewPassword("54321");
        password.setConfirmPassword(null);
        assertEquals(PasswordUtils.CONFIRM_PASSWORD_EMPTY, password.validateChangePassword());
    }

    @Test
    public void validateChangePassword_notMatchNewPassword() {
        PasswordUtils password = new PasswordUtils();
        password.setOriginalPassword("12345");
        password.setNewPassword("54321");
        password.setConfirmPassword("00000");
        assertEquals(PasswordUtils.NOT_MATCH, password.validateChangePassword());
    }

    @Test
    public void isValidPassword_toSmall() {
        PasswordUtils password = new PasswordUtils();
        assertEquals(
                PasswordUtils.PASSWORD_TO_SMALL,
                password.isValidPassword("1234")
        );
    }
}
