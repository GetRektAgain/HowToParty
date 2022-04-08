package com.example.howtoparty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PartyOverviewActivity extends AppCompatActivity {

    private TextView txtId;
    private Button btnBack;
    private Button btnTeilnehmen;
    private TextView txtDesc;
    private DatabaseHelper db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_overview);

        txtId = (TextView) findViewById(R.id.txtId);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnTeilnehmen = (Button) findViewById(R.id.btnTeilnehmen);
        txtDesc = (TextView) findViewById(R.id.textView_beschreibung);

        db = new DatabaseHelper();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        userId = intent.getIntExtra("userId",0);


        try {
            checkAttendant(userId, id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            checkOrganizer(userId, id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        ResultSet result = db.getParty(id);

        try {
            result.first();
            txtId.setText(result.getString("veranstaltungs_art"));
            txtDesc.setText(result.getString("veranstaltungs_beschreibung"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



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
            btnTeilnehmen.setClickable(false);
            btnTeilnehmen.setText("Bearbeiten");
            btnTeilnehmen.setBackgroundTintList(btnTeilnehmen.getResources().getColorStateList(R.color.blue, this.getTheme()));
        }
        return check;
    }
}