package com.mikeltek.fotressmarket.services;

import androidx.annotation.Nullable;

import com.mikeltek.fotressmarket.models.User;

public class AuthUser {

    private User user;

    public AuthUser(User user) {
        this.user = user;
    }

    public AuthUser() {}

    @Nullable
    public User get() {
        return user;
    }

    public boolean isEmpty() {
        return user == null;
    }
}
