package com.example.ali.karakod;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class uodate extends AppCompatActivity {
    DatabaseReference kod,kod_name,kod_url,adets;
    TextView Tbarkod_name;
    EditText barkodname,adet;
    Button kaydet,iptal;
    FirebaseDatabase database;
     String gelen_url,gelen_name;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uodate);

        findid();
    prog();
        Intent i=getIntent();


        if (i.getStringExtra("gelenyer").equals("ana")){
            gelen_name=i.getStringExtra("name");
            veriler(gelen_name);

        }
             else{
            gelen_url=i.getStringExtra("url");
            Tbarkod_name.setText(gelen_url);
            kontrol(gelen_url);

}
     kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    kod_url=kod.child(gelen_url.toString());
                kod_name=kod_url.child("kod_name");
                adets=kod_url.child("adet");
                kod_name.setValue(barkodname.getText().toString());
                adets.setValue(adet.getText().toString());
                Toast.makeText(uodate.this, "kaydedildi!", Toast.LENGTH_SHORT).show();
              git();
            }
        });

     iptal.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             git();
         }
     });

    }
    private void prog() {
        dialog = new ProgressDialog(uodate.this);
        dialog.setTitle("yükleniyor...");
        dialog.setMessage("lütfen bekleyin..");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void kontrol(final String gelen_url) {
        kod.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    DataSnapshot ds1=ds.child("kod_name");
                    DataSnapshot ds2=ds.child("adet");

                    if (gelen_url.equals(ds.getKey())){
                        barkodname.setText(ds1.getValue(String.class));
                        adet.setText(ds2.getValue(String.class));

                    dialog.cancel();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });



    }

    private void veriler(final String gelen_name) {


            kod.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        DataSnapshot ds1=ds.child("kod_name");
                        DataSnapshot ds2=ds.child("adet");

                                 if (gelen_name.equals(ds1.getValue(String.class))){
                                barkodname.setText(ds1.getValue(String.class));
                                adet.setText(ds2.getValue(String.class));
                                Tbarkod_name.setText(ds.getKey());
                                gelen_url=ds.getKey();

                                     dialog.cancel();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }

            });


    }

    private void git() {
        Intent i=new Intent(uodate.this,MainActivity.class);
        startActivity(i);
    }

    private void findid() {
        database = FirebaseDatabase.getInstance();
        kod = database.getReference("scannerqr");


        kaydet=(Button)findViewById(R.id.kaydet);
            iptal=(Button) findViewById(R.id.iptal);
        Tbarkod_name=(TextView) findViewById(R.id.Tname);
        barkodname=(EditText) findViewById(R.id.urun_ad);
        adet=(EditText)findViewById(R.id.urun_adet);

    }
}
