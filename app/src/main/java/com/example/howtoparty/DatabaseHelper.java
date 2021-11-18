package com.example.howtoparty;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "howtoparty.db";
    public static final int DB_VERSION = 1;

    public static String TABLE_NAME;
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_VNAME = "vname";
    public static final String COLUMN_NNAME = "nname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_BDDATE = "bd_date";

    public DatabaseHelper(@Nullable Context context, String tableName) {
        super(context, DB_NAME, null, DB_VERSION);
        TABLE_NAME = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String executable = this.getDbExecuatable();
        if (TABLE_NAME.equals("users")) {
            db.execSQL(executable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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

    public boolean validateUsername (String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM users WHERE username = '" + username + "';";

        @SuppressLint("Recycle") Cursor data = db.rawQuery(sql, null);
        return data.getCount() == 0;
    }

    public String getDbExecuatable() {
        if (TABLE_NAME.equals("users")) {
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_VNAME + " TEXT NOT NULL, " +
                    COLUMN_NNAME + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_BDDATE + " TEXT NOT NULL);";
        } else
        return null;
    }
}
