package com.example.ali.karakod;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity
{
    ProgressDialog dialog;
    ListView myListView;
    FirebaseDatabase database;
    DatabaseReference kod;
    ArrayAdapter<String> adapter;


    private ArrayList<String> a = new ArrayList<>();
    boolean vericekme=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        kod = database.getReference("scannerqr");
        myListView = findViewById(R.id.ana_listview);
   prog();
        vericek();

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String ab= (String) ((TextView)view).getText();

                    alert(ab,position,view);

                return true;
            }
        });

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ab= (String) ((TextView)view).getText();
                Intent git=new Intent(MainActivity.this,uodate.class);
                git.putExtra("name",(String) ((TextView)view).getText());
                git.putExtra("gelenyer","ana");
                startActivity(git);
            }
        });
    }

    private void prog() {
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setTitle("yükleniyor...");
        dialog.setMessage("lütfen bekleyin..");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void alert(final String ab, final int position, final View view) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MainActivity.this);
        }
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sil(ab);

                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void sil(final String text) {
        kod.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    DataSnapshot ds1=ds.child("kod_name");
                    if (ds1.getValue(String.class).equals(text)){
                        ds.getRef().removeValue();
                        adapter.clear();
                        a.clear();

                        vericek();

                    }


                }
        myListView.refreshDrawableState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });


    }



    public void scan(View view){
        if (vericekme) {
           Intent i=new Intent(MainActivity.this,qrokuma.class);
           i.putExtra("data",a);
           startActivity(i);
        }
    }




    private void vericek() {
        kod.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                   DataSnapshot ds1=ds.child("kod_name");
                       a.add(ds1.getValue(String.class));


                }
                 adapter = new ArrayAdapter<String> ( MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, a );

                myListView.setAdapter(adapter);
                vericekme=true;
                dialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });
    }






    @Override
    protected void onPause() {
        super.onPause();
    }
}
