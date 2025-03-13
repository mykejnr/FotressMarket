package com.mikeltek.fotressmarket.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;
import kotlin.NotImplementedError;


//Todo: Save cache to disk, and load on startup

public class AppImageRepository {

    private final Map<String, Bitmap> cache;
    private AppImageRepository() {
        this.cache = Collections.synchronizedMap(new HashMap<String, Bitmap>());
    }

    private static final class InstanceHolder {
        static final AppImageRepository instance = new AppImageRepository();
    }

    public static AppImageRepository getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * Get image from the local disk
     * @param path path to image file
     * @return a bitmap image
     */
    public Single<Bitmap> getLocal(String path) {
        throw new NotImplementedError();
    }

    /**
     * Fetch an image from the provided url
     * @param url url point ot the image
     * @return a bitmap image
     */
    public Single<Bitmap> getRemote(String url) {
        Bitmap bm = cache.get(url);
        if (bm != null) {
            Log.d("Image Repo Cache", String.format("Cache hit: %s", url));
            return Single.just(bm);
        }

        return Single.fromCallable(() -> fetchImage(url))
            .doAfterSuccess(b -> {
                cache.put(url, b);
            });

        //Todo: there is still a cache mis if multiple requests
        // come in for the same image. This is because the first request
        // hasn't returned from it request in other to cache the result
        // Solution: is to cache an Observable/Subject instead, so that
        // subsequent requests can resolve from the observable
    }

    private Bitmap fetchImage(String src) {
        try {
            var url = new URL(src);
            try (InputStream is = url.openStream()) {
                return BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
