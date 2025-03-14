package com.mikeltek.fotressmarket.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public float price;
    public String description;
    public String category;
    public String image;
}
