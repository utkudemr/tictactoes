package com.example.utkudemir.tictactoes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class OnlineLoginActivity extends Activity {

    ListView lv_loginUsers;
    ArrayList<String> list_loginUsers = new ArrayList<String>();
    ArrayAdapter adpt;

    ListView lv_requstedUsers;
    ArrayList<String> list_requestedUsers = new ArrayList<String>();
    ArrayAdapter reqUsersAdpt;

    TextView tvUserID, tvSendRequest, tvAcceptRequest;
    String LoginUserID, UserName, LoginUID;
    String UserPoint,OtherUserPoint,OtherUserPoint1;
    TextView tv;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_online_login);
       // tv=findViewById(R.id.textView);
        tvSendRequest = findViewById(R.id.tvSendRequest);
        tvAcceptRequest = findViewById(R.id.tvAcceptRequest);

        tvSendRequest.setText("bekle...");
        tvAcceptRequest.setText("bekle...");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();

        lv_loginUsers = (ListView) findViewById(R.id.lv_loginUsers);
        adpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list_loginUsers);
        lv_loginUsers.setAdapter(adpt);


        lv_requstedUsers = (ListView) findViewById(R.id.lv_requestedUsers);
        reqUsersAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list_requestedUsers);
        lv_requstedUsers.setAdapter(reqUsersAdpt);


        tvUserID =  findViewById(R.id.tvLoginUser);

        //UserPoint=myRef.child("users").child(UserName).child("point").getValue();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // User is signed in
                    LoginUID = user.getUid();
                    Log.d("Auth", "onAuthStateChanged:signed_in:" + LoginUID);
                    LoginUserID = user.getEmail();
                    tvUserID.setText(LoginUserID);
                    UserName = convertEmailToString(LoginUserID);
                    UserName = UserName.replace(".", "");
                    myRef.child("users").child(UserName).child("request").setValue(LoginUID);
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    rootRef.getRoot().child("users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserPoint = dataSnapshot.child(UserName).child("point").getValue(String.class);
                            if(UserPoint==null)
                            {
                                //UserPoint="0";
                                myRef.child("users").child(UserName).child("point").setValue("0");
                                UserPoint = "0";
                            }
                           // tv.setText(UserPoint.toString());
                            // updateLoginUsers(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    reqUsersAdpt.clear();
                    AcceptIncommingRequests();
                } else {
                    // Çıkış yapıldıysa...
                    Log.d("Auth", "onAuthStateChanged:signed_out");

                    JoinOnlineGame();

                }
            }
        };




        myRef.getRoot().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateLoginUsers(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        lv_loginUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String requestToUser = ((TextView)view).getText().toString();
               // ShowAlert(requestToUser);
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                rootRef.getRoot().child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         String otherUserPoint = dataSnapshot.child(requestToUser).child("point").getValue(String.class);
                         OtherUserPoint=otherUserPoint;


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
                if(OtherUserPoint!=null) {
                  //  ShowAlert(OtherUserPoint,"Success");
                    confirmRequest(requestToUser, "From", OtherUserPoint);
                }

            }

        });



        lv_requstedUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String requestFromUser = ((TextView)view).getText().toString();
               // ShowAlert(requestFromUser);
                DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference();
                rootRef1.getRoot().child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String otherUserPoint1 = dataSnapshot.child(requestFromUser).child("point").getValue(String.class);
                        OtherUserPoint1=otherUserPoint1;
                       // ShowAlert(OtherUserPoint1);
                        //confirmRequest(requestFromUser, "From",OtherUserPoint1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
                 if(OtherUserPoint1!=null) {
                   // ShowAlert(OtherUserPoint1,"Success");
                    confirmRequest(requestFromUser, "To", OtherUserPoint1);
                }

            }
        });

    }



    void confirmRequest(final String OtherPlayer, final String reqType, final String otherUserPoint){
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.connect_player_dialog, null);
            b.setView(dialogView);
            //ShowAlert(otherUserPoint,"fuck");

            b.setTitle("Oyuna Başla?");
            b.setMessage("Bağlan " + OtherPlayer);
            b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String otherPoint=otherUserPoint;
                    myRef.child("users")
                            .child(OtherPlayer).child("request").push().setValue(LoginUserID);
                    if(reqType.equalsIgnoreCase("From")) {
                        StartGame(otherPoint,OtherPlayer + ":" + UserName, OtherPlayer, "From");
                        //StartGame();
                    } if(reqType.equalsIgnoreCase("To")) {
                        StartGame(otherPoint,UserName + ":" + OtherPlayer, OtherPlayer, "To");
                      //  StartGame();
                    }
            }
        });
        b.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShowAlert("sorun","var");
            }
        });
        b.show();



    }


    void StartGame(String OtherPlayerPoint,String PlayerGameID, String OtherPlayer, String requestType){

        /*Intent intocan = new Intent(getApplicationContext(), OnlineGameActivity.class);
        startActivity(intocan);*/
        /*ShowAlert(PlayerGameID,"playerGameId");
        ShowAlert(UserName,"username");
       ShowAlert(OtherPlayer,"otherplayer");
       ShowAlert(LoginUID,"loginuid");
        ShowAlert(requestType,"requesttype");
       ShowAlert(UserPoint,"userpoint");
        ShowAlert(OtherPlayerPoint,"otherplayerpoint");*/
        myRef.child("playing").child(PlayerGameID).removeValue();
        if(PlayerGameID!=null && OtherPlayer!=null && requestType !=null && OtherPlayerPoint!=null && UserName !=null && LoginUID !=null && UserPoint!=null)
        {
            Intent intent = new Intent(getApplicationContext(), OnlineGameActivity.class);
            intent.putExtra("player_session", PlayerGameID);
            intent.putExtra("user_name", UserName);
            intent.putExtra("other_player", OtherPlayer);
            intent.putExtra("login_uid", LoginUID);
            intent.putExtra("request_type", requestType);
           intent.putExtra("user_point",UserPoint);
            intent.putExtra("other_user_point",OtherPlayerPoint);
            startActivity(intent);

        }





    }

    private String convertEmailToString(String Email){
        String value = Email.substring(0, Email.indexOf('@'));
        value = value.replace(".", "");
        return value;
    }


    void AcceptIncommingRequests(){
        myRef.child("users").child(UserName).child("request")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{
                            HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                            if(map != null){
                                String value = "";
                                for(String key:map.keySet()){
                                    value = (String) map.get(key);
                                    reqUsersAdpt.add(convertEmailToString(value));
                                    reqUsersAdpt.notifyDataSetChanged();
                                    myRef.child("users").child(UserName).child("request").setValue(LoginUID);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
    }

    public void updateLoginUsers(DataSnapshot dataSnapshot){
        String key = "";
        Set<String> set = new HashSet<String>();
        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){
            key = ((DataSnapshot) i.next()).getKey();
            if(!key.equalsIgnoreCase(UserName)) {
                set.add(key);
            }
        }

        adpt.clear();
        adpt.addAll(set);
        adpt.notifyDataSetChanged();
        tvSendRequest.setText("Davet Gönder");
        tvAcceptRequest.setText("Daveti Kabul Et");
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void RegisterUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth Complete", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Auth failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    public void JoinOnlineGame() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.login_dialog, null);
        b.setView(dialogView);

        final EditText etEmail = (EditText) dialogView.findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) dialogView.findViewById(R.id.etPassword);

        b.setTitle("Lütfen Kayıt Olun");
        b.setMessage("Kayıt için gerekli alanları doldurun");
        b.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RegisterUser(etEmail.getText().toString(), etPassword.getText().toString());
            }
        });
        b.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
        b.show();
    }
    void ShowAlert(String Title,String title01){
        AlertDialog.Builder b = new AlertDialog.Builder(this, R.style.TransparentDialog);
        b.setTitle(Title)
                .setMessage(title01)
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