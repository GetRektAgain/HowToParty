package com.example.howtoparty;

import android.Manifest;
import android.content.ContentProvider;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RegisterPartyActivity extends AppCompatActivity {
    private ContentProvider contentProvider;
    private EditText textVeranstaltungsart;
    private Button btnRegistrieren;
    private Button btnAbbrechen;
    private Button btnBildAuswählen;
    private EditText textBeschreibung;
    private Bitmap ImageVeranstaltung;
    private ImageView ImageVeranstaltInsideApp;
    DatabaseHelper dbPartys = new DatabaseHelper(this, "partys");
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_party);

        textVeranstaltungsart = (EditText) findViewById(R.id.textVeranstaltungsart);
        textBeschreibung =(EditText) findViewById(R.id.VeranstaltungBeschreibung);
        btnRegistrieren = (Button) findViewById(R.id.btnRegistrieren);
        btnAbbrechen = (Button) findViewById(R.id.btnAbbrechen);
        btnBildAuswählen = (Button) findViewById(R.id.btnVeranstaltungBildAuswählen);
        ImageVeranstaltInsideApp = (ImageView) findViewById(R.id.Veranstaltung_Bild);

        Intent intent = getIntent();
        LatLng position = intent.getParcelableExtra("position");


        btnBildAuswählen.setOnClickListener(new View.OnClickListener() {
            private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Explain to the user why we need to read the contacts
                    }
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant that should be quite unique
                    return;
                }

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/png , image/jpeg");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/png , image/jpeg");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        btnAbbrechen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPartyActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        btnRegistrieren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPartyActivity.this, MapsActivity.class);
                startActivity(intent);
                MapsActivity mapFragment=new MapsActivity();
                dbPartys.addParty(position, textVeranstaltungsart.getText().toString(), textBeschreibung.getText().toString(),getBytes(ImageVeranstaltung));
            }
        });
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream is = null;
            try {
                is = getContentResolver().openInputStream(data.getData());
                ImageVeranstaltung = scaleDown(BitmapFactory.decodeStream(is, null, options),128,false );
                ImageVeranstaltInsideApp.setImageBitmap(ImageVeranstaltung);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}