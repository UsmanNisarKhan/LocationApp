package com.example.locationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        preparegoogleservices();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        showlocation();
    }

    private void requestpermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode == 1000)
        {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                showlocation();
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else
            {
                requestpermission();
                Toast.makeText(this,"Permission Required",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showlocation()
    {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mMap.setMyLocationEnabled(true);

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    Location location = task.getResult();
                    if(location != null)
                    {
                        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Current Location"));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng,16.0f);
                        mMap.moveCamera(cameraUpdate);

                    }

                    else
                    {
                        Toast.makeText(MapsActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else
        {
                requestpermission();
        }
    }

    private void preparegoogleservices()
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }
}