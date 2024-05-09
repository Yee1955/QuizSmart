package com.example.lostfound;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lostfound.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ManagerDB managerDB;
    List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        managerDB = new ManagerDB(this);
        posts = managerDB.getPosts();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng lastLocation = new LatLng(0, 0); // Default location
        boolean isMarkerSet = false;

        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocationName(post.getLocation(), 1);
                if (addresses == null || addresses.isEmpty()) {
                    Log.e("Geocoder", "No addresses found for " + post.getLocation());
                    continue; // Skip this iteration if no addresses found
                }

                Address location = addresses.get(0);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(post.getName()));
                lastLocation = latLng; // Keep track of the last location
                isMarkerSet = true;
            } catch (IOException e) {
                Log.e("Geocoder", "Geocoder failed for " + post.getName() + ": " + e.getMessage());
            } catch (IllegalArgumentException e) {
                Log.e("Geocoder", "Invalid location name input: " + post.getName());
            }
        }

        if (isMarkerSet) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 10)); // Move camera to last marker
        } else {
            Log.d("MapsActivity", "No markers set.");
        }
    }

}