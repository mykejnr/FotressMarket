package com.mikeltek.fotressmarket.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM product")
    Single<List<Product>> getAll();

    @Query("SELECT * FROM product LIMIT 1")
    Single<Product> getAny();

    @Query("SELECT * FROM product")
    Flowable<List<Product>> getAllForever();

    @Query("SELECT * FROM product WHERE id = :productId")
    Single<Product> get(int productId);

    @Query("SELECT * FROM product WHERE id = :productId")
    Flowable<Product> getForever(int productId);

    @Insert
    Completable insert(Product product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<Product> products);

    @Delete
    Completable delete(Product product);

    @Update
    Completable update(Product product);
}
