package com.example.howtoparty;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.transform.Result;

public class LoginActivity extends AppCompatActivity {

    public DatabaseHelper db;
    private EditText loginUsername;
    private EditText loginPassword;
    private Button btnLogin;
    private Button btnRegister;
    private TextView loginErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkPermission();

        db = new DatabaseHelper();

        loginUsername = (EditText) findViewById(R.id.LoginUsername);
        loginPassword = (EditText) findViewById(R.id.LoginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        loginErrorText = (TextView) findViewById(R.id.loginErrorMessage);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login(loginUsername.getText().toString(), loginPassword.getText().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(String username, String password) throws InterruptedException, SQLException {
        ResultSet successful = db.isLoginSuccessful(username, password);
        if (successful.next()) {
            if (successful.getString(2).equals(password)) {
                Bundle bundle = new Bundle();
                bundle.putInt("userId", successful.getInt(1));
                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                loginErrorText.setText("Login fehlgeschlagen. Username oder Passwort falsch");
            }
        } else {
            loginErrorText.setText("Login fehlgeschlagen. Username oder Passwort falsch");
        }
    }

    public void checkPermission()
    {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
        }
    }
}