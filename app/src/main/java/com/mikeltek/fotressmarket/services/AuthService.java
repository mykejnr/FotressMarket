package com.mikeltek.fotressmarket.services;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.room.rxjava3.EmptyResultSetException;
import com.mikeltek.fotressmarket.models.AppDatabase;
import com.mikeltek.fotressmarket.models.User;
import com.mikeltek.fotressmarket.models.UserDao;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class AuthService {

    private final UserDao userDao;

    /**
     * Contains a pointer to the logged in user.
     * Points to a null object if no user is logged in
     */
    public final BehaviorSubject<AuthUser> authUser = BehaviorSubject
            .createDefault(new AuthUser());

    private AuthService(Context appContext) {
        userDao = AppDatabase.getInstance(appContext).userDao();
    }

    @Nullable
    private static volatile AuthService authService;
    public static AuthService getInstance(Context appContext) {
        if (authService == null) synchronized (AuthService.class) {
            if (authService == null)
                authService  = new AuthService(appContext);
        }
        return authService;
    }

    public Single<Boolean> createUserAsync(User user, String password) {
        user.passwordHash = makePasswordHash(password);
        return userDao.getByEmail(user.email)
            .flatMap(u -> Single.<Boolean>error(new RuntimeException(
                    "Email (" + u.email + "already exist.")))
            .onErrorResumeNext(err -> {
                if (err instanceof EmptyResultSetException)
                    return userDao.insert(user).andThen(Single.just(true));
                return Single.error(err);
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<User> loginAsync(String email, String rawPassword) {
        return userDao.getByEmail(email)
            .map(u -> {
                if ( checkPassword(u, rawPassword) ) return u;
                throw new Exception("Invalid password.");
            })
            .onErrorResumeNext(err -> {
                if (err instanceof EmptyResultSetException)
                    return Single.error( new Exception("Email not found."));
                return Single.error( err );
            })
           .observeOn(AndroidSchedulers.mainThread())
           .doAfterSuccess(this::initializeAuthUser);
    }

    private void initializeAuthUser(User user) {
        var d = this.userDao.getForever(user.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(u -> this.authUser.onNext(new AuthUser(u)));
    }

    private String makePasswordHash(String rawPassword) {
        return rawPassword; // Todo: implement password hashing
    }

    private boolean checkPassword(User user, String rawPassword) {
        return user.passwordHash.equals(rawPassword); // Todo: Check against password has
    }
}
