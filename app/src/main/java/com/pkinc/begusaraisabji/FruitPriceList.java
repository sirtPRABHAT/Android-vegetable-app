package com.pkinc.begusaraisabji;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FruitPriceList extends AppCompatActivity {

    ListView fruit_price_list;



    String phoneNo;
    String SmsMessage;
    DatabaseReference rootRef,fruitRef;
    FirebaseDatabase database;
    String vegName, vegValue;
    ArrayList<String> array;
    ProgressBar progressBarFruit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fruit_price_list);



        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();
        fruitRef = rootRef.child("fruit");


        fruit_price_list = (ListView)findViewById(R.id.fruit_price_list_view) ;
        progressBarFruit = (ProgressBar)findViewById(R.id.progressBarFruit);

        fruitRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("Failed to read value.", "error");
            }
        });



    }

    public void showData(DataSnapshot dataSnapshot) {
        array = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            Fruit fru = new Fruit();
            fru.setName(ds.getValue(Vegetable.class).getName());
            fru.setPrice(ds.getValue(Vegetable.class).getPrice());
            array.add(fru.getName() + "  -  " + fru.getPrice());
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item,array);
        progressBarFruit.setVisibility(View.GONE);
        fruit_price_list.setAdapter(adapter);
    }



}
