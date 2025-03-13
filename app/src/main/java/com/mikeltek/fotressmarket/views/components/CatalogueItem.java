package com.mikeltek.fotressmarket.views.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.mikeltek.fotressmarket.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CatalogueItem extends LinearLayout {

    String imageSrc;
    String productTitle;
    float productPrice;

    TextView txtTitle;
    TextView txtPrice;
    TextView txtImageCaption;
    ImageView imgProductImage;


    public CatalogueItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.catalogue_item, this);
        try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CatalogueItem)) {
            imageSrc = a.getString(R.styleable.CatalogueItem_productImageSrc);
            productTitle = a.getString(R.styleable.CatalogueItem_productTitle);
            productPrice = a.getFloat(R.styleable.CatalogueItem_productPrice, 0);
        }

        initComponent();
    }

    private void initComponent() {
        txtTitle = findViewById(R.id.catalogItem_TextProductTitle);
        txtPrice = findViewById(R.id.catalogItem_TextPrice);
        txtImageCaption = findViewById(R.id.catalogItem_TextImageCaption);
        imgProductImage  = findViewById(R.id.catalogItem_Image);

        txtTitle.setText(productTitle);
        txtPrice.setText(String.format("Gh¢%s", productPrice));
        renderImage();
    }

    private void renderImage() {
        renderImageCaption("Loading image . . .");
        var d  = Single.fromCallable(this::fetchImage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorComplete(err -> {
                renderImageCaption(err.getMessage());
                return true;
            })
            .subscribe(this::renderImageContent);
    }

    private void renderImageCaption(String msg) {
        txtImageCaption.setVisibility(VISIBLE);
        txtImageCaption.setText(msg);
    }

    private void renderImageContent(Bitmap bitmap) {
        imgProductImage.setImageBitmap(bitmap);
        txtImageCaption.setVisibility(INVISIBLE);
    }

    private Bitmap fetchImage() {
        try {
            var url = new URL(imageSrc);
            try (InputStream is = url.openStream()) {
                return BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}