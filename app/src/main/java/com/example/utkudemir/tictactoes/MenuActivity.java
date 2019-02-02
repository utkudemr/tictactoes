package com.example.utkudemir.tictactoes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MenuActivity extends Activity {

    FirebaseDatabase db;
    DatabaseReference ref;
    DatabaseReference mDatabase;
    String name;
    String point;

    ListView lv_usersPoint;
    ListView lv_usersPoint1;
   public ArrayList<String> list_usersPoint = new ArrayList<String>();
    public ArrayList<String> list_usersPoint1 = new ArrayList<String>();
    ArrayAdapter<String> adpt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
        lv_usersPoint=findViewById(R.id.users_point_list);
        lv_usersPoint1=findViewById(R.id.users_point_list1);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("users");


        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        usersdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    String name=datas.getKey();
                    //ShowAlert(name);
                    list_usersPoint.add(name);
                }
                    Collections.sort(list_usersPoint);
                   // Collections.reverse(list_usersPoint);
                    ArrayAdapter<String> adapter = new ArrayAdapter(MenuActivity.this, android.R.layout.simple_list_item_1, list_usersPoint);

                    lv_usersPoint.setAdapter(adapter);
                DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference();
                rootRef1.getRoot().child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       // Collections.reverse(list_usersPoint);
                        for( String points: list_usersPoint)
                        {

                            //ShowAlert(points);
                            String name1=dataSnapshot.child(points).child("point").getValue(String.class);
                            list_usersPoint1.add(name1);
                        }
                      //  Collections.sort(list_usersPoint1);
                       // Collections.reverse(list_usersPoint1);
                        ArrayAdapter<String> adapter = new ArrayAdapter(MenuActivity.this, android.R.layout.simple_list_item_1, list_usersPoint1);

                        lv_usersPoint1.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
               /* for (DataSnapshot ds : dataSnapshot.getChildren()) {
                   for( String points: list_usersPoint)
                   {
                           //ShowAlert(points);
                          String name1=ds.child(points).child("point").getValue(String.class);
                          ShowAlert(name1);
                           //list_usersPoint.add(name);
                       }
                   }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
      /*  ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String name = ds.child("users").getValue(String.class);
                    //ShowAlert(ds.child("users").getValue(String.class));
                    Log.d("TAG", name);

                    list_usersPoint.add(name);

                }
               // Collections.sort(list_usersPoint);
               // Collections.reverse(list_usersPoint);
              //  ArrayAdapter<String> adapter = new ArrayAdapter(MenuActivity.this, android.R.layout.simple_list_item_1, list_usersPoint);

               // lv_usersPoint.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ShowAlert("sadsadsa");
        usersdRef.addListenerForSingleValueEvent(eventListener);*/
    }









    public void startGame_Single(View view)
    {
        Intent i=new Intent(this,SinglePlayer.class);
        startActivity(i);
    }
    public  void startGame_Online(View view)
    {
        Intent i=new Intent(this,OnlineLoginActivity.class);
        startActivity(i);
    }

    public void updateLoginUsers(DataSnapshot dataSnapshot){
        String key = "";
        Set<String> set = new HashSet<String>();
        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){
            key = ((DataSnapshot) i.next()).getKey();
            if(!key.equalsIgnoreCase(name)) {
                set.add(key);
            }
        }

        adpt.clear();
        adpt.addAll(set);
        adpt.notifyDataSetChanged();
    }
    void ShowAlert(String Title){
        AlertDialog.Builder b = new AlertDialog.Builder(this, R.style.TransparentDialog);
        b.setTitle(Title)
                .setMessage("Start a new game?")
                .setNegativeButton("Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
