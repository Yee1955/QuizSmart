package com.example.lostfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap goMap;
    private MapActivityBinding mapActivityBinding;
    AdvertDB db;
    List<Advert> adverts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        db = new AdvertDB(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        goMap = googleMap;

        for (int i = 0; i < adverts.size(); i++) {

        }
    }
}