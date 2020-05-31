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

public class VegPriceList extends AppCompatActivity {


    ListView veg_price_list;



    String phoneNo;
    String SmsMessage;
    DatabaseReference rootRef,orderRef,vegetableRef;
    FirebaseDatabase database;
    String vegName, vegValue;
    ArrayList<String> array;
    ProgressBar progressBarVeg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.veg_price_list);



        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();
        vegetableRef = rootRef.child("vegetable");


        veg_price_list = (ListView)findViewById(R.id.veg_price_list_view) ;
        progressBarVeg = (ProgressBar)findViewById(R.id.progressBarVeg);

        vegetableRef.addValueEventListener(new ValueEventListener() {
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
            Vegetable veg = new Vegetable();
            veg.setName(ds.getValue(Vegetable.class).getName());
            veg.setPrice(ds.getValue(Vegetable.class).getPrice());

            Log.i("vegetablename", veg.getName());
            Log.d("vegetable",  veg.getPrice());
            array.add(veg.getName() + "  -  " + veg.getPrice());
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item,array);
        progressBarVeg.setVisibility(View.GONE);
        veg_price_list.setAdapter(adapter);
    }


}

