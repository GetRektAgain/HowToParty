package com.example.howtoparty;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.howtoparty.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapsActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback{

    private GoogleMap mMap;

    private ActivityMapsBinding binding;
    private LocationManager locationManager;
    private static int first;
    private static Marker registrationMarker;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 100;
    private int userId;
    boolean Success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_maps);
        askPermission();
        checkPermission();
        initGMaps();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
    }

    // Initialize GoogleMaps
    private void initGMaps(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
    }

    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DatabaseHelper db = new DatabaseHelper();

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", 0);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        first = 0;
        ResultSet partys = db.getPartys();

        try {
            Success = partys.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        while(Success) {
        try {
            int id = partys.getInt("id");
            double lat = partys.getDouble("latitude");
            double lng = partys.getDouble("longitude");
            String veranstaltungsart = partys.getString("veranstaltungs_art");
            String veranstaltungsBeschreibung = partys.getString("veranstaltungs_beschreibung");
            String blob = partys.getString("image");


            Bitmap bitmap = StringToBitMap(blob);

            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
            LatLng partyMarker = new LatLng(lat, lng);
            MarkerOptions markerOptions;


            if (icon == null )
            {
                 markerOptions = new MarkerOptions().position(partyMarker)
                        .title(veranstaltungsart);
            }
            else
            {
                 markerOptions = new MarkerOptions().position(partyMarker)
                        .title(veranstaltungsart)
                        .icon(icon);
            }

            mMap.addMarker(markerOptions).setTag(id);

            Success = partys.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                  @Override
                  public void onInfoWindowClick(Marker marker) {
                      Object tag = marker.getTag();
                      Bundle bundle = new Bundle();
                      assert tag != null;
                      bundle.putInt("id", ((Integer) tag).intValue());
                      bundle.putInt("userId", userId);
                      Intent intent = new Intent(MapsActivity.this, PartyOverviewActivity.class);
                      intent.putExtras(bundle);
                      startActivity(intent);
                  }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
                public void onMapLongClick(LatLng point) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("userId", userId);
                        bundle.putParcelable("position", point);
                        Intent intent = new Intent(MapsActivity.this, RegisterPartyActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
             }
        });
    }


    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}