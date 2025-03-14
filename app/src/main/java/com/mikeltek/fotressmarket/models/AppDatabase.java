package com.mikeltek.fotressmarket.models;

import android.content.Context;
import android.util.Log;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.rxjava3.EmptyResultSetException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Database(
    exportSchema = true,
    version = 2,
    entities = {
        User.class,
        Product.class
    },
    autoMigrations = {
        @AutoMigration(from = 1, to = 2)
    }
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ProductDao productDao();

    private static AppDatabase db;
    public static AppDatabase getInstance(Context appContext) {
        if (db != null) {
            return db;
        }
        synchronized (AppDatabase.class) {
            if (db == null) {
                db = Room.databaseBuilder(appContext, AppDatabase.class, "fortress.db")
                        .build();
                Populate(db);
            }
        }
        return db;
    }

    /**
     * This api points to an open source fake stores api project. We fetch
     * a list of 'fake' products from this api, first time our app runs, to
     * populate our database. Subsequent app startups will skip the product
     * fetching if the database has at least one product.
     * Visit <a href="https://fakestoreapi.com/">Fake Store Api Page</a> to
     * learn more
     */
    static final String FAKE_API =  "https://fakestoreapi.com/products";

    public static void Populate(AppDatabase db) {
        var TAG = "Populate_Products";
        Log.d(TAG, "Initializing product table population.");

        var b = db.productDao().getAny()
                .subscribeOn(Schedulers.io()) // we start on the rx java's io thread

                // If there is at least one product. send a message directly to
                // the ".subscribe()" consumer
                .map(anyProduct ->
                    "Found at least one (1) product in product table. Skipping table seeding."
                )

                // An Unhappy path, or not...
                .onErrorResumeNext(err -> {
                    // If there is no record in the product db table, we should receive
                    // the error here as an "EmptyResultSetException".
                    // We therefore handle it, by fetching the products from the remote
                    // api and inserting it into the database
                    if (err instanceof EmptyResultSetException) {
                        Log.d(TAG, "Fetching products from remote api.");
                        var products = fetchProducts();
                        // After fetching the products, insert them into the db, and return
                        // with a message on how many rows we inserted
                        return db.productDao().insertAll(products)
                                .toSingle(() -> "Populated db with " + products.size() + " products.");
                    }
                    // If we are here, the error received, is not an "EmptyResultSetException".
                    // this was something else, send the error message to ".subscribe()" for
                    // it to be logged
                    return Single.just( err.getMessage() );
                })

                .observeOn(AndroidSchedulers.mainThread()) // we are back on the main thread

                // A Very Unhappy Path
                // At this point, we've done all we could, but some random error
                // still occurred. We just log the error and complete our journey
                .onErrorComplete(err -> {
                    Log.d(TAG, String.format("Error - %s", err.getMessage()));
                    return true;
                })

                // The happy path.. or not.
                // Just log messages that are routed here from other paths.
                .subscribe(msg -> Log.d(TAG, msg) );
    }

    private static List<Product> fetchProducts() throws JSONException {
        var jsonArray= getJsonFormUrl();
        List<Product> products = new ArrayList<>(jsonArray.length());

        JSONObject po;

        for (int i = 0; i < jsonArray.length(); i++) {
            po = jsonArray.getJSONObject(i);
            var product = new Product();
            product.title = po.getString("title");
            product.price = (float) po.getDouble("price");
            product.description = po.getString("description");
            product.category = po.getString("category");
            product.image = po.getString("image");

            products.add(product);
        }

        return products;
    }

    private static JSONArray getJsonFormUrl() {
        try {
            var u = new URL(FAKE_API);
            try (InputStream is = u.openStream()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String ln;
                while ((ln = br.readLine()) != null) {
                    sb.append(ln);
                }
                br.close();
                return new JSONArray(sb.toString());
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
