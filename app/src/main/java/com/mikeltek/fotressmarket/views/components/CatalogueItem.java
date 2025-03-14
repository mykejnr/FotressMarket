package com.mikeltek.fotressmarket.views.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.mikeltek.fotressmarket.R;
import com.mikeltek.fotressmarket.models.Product;
import com.mikeltek.fotressmarket.services.AppImageRepository;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CatalogueItem extends LinearLayout {

    String imageSrc = "";
    String productTitle = "$Set product price";
    float productPrice;

    TextView txtTitle;
    TextView txtPrice;
    TextView txtImageCaption;
    ImageView imgProductImage;

    AppImageRepository imageRepo = AppImageRepository.getInstance();

    public CatalogueItem(Context context) {
        this(context, null);
    }

    public CatalogueItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        inflate(context, R.layout.catalogue_item, this);

        txtTitle = findViewById(R.id.catalogItem_TextProductTitle);
        txtPrice = findViewById(R.id.catalogItem_TextPrice);
        txtImageCaption = findViewById(R.id.catalogItem_TextImageCaption);
        imgProductImage  = findViewById(R.id.catalogItem_Image);

        if (attrs != null) {
            try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CatalogueItem)) {
                imageSrc = a.getString(R.styleable.CatalogueItem_productImageSrc);
                productTitle = a.getString(R.styleable.CatalogueItem_productTitle);
                productPrice = a.getFloat(R.styleable.CatalogueItem_productPrice, 0);
            }

            updateView();
        }
    }

    public void updateView(Product product) {
        imageSrc = product.image;
        productTitle = product.title;
        productPrice = product.price;
        updateView();
    }

    private void updateView() {
        txtTitle.setText(productTitle);
        txtPrice.setText(String.format("Gh¢%s", productPrice));
        renderImage();
    }

    private void renderImage() {
        if (imageSrc == null || imageSrc.isBlank()) {
            Log.d("CatalogueItem", "Attempting to render with a null/blank image src. TERMINATED.");
            return;
        }
        renderImageCaption("Loading image . . .");

        var d = imageRepo.getRemote(this.imageSrc)
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
}