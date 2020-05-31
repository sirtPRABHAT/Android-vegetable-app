package com.pkinc.begusaraisabji;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MakeOrder extends AppCompatActivity {
   // private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;


    ArrayList<String> array;
    ArrayList<String> arrayFruit;
    String message;
    EditText inputName;
    EditText inputPhone;
    EditText inputEmail;
    EditText inputAmount;
    EditText inputAdress;
    TextView locationTextView;
    TextView mItemSelected, mItemSelectedFruit;
    Button makeOrder;
    double longitude;
    double latitude;
    EditText Address;
    String Id;
    String RequestId;
    String coordinates;
    DatabaseReference rootRef,orderRef,vegetableRef,fruitRef;
    FirebaseDatabase database;
    SimpleDateFormat sdf;
    String currentDateandTime;
    boolean mobileDataEnabled = false;


    String listItems[] ;
    String listItemsFruit[] ;
    boolean[] checkedItems,checkedItemsFruit;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    ArrayList<Integer> mUserItemsFruit = new ArrayList<>();
    Random rand = new Random();

    public static class Order {

        public String time_stamp;
        public String name;
        public String phone;
        public  String email;
        public String address;
        public String location;
        public String items;
        public String amount;
        public String requestId;


        public Order(String time_stamp , String name, String phone, String email, String address, String location,  String items, String amount, String requestId) {
            this.time_stamp = time_stamp;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.address = address;
            this.location = location;
            this.items = items;
            this.amount = amount;
            this.requestId = requestId;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_order);


        sdf = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss ");
        currentDateandTime = sdf.format(new Date());
        database = FirebaseDatabase.getInstance();
        rootRef = database.getReference();
        orderRef = rootRef.child("orders");
        vegetableRef = rootRef.child("vegetable");
        fruitRef = rootRef.child("fruit");



        inputName = (EditText)findViewById(R.id.inputName);
         inputPhone = (EditText)findViewById(R.id.Phone);
         inputAdress = (EditText)findViewById(R.id.Address);
         inputEmail = (EditText)findViewById(R.id.Email);
         inputAmount = (EditText)findViewById(R.id.Amount);
         locationTextView = (TextView)findViewById(R.id.locationTextView);
         mItemSelected = (TextView)findViewById(R.id.list);
         mItemSelectedFruit = (TextView)findViewById(R.id.SelectedFruit);
         makeOrder = (Button)findViewById(R.id.makeOrder);

//---------------------------getting data caome with intent--------------------------
         final Intent intent = getIntent();
          message = intent.getStringExtra("message");
         locationTextView.setText(message);

//---------------------------getting data caome with intent ends---------------------

        if(message.isEmpty()){
            AlertDialog.Builder noLocationAlert = new AlertDialog.Builder(MakeOrder.this);
            noLocationAlert.setMessage("Unable to fetch your location, You must reload the page or provide your address in address column");
            noLocationAlert.setPositiveButton("Reload", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Intent reload = new Intent(MakeOrder.this, MakeOrder.class);
                    finish();
//                    overridePendingTransition(0,0);
//                    startActivity(reload);
//                    overridePendingTransition(0,0);
                }
            });
            noLocationAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            noLocationAlert.create().show();
        }



        Address = (EditText)findViewById(R.id.Address);
        locationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 coordinates = message;
                String[] arrOfLatLong = coordinates.split(",",2);
                latitude =  Double.valueOf(arrOfLatLong[0]);
                longitude = Double.valueOf(arrOfLatLong[1]);
                Uri gmmIntentUri = Uri.parse("geo:"+latitude+","+longitude+"z=51?q="+Address.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if(mapIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(mapIntent);
                }else{
                    Toast.makeText(MakeOrder.this, "Unable to open google maps", Toast.LENGTH_LONG);
                }

            }
        });

        //-----------------------------------------------------------------------------------------------------------------------------------

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

        fruitRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showDataFruit(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("Failed to read value.", "error");
            }
        });




        makeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    currentDateandTime = sdf.format(new Date());
                   Id = Integer.toString(rand.nextInt(1000) + 1);
                   RequestId = inputName.getText().toString() + "-#" + Id;
                    orderRef.push().setValue(new Order(currentDateandTime , inputName.getText().toString(), inputPhone.getText().toString(), inputEmail.getText().toString(), inputAdress.getText().toString(), message,  mItemSelected.getText().toString()+", "+mItemSelectedFruit.getText().toString(), inputAmount.getText().toString(), RequestId)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                              //  Toast.makeText(MakeOrder.this, "Order Submitted Sucessfully", Toast.LENGTH_LONG).show();
                            // ---------alert Dialog for request Id-------------------------------------------------
            final AlertDialog.Builder requestIdDialog = new AlertDialog.Builder(MakeOrder.this);

            requestIdDialog.setMessage("Your Order has been placed, With order Id- " + RequestId);
            requestIdDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            requestIdDialog.create().show();
//
//            //---------alert Dialog for request Id-------------------------------------------------
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MakeOrder.this,"Failed",Toast.LENGTH_LONG).show();
                        }
                    });

            }
        });

    }


    public void showData(DataSnapshot dataSnapshot) {
        array = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            Vegetable veg = new Vegetable();

            veg.setName(ds.getValue(Vegetable.class).getName());
            veg.setPrice(ds.getValue(Vegetable.class).getPrice());
            array.add(veg.getName() + "   -   " + veg.getPrice());
            System.out.println(array);
        }
    String[] arr = array.toArray(new String[array.size()]);
         listItems = arr;
       checkedItems = new boolean[listItems.length];
        mItemSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MakeOrder.this);
                mBuilder.setTitle("Vegetables Available");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUserItems.contains(position)){
                                mUserItems.add(position);
                            }

                        } else if(mUserItems.contains(position)){
                            mUserItems.remove((Integer) position);
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++){
                            item = item +listItems[mUserItems.get(i)];
                            if(i != mUserItems.size() -1){
                                item = item + ", ";
                            }
                        }
                        mItemSelected.setText(item);
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for(int i = 0; i < checkedItems.length; i++){
                            checkedItems[i] = false;
                            mUserItems.clear();
                            mItemSelected.setText("");
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });



    }

    public void showDataFruit(DataSnapshot dataSnapshot) {
        arrayFruit = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            Fruit fru = new Fruit();

            fru.setName(ds.getValue(Fruit.class).getName());
            fru.setPrice(ds.getValue(Fruit.class).getPrice());
            arrayFruit.add(fru.getName() + "   -   " + fru.getPrice());
            System.out.println(arrayFruit);
        }
        String[] arr = arrayFruit.toArray(new String[arrayFruit.size()]);
        listItemsFruit = arr;
        checkedItemsFruit = new boolean[listItemsFruit.length];
        mItemSelectedFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MakeOrder.this);
                mBuilder.setTitle("Fruits Available");
                mBuilder.setMultiChoiceItems(listItemsFruit, checkedItemsFruit, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUserItemsFruit.contains(position)){
                                mUserItemsFruit.add(position);
                            }

                        } else if(mUserItemsFruit.contains(position)){
                            mUserItemsFruit.remove((Integer) position);
                        }
                    }
                });
                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String itemFruit = "";
                        for (int i = 0; i < mUserItemsFruit.size(); i++){
                            itemFruit = itemFruit +listItemsFruit[mUserItemsFruit.get(i)];
                            if(i != mUserItemsFruit.size() -1){
                                itemFruit = itemFruit + ", ";
                            }
                        }
                        mItemSelectedFruit.setText(itemFruit);
                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for(int i = 0; i < checkedItemsFruit.length; i++){
                            checkedItemsFruit[i] = false;
                            mUserItemsFruit.clear();
                            mItemSelectedFruit.setText("");
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.add:
            //add the function to perform here
            return(true);
        case R.id.reset:
            finish();
            return(true);
        case R.id.about:
            //add the function to perform here
            return(true);
        case R.id.exit:
            //add the function to perform here
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

}
