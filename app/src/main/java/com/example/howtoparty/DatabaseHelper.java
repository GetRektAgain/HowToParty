package com.example.howtoparty;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "howtoparty.db";
    public static final int DB_VERSION = 5;

    public static String TABLE_NAME;
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_VNAME = "vName";
    public static final String COLUMN_NNAME = "nName";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_BDDATE = "bd_date";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LON = "longitude";
    public static final String COLUMN_VERANSTALTUNGSART = "veranstaltungs_art";
    public static final String COLUMN_VERANSTALTUNGSBECHREIBUNG = "veranstaltungs_beschreibung";
    public static final String COLUMN_IMAGE = "image_data";
    public static final String COLUMN_TEILNEHMER = "teilnehmer";
    public static final String COLUMN_PARTYID = "partyId";
    public static final String COLUMN_USERID = "userId";
    public static final String COLUMN_ORGANIZER = "veranstalter";

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
        String[] dropExecutables = this.getDbTableDropExecuatable();

        for (String dropExecutable: dropExecutables) {
            try{
                db.execSQL(dropExecutable);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        onCreate(db);
    }

    @SuppressLint("Recycle")
    public Cursor isLoginSuccessful (String userUsername, String passwort) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = null;

        String sql = "SELECT * FROM users WHERE username = '" + userUsername +
                "' AND password = '" + passwort + "';";
        try {
            data = db.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
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

        db.insert(TABLE_NAME, null, values);
    }

    public void addParty (LatLng position, String Veranstaltungsart, String VeranstaltungsBeschreibung, byte[] image, int organizerId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_LAT, position.latitude);
        values.put(COLUMN_LON, position.longitude);
        values.put(COLUMN_VERANSTALTUNGSART, Veranstaltungsart);
        values.put(COLUMN_VERANSTALTUNGSBECHREIBUNG, VeranstaltungsBeschreibung);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_ORGANIZER, organizerId);

        db.insert(TABLE_NAME, null, values);
    }

    public void addParty (LatLng position, String Veranstaltungsart, String VeranstaltungsBeschreibung) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_LAT, position.latitude);
        values.put(COLUMN_LON, position.longitude);
        values.put(COLUMN_VERANSTALTUNGSART, Veranstaltungsart);
        values.put(COLUMN_VERANSTALTUNGSBECHREIBUNG, VeranstaltungsBeschreibung);
        values.put(COLUMN_IMAGE, 0);

        db.insert(TABLE_NAME, null, values);
    }

    public void addAttendent (int userId, int partyId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PARTYID, partyId);
        values.put(COLUMN_USERID, userId);
        db.insert("teilnehmer", null, values);
    }

    public boolean isAttendent(int userId,int partyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM teilnehmer WHERE " + COLUMN_USERID +
                " = " + userId + " AND " + COLUMN_PARTYID +
                " = " + partyId + ";";
        Cursor result = db.rawQuery(sql, null);
        int count = result.getCount();
        return count != 0;
    }

    public boolean isOrganizer(int userId, int partyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM partys " +
                "WHERE id = " + partyId +
                " AND " + COLUMN_ORGANIZER + " = " + userId + ";";
        Cursor result = db.rawQuery(sql, null);
        int count = result.getCount();
        return  count != 0;
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

    public Cursor getAttendantsForParty (int partyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM teilnehmer WHERE " +
                COLUMN_PARTYID + " = " + partyId + ";";
        return db.rawQuery(sql, null);
    }

    public boolean validateUsername (String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM users WHERE username = '" + username + "';";

        @SuppressLint("Recycle") Cursor data = db.rawQuery(sql, null);
        return data.getCount() == 0;
    }

    public String[] getDbExecuatable() {
        String[] execuatable = new String[3];
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
                COLUMN_VERANSTALTUNGSART + " TEXT NOT NULL," +
                COLUMN_VERANSTALTUNGSBECHREIBUNG + " TEXT NOT NULL," +
                COLUMN_IMAGE + " BLOB, " +
                COLUMN_ORGANIZER + " INTEGER NOT NULL);";
        execuatable[2] = "CREATE TABLE teilnehmer (" +
                COLUMN_PARTYID + " INTEGER NOT NULL, " +
                COLUMN_USERID + " INTEGER NOT NULL);";

        return execuatable;
    }

    public String[] getDbTableDropExecuatable() {
        String[] dropExecuatable = new String[2];
        dropExecuatable[0] = "DROP TABLE users";
        dropExecuatable[1] = "DROP TABLE partys";
        return dropExecuatable;
    }
}
