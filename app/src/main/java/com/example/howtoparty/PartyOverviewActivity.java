package com.example.howtoparty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PartyOverviewActivity extends AppCompatActivity {

    private TextView txtId;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_overview);

        txtId = (TextView) findViewById(R.id.txtId);
        btnBack = (Button) findViewById(R.id.btnBack);

        DatabaseHelper db = new DatabaseHelper(this, "partys");

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        Cursor result = db.getParty(id);
        result.moveToFirst();
        double posLat = result.getDouble(1);
        double posLng = result.getDouble(2);
        String musikRichtung = result.getString(3);

        txtId.setText(musikRichtung);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PartyOverviewActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}