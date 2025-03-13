package com.mikeltek.fotressmarket.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    Single<List<User>> getAll();

    @Query("SELECT * FROM user WHERE id = :userId")
    Single<User> get(int userId);

    @Query("SELECT * FROM user WHERE email = :email")
    Single<User> getByEmail(String email);

    @Insert
    Completable insert(User user);

    @Delete
    Completable delete(User user);

    @Update
    Completable update(User user);
}
