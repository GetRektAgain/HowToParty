package com.example.howtoparty;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Array;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "howtoparty.db";
    public static final int DB_VERSION = 3;

    public static String TABLE_NAME;
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_VNAME = "vname";
    public static final String COLUMN_NNAME = "nname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_BDDATE = "bd_date";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LON = "longitude";
    public static final String COLUMN_MUSIKR = "musikrichtung";
    public static final String COLUMN_TEILNEHMER = "teilnehmer";

    public DatabaseHelper(@Nullable Context context, String tableName) {
        super(context, DB_NAME, null, DB_VERSION);
        TABLE_NAME = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] executables = this.getDbExecuatable();
        for (String executable: executables ) {
            db.execSQL(executable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] executables = this.getDbExecuatable();
        String[] dropExecutables = this.getDbTableDropExecuatable();
        for (String dropExecutable: dropExecutables) {
            db.execSQL(dropExecutable);
        }
        for (String executable: executables ) {
            db.execSQL(executable);
        }
    }

    @SuppressLint("Recycle")
    public boolean isLoginSuccessful (String userUsername, String passwort) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = null;

        String sql = "SELECT * FROM users WHERE username = '" + userUsername +
                "' AND password = '" + passwort + "';";
        try {
            data = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data.getCount() == 1;
    }

    public void addUser (String username, String passwort, String vname, String nname, String email, String bddate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, passwort);
        values.put(COLUMN_VNAME, vname);
        values.put(COLUMN_NNAME, nname);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_BDDATE, bddate);

        long id = db.insert(TABLE_NAME, null, values);

    }

    public void addParty (LatLng position, String musikrichtung) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_LAT, position.latitude);
        values.put(COLUMN_LON, position.longitude);
        values.put(COLUMN_MUSIKR, musikrichtung);

        db.insert(TABLE_NAME, null, values);
    }

    public Cursor getPartys () {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM partys";
        return db.rawQuery(sql, null);
    }

    public Cursor getParty (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM partys WHERE id = "+ id;
        return db.rawQuery(sql, null);
    }

    public boolean validateUsername (String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM users WHERE username = '" + username + "';";

        @SuppressLint("Recycle") Cursor data = db.rawQuery(sql, null);
        return data.getCount() == 0;
    }

    public String[] getDbExecuatable() {
        String[] execuatable = new String[2];
        execuatable[0] = "CREATE TABLE users (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_VNAME + " TEXT NOT NULL, " +
                COLUMN_NNAME + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT NOT NULL, " +
                COLUMN_BDDATE + " TEXT NOT NULL);";
        execuatable[1] = "CREATE TABLE partys (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LAT + " DOUBLE NOT NULL, " +
                COLUMN_LON + " DOUBLE NOT NULL, " +
                COLUMN_MUSIKR + " TEXT NOT NULL);";

        return execuatable;
    }

    public String[] getDbTableDropExecuatable() {
        String[] dropExecuatable = new String[1];
        dropExecuatable[0] = "DROP TABLE users";
        dropExecuatable[1] = "DROP TABLE partys";
        return dropExecuatable;
    }
}
