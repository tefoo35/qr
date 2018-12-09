package com.example.ali.karakod;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class qrokuma extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    boolean kaydet = false;
    int i = 0;


    ListView myListView;
    FirebaseDatabase database;
    DatabaseReference kod;

ArrayList<String> a=new ArrayList<>();
    private ZXingScannerView scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrokuma);

        Intent i=getIntent();
        a=i.getStringArrayListExtra("data");


        database = FirebaseDatabase.getInstance();
        kod = database.getReference("scannerqr");
        myListView = findViewById(R.id.ana_listview);

        checkCameraPermission();
        scanner = new ZXingScannerView(getApplicationContext());
        setContentView(scanner);
        scanner.setResultHandler(this);
        scanner.startCamera();
    }


    @Override
    public void handleResult(Result result) {


        scanner.resumeCameraPreview(this);
        for (String aa : a) {
            if (aa.equals(result.getText())) {
                kaydet = true;
            }

        }
        if (!kaydet) {

            Toast.makeText(this, "kaydedildi", Toast.LENGTH_SHORT).show();
            kaydet = false;
        } else {
            kaydet = false;
            Toast.makeText(this, "kayıtlı!!", Toast.LENGTH_SHORT).show();
        }


        scanner.removeAllViews(); //<- here remove all the views, it will make an Activity having no View
        scanner.stopCamera();
        Intent ii=new Intent(qrokuma.this,uodate.class);
        ii.putExtra("url",result.getText());
        ii.putExtra("gelenyer","qr");
        startActivity(ii);
        //<- then stop the camera


    }

    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    private void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            Log.d("d","Permission not available requesting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
           
        } else {
            Log.d("dq","Permission has already granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_USE_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent restart=getIntent();
                    qrokuma.this.finish();
                    startActivity(restart);
                    Toast.makeText(this, "izin ver", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "yok", Toast.LENGTH_SHORT).show();   }
                return;
            }
        }

    }
}