package com.mikeltek.fotressmarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.mikeltek.fotressmarket.models.AppDatabase;
import com.mikeltek.fotressmarket.models.Product;
import com.mikeltek.fotressmarket.models.ProductDao;
import com.mikeltek.fotressmarket.views.Login;
import com.mikeltek.fotressmarket.views.Profile;
import com.mikeltek.fotressmarket.views.Register;
import com.mikeltek.fotressmarket.views.components.CatalogueItem;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    ProductDao productDao;
    GridLayout productContainer;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productDao = AppDatabase.getInstance(getApplicationContext()).productDao();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void init() {
        findViewById(R.id.appBar_BtnUser).setOnClickListener(v -> gotoProfile());
        productContainer = findViewById(R.id.mainActivity_ProductsContainer);
        fetchProducts();
    }

    private void fetchProducts() {
        var d = productDao.getAllForever()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorComplete(err -> {
                showFetchError(err.getMessage());
                return true;
            })
            .subscribe(this::renderProducts);

       compositeDisposable.add(d);
    }

    private void renderProducts(List<Product> products) {
        productContainer.setColumnCount(2);
        int rowCount = (int)Math.ceil((double) products.size() / 2);
        Log.d("MainActivity", "Calculated Row count: " + rowCount);
        productContainer.setRowCount(rowCount);
        var row = -1;
        var col = 1;
        for (var product : products) {
            col = col == 1 ? 0 : 1;
            row = col == 0 ? row + 1 : row;
            createChildItem(product, row, col);
        }
    }

    private void createChildItem(Product product, int row, int col) {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        var rowSpec = GridLayout.spec(row, 1, 1);
        var columnSpec = GridLayout.spec(col, 1, 1);
        params.rowSpec = rowSpec;
        params.columnSpec = columnSpec;

        var view = (CatalogueItem) LayoutInflater.from(this)
                .inflate(R.layout.catalog_item_template, null);

        productContainer.addView(view, params);
        view.updateView(product);
    }

    private void showFetchError(String error) {
        Log.d("Fetch Error", error);
    }

    private void gotoProfile() {
        startActivity(new Intent(MainActivity.this, Profile.class));
    }
}