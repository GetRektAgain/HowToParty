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
    private Button btnTeilnehmen;
    private DatabaseHelper db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_overview);

        txtId = (TextView) findViewById(R.id.txtId);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnTeilnehmen = (Button) findViewById(R.id.btnTeilnehmen);

        db = new DatabaseHelper(this, "partys");

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        userId = intent.getIntExtra("userId", 0);

        this.checkAttendant(userId, id);
        boolean isOrganizer = this.checkOrganizer(userId, id);

        Cursor result = db.getParty(id);
        result.moveToFirst();
        double posLat = result.getDouble(1);
        double posLng = result.getDouble(2);
        String musikRichtung = result.getString(3);

        txtId.setText(musikRichtung);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", userId);
                Intent intent = new Intent(PartyOverviewActivity.this, MapsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnTeilnehmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addAttendent(userId, id);
                checkAttendant(userId, id);
            }
        });
    }

    private void checkAttendant(int userId, int partyId) {
        if (db.isAttendent(userId, partyId)) {
            btnTeilnehmen.setClickable(false);
            btnTeilnehmen.setText("Teilgenommen");
            btnTeilnehmen.setBackgroundTintList(btnTeilnehmen.getResources().getColorStateList(R.color.grey, this.getTheme()));
        }
    }

    private boolean checkOrganizer(int userId, int partyId) {
        boolean check = db.isOrganizer(userId, partyId);
        if (check) {
            btnTeilnehmen.setText("Bearbeiten");
            btnTeilnehmen.setBackgroundTintList(btnTeilnehmen.getResources().getColorStateList(R.color.blue, this.getTheme()));
        }
        return check;
    }
}