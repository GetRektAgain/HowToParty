package com.example.howtoparty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    public DatabaseHelper db;
    private Button btnRegister;
    private EditText regUsername;
    private EditText regVname;
    private EditText regNname;
    private EditText regEmail;
    private EditText regBddate;
    private EditText regPassword;
    private EditText regPasswordWdh;
    private TextView regErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this, "users");

        btnRegister = (Button) findViewById(R.id.btnSaveRegister);
        regUsername = (EditText) findViewById(R.id.regUsername);
        regVname = (EditText) findViewById(R.id.regVname);
        regNname = (EditText) findViewById(R.id.regNname);
        regEmail = (EditText) findViewById(R.id.regEmail);
        regBddate = (EditText) findViewById(R.id.regBddate);
        regPassword = (EditText) findViewById(R.id.regPassword);
        regPasswordWdh = (EditText) findViewById(R.id.regPasswordWdh);
        regErrorText = (TextView) findViewById(R.id.regErrorText);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewUser(
                        regUsername.getText().toString(),
                        regVname.getText().toString(),
                        regNname.getText().toString(),
                        regEmail.getText().toString(),
                        regBddate.getText().toString(),
                        regPassword.getText().toString(),
                        regPasswordWdh.getText().toString()
                );
            }
        });
    }

    private void saveNewUser(String username, String vname, String nname, String email, String dbdate, String password, String passwordWdh) {
        boolean canBeSaved = true;

        boolean validUsername = db.validateUsername(username);
        if (!validUsername) {
            canBeSaved = false;
            regErrorText.setText("Der Username ist bereits vergeben");
        }

        if (!password.equals(passwordWdh)) {
            canBeSaved = false;
            regErrorText.setText("Das Passwort stimmt nicht überein");
        }

        if (username.equals("") || vname.equals("") || nname.equals("") || email.equals("") || password.equals("")) {
            canBeSaved = false;
            regErrorText.setText("Bitte Füllen Sie alle Felder aus");
        }

        if (canBeSaved) {
            db.addUser(username, password, vname, nname, email, dbdate);
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}