package com.example.howtoparty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

public class RegisterPartyActivity extends AppCompatActivity {

    private EditText textMusikrichtung;
    private Button btnRegistrieren;
    private Button btnAbbrechen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_party);

        DatabaseHelper db = new DatabaseHelper(this, "partys");

        textMusikrichtung = (EditText) findViewById(R.id.textMusikrichtung);
        btnRegistrieren = (Button) findViewById(R.id.btnRegistrieren);
        btnAbbrechen = (Button) findViewById(R.id.btnAbbrechen);

        Intent intent = getIntent();
        LatLng position = intent.getParcelableExtra("position");

        btnAbbrechen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPartyActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        btnRegistrieren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addParty(position, textMusikrichtung.getText().toString());
                Intent intent = new Intent(RegisterPartyActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}