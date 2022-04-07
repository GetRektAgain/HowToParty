package com.example.howtoparty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;

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

        db = new DatabaseHelper();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        userId = intent.getIntExtra("userId", 0);

        try {
            this.checkAttendant(userId, id);
            this.checkOrganizer(userId, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet result = db.getParty(id);
        String musikRichtung = "";
        try {
            result.next();
            musikRichtung = result.getString("veranstaltungs_art");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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
                try {
                    checkAttendant(userId, id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkAttendant(int userId, int partyId) throws SQLException {
        if (db.isAttendent(userId, partyId)) {
            btnTeilnehmen.setClickable(false);
            btnTeilnehmen.setText("Teilgenommen");
            btnTeilnehmen.setBackgroundTintList(btnTeilnehmen.getResources().getColorStateList(R.color.grey, this.getTheme()));
        }
    }

    private boolean checkOrganizer(int userId, int partyId) throws SQLException {
        boolean check = db.isOrganizer(userId, partyId);
        if (check) {
            btnTeilnehmen.setText("Bearbeiten");
            btnTeilnehmen.setBackgroundTintList(btnTeilnehmen.getResources().getColorStateList(R.color.blue, this.getTheme()));
        }
        return check;
    }
}