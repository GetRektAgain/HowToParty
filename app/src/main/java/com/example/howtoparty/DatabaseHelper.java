package com.example.howtoparty;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    public static final String url = "jdbc:mysql://howtoparty.cckmvfsk8gy9.eu-central-1.rds.amazonaws.com:3306/howtoparty";
    public static final String user = "admin";
    public static final String pass = "howtoparty072001";
    public static ResultSet resultSet = null;
    public static Cursor a;

    @SuppressLint("Recycle")
    public ResultSet isLoginSuccessful (String userUsername, String passwort) {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = null;
                st = con.createStatement();
                resultSet = st.executeQuery("SELECT * FROM users WHERE username = '" + userUsername +
                        "' AND password = '" + passwort + "';");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
        while (resultSet == null) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return resultSet;
    }

    public void addUser (String username, String passwort, String vname, String nname, String email, String bddate) {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = con.createStatement();
                st.executeQuery("INSERT INTO users (username, password, vName, nName, email, bd_date) VALUES (" +
                        username +", " +
                        passwort +", " +
                        vname + ", " +
                        nname + ", " +
                        email + ", " +
                        bddate + ")");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public void addParty (LatLng position, String veranstaltungsart, String veranstaltungsBeschreibung, byte[] image, int organizerId) {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = con.createStatement();
                st.executeQuery("INSERT INTO partys (latitude, longitude, veranstaltungs_art, veranstaltungs_beschreibung, image, veranstalter) VALUES (" +
                        position.latitude +", " +
                        position.longitude +", " +
                        veranstaltungsart +", " +
                        veranstaltungsBeschreibung +", " +
                        image +", " +
                        organizerId +");");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void addParty (LatLng position, String Veranstaltungsart, String VeranstaltungsBeschreibung) {

    }

    public void addAttendent (int userId, int partyId) {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = con.createStatement();
                st.executeQuery("INSERT INTO teilnehmer (partyId, userId) VALUES (" +
                        partyId +", " +
                        userId +");");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public boolean isAttendent(int userId,int partyId) throws SQLException {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = con.createStatement();
                resultSet = st.executeQuery("SELECT * FROM teilnehmer WHERE userId = " + userId +
                        " AND partyId = " + partyId + ";");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
        while (resultSet == null) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return resultSet.next();
    }

    public boolean isOrganizer(int userId, int partyId) throws SQLException {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = con.createStatement();
                resultSet = st.executeQuery("SELECT * FROM partys WHERE veranstalter = " + userId +
                        " AND id = " + partyId + ";");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
        while (resultSet == null) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return resultSet.next();
    }

    public ResultSet getPartys () {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = con.createStatement();
                resultSet = st.executeQuery("SELECT * FROM partys;");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
        while (resultSet == null) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return resultSet;
    }

    public ResultSet getParty (int id) {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = con.createStatement();
                resultSet = st.executeQuery("SELECT * FROM partys WHERE id = "+ id +";");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
        while (resultSet == null) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return resultSet;
    }

    public Cursor getAttendantsForParty (int partyId) {
        return a;
    }

    public boolean validateUsername (String username) throws SQLException {
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = con.createStatement();
                resultSet = st.executeQuery("SELECT * FROM users WHERE username = "+ username +";");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();
        while (resultSet == null) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return !resultSet.next();
    }
}
