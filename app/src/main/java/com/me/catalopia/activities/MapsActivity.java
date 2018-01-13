package com.me.catalopia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.me.catalopia.R;
import com.me.catalopia.models.Location;
import com.me.catalopia.models.Product;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    private static final int PRODUCT_DATA_REQUEST_CODE = 6,
            PRODUCT_EDIT_REQUEST_CODE = 7;
    private static final String PRODUCT_EXTRA = "PRODUCT_EXTRA";
    private static LinkedList<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markers = new LinkedList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("products").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                product.setId(dataSnapshot.getKey());
                drawProductOnMap(product);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                updateProductOnMap(product);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                removeProductFromMap(product);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        mDatabase.child("products").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Map<String, Object> td = (HashMap<String,Object>) dataSnapshot.getValue();
//
//                Log.d("", "");
//                //List<Object> values = td.values();
//
//                //notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(MapsActivity.this, AddEditProductActivity.class);
                Location location = new Location(latLng.latitude, latLng.longitude);
                intent.putExtra("LAT_LNG", location);
                startActivityForResult(intent, PRODUCT_DATA_REQUEST_CODE);
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Product product = (Product) marker.getTag();
                if (product != null) {
                    Intent intent = new Intent(MapsActivity.this, AddEditProductActivity.class);
                    intent.putExtra("PRODUCT_DATA", product);
                    startActivityForResult(intent, PRODUCT_EDIT_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PRODUCT_DATA_REQUEST_CODE) {
                Product product = (Product) data.getSerializableExtra(PRODUCT_EXTRA);
                createNewProduct(product);
            } else if (requestCode == PRODUCT_EDIT_REQUEST_CODE) {
                Product product = (Product) data.getSerializableExtra(PRODUCT_EXTRA);
                updateProduct(product);
            }
        }
    }

    private void updateProduct(Product product) {
        updateProductOnMap(product);

        Map<String, Object> keyVal = new HashMap<>();
        keyVal.put(product.getId(), product);
        mDatabase.child("products").updateChildren(keyVal);
    }

    private void createNewProduct(Product product) {
        drawProductOnMap(product);

        String productId = mDatabase.child("products").push().getKey();
        product.setId(productId);
        mDatabase.child("products").child(productId).setValue(product);
    }

    private void drawProductOnMap(Product product) {
        if (mMap!= null) {
            LatLng latLng = new LatLng(product.getLocation().getLat(), product.getLocation().getLng());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(product.getName());
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(product);
            markers.add(marker);
        }
    }

    private void updateProductOnMap(Product product) {
        if (product != null) {
            for (int i = 0; i < markers.size(); i++) {
                Product productTag = (Product) markers.get(i).getTag();
                if (productTag.equals(product)) {
                    markers.get(i).setTag(product);
                    markers.get(i).setTitle(product.getName());
                    break;
                }
            }
        }
    }

    private void removeProductFromMap(Product product) {
        if (product != null) {
            for (int i = 0; i < markers.size(); i++) {
                Product productTag = (Product) markers.get(i).getTag();
                if (productTag.equals(product)) {
                    markers.get(i).remove();
                    break;
                }
            }
        }
    }
}
