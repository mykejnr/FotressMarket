package com.mikeltek.fotressmarket.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    indices = {
        @Index(value = "email", unique = true)
    }
)
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "surname")
    public String surname;
    @ColumnInfo(name = "other_names")
    public String otherNames;
    public String email;
    @ColumnInfo(name = "password_hash")
    public String passwordHash;
}
