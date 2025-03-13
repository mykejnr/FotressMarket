package com.mikeltek.fotressmarket.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
    version = 1,
    entities = {
        User.class
    }
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static AppDatabase db;
    public static AppDatabase getInstance(Context appContext) {
        if (db == null)
            db = Room.databaseBuilder(appContext, AppDatabase.class, "fortress.db").build();
        return db;
    }
}
