package com.example.howtoparty;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.howtoparty.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static int first;
    private static Marker registrationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        DatabaseHelper db = new DatabaseHelper(this, "partys");
        // Add a marker in Sydney and move the camera
        LatLng home = new LatLng(48.458670499547736, 11.64852898567915);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

        first = 0;

        Cursor partys = db.getPartys();
        int countPartys = partys.getCount();
        partys.moveToFirst();
        for (int i = 0; i < countPartys; i++) {
            int id = partys.getInt(0);
            double lat = partys.getDouble(1);
            double lng = partys.getDouble(2);
            String mr = partys.getString(3);
            LatLng partyMarker = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(partyMarker).title(mr)).setTag(id);
            partys.moveToNext();
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setRegistrationMarker(latLng);
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Object tag = marker.getTag();
                if (tag.equals(0)) {
                    LatLng position = marker.getPosition();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("position", position);
                    Intent intent = new Intent(MapsActivity.this, RegisterPartyActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MapsActivity.this, PartyOverviewActivity.class);
                    int id = ((Integer) tag).intValue();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void setRegistrationMarker(LatLng latLng) {
        if (first == 0) {
            registrationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Party").snippet("Hinzufügen"));
            registrationMarker.setTag(0);
            first++;
        } else {
            registrationMarker.setPosition(latLng);
        }
    }
}