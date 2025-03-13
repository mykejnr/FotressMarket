package com.mikeltek.fotressmarket.services;

import android.content.Context;

import androidx.room.rxjava3.EmptyResultSetException;

import com.mikeltek.fotressmarket.models.AppDatabase;
import com.mikeltek.fotressmarket.models.User;
import com.mikeltek.fotressmarket.models.UserDao;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthService {

    private final UserDao userDao;
    public AuthService(Context appContext) {
        userDao = AppDatabase.getInstance(appContext).userDao();
    }

    public Completable createUserAsync(User user, String password) {
        user.passwordHash = makePasswordHash(password);
        return userDao.insert(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<User> loginAsync(String email, String rawPassword) {
        return userDao.getByEmail(email)
            .map(u -> {
                if ( checkPassword(u, rawPassword) )
                    return u;
                throw new Exception("Invalid password.");
            })
            .onErrorResumeNext(err -> {
                if (err instanceof EmptyResultSetException)
                    return Single.error( new Exception("Email not found."));
                return Single.error( err );
            })
           .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private String makePasswordHash(String rawPassword) {
        return rawPassword; // Todo: implement password hashing
    }

    private boolean checkPassword(User user, String rawPassword) {
        return user.passwordHash.equals(rawPassword); // Todo: Check against password has
    }
}
