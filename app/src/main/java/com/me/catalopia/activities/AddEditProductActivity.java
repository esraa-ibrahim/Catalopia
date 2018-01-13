package com.me.catalopia.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.me.catalopia.R;
import com.me.catalopia.connection.CatalopiaApiManager;
import com.me.catalopia.models.Location;
import com.me.catalopia.models.Product;
import com.me.catalopia.models.Store;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditProductActivity extends AppCompatActivity {

    private static final String PRODUCT_EXTRA = "PRODUCT_EXTRA";
    private DatabaseReference mDatabase;
    private Location latlng = null;
    private Product product;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        final EditText etProductName = (EditText) findViewById(R.id.et_product_name);
        final EditText etProductDesc = (EditText) findViewById(R.id.et_product_desc);
        final EditText etPrice = (EditText) findViewById(R.id.et_product_price);
        final Spinner spStores = (Spinner) findViewById(R.id.sp_stores);
        Button btnSave = (Button) findViewById(R.id.btn_save);

        CatalopiaApiManager apiManager = new CatalopiaApiManager();
        pd = new ProgressDialog(AddEditProductActivity.this);
        pd.show();
        apiManager.getStoresCall().enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                pd.hide();
                ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(AddEditProductActivity.this,
                        android.R.layout.simple_spinner_item, response.body().toArray());
                spStores.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                pd.hide();
                Log.d("", t.getMessage());
            }
        });

        if (getIntent()!= null) {
            if (getIntent().hasExtra("LAT_LNG")) {
                latlng = (Location) getIntent().getSerializableExtra("LAT_LNG");
            } else if (getIntent().hasExtra("PRODUCT_DATA")) {
                product = (Product) getIntent().getSerializableExtra("PRODUCT_DATA");
            }
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (product != null) {
            etProductName.setText(product.getName());
            etProductDesc.setText(product.getDescription());
            etPrice.setText(product.getPrice().toString());
//
//            etProductName.setEnabled(false);
//            etProductDesc.setEnabled(false);
//            etPrice.setEnabled(false);
//            btnSave.setEnabled(false);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product p = null;
                if (product != null) {
                    // Edit mode
                    product.setName(etProductName.getText().toString());
                    product.setDescription(etProductDesc.getText().toString());
                    product.setPrice(Double.valueOf(etPrice.getText().toString()));
                    p = product;
                } else {
                    p = new Product(etProductName.getText().toString(),
                            etProductDesc.getText().toString(),
                            Double.valueOf(etPrice.getText().toString()), latlng);
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra(PRODUCT_EXTRA, p);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
