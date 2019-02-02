package com.example.utkudemir.tictactoes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SinglePlayer extends Activity {

    int gameState=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_single_player);

        gameState=1;
    }

    public void GameBoardClick(View view){
        ImageView selectedImage = (ImageView) view;

        int selectedBlock = 0;
        switch ((selectedImage.getId())) {
            case R.id.iv_11: selectedBlock = 1; break;
            case R.id.iv_12: selectedBlock = 2; break;
            case R.id.iv_13: selectedBlock = 3; break;

            case R.id.iv_21: selectedBlock = 4; break;
            case R.id.iv_22: selectedBlock = 5; break;
            case R.id.iv_23: selectedBlock = 6; break;

            case R.id.iv_31: selectedBlock = 7; break;
            case R.id.iv_32: selectedBlock = 8; break;
            case R.id.iv_33: selectedBlock = 9; break;
        }

        PlayGame(selectedBlock, selectedImage);
    }

    int activePlayer = 1;
    ArrayList<Integer> Player1 = new ArrayList<Integer>();
    ArrayList<Integer> Player2 = new ArrayList<Integer>();

    void PlayGame(int selectedBlock, ImageView selectedImage){
        int time=299;
        if(gameState == 1) {
            if (activePlayer == 1) {
                selectedImage.setImageResource(R.drawable.x);
                Player1.add(selectedBlock);
                activePlayer = 2;
                AutoPlay();
            }else if (activePlayer == 2) {

                selectedImage.setImageResource(R.drawable.o);
                Player2.add(selectedBlock);
                activePlayer = 1;
            }

            selectedImage.setEnabled(false);
            checkWinner();
        }
    }


    void AutoPlay(){
        ArrayList<Integer> emptyBlocks = new ArrayList<Integer>();

        for(int i=1; i<=9; i++){
            if(!(Player1.contains(i) || Player2.contains(i))){
                emptyBlocks.add(i);
            }
        }

        if(emptyBlocks.size() == 0)
        {
            checkWinner();
            if(gameState == 1)
            {
                AlertDialog.Builder b = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                showAlert("Berabere");
            }
            gameState = 3;
        } else {
            Random r = new Random();
            int randomIndex = r.nextInt(emptyBlocks.size());
            int selectedBock = emptyBlocks.get(randomIndex);


            ImageView selectedImage = (ImageView) findViewById(R.id.iv_11);
            switch (selectedBock) {
                case 1: selectedImage =  findViewById(R.id.iv_11); break;
                case 2: selectedImage =  findViewById(R.id.iv_12); break;
                case 3: selectedImage =  findViewById(R.id.iv_13); break;

                case 4: selectedImage =  findViewById(R.id.iv_21); break;
                case 5: selectedImage =  findViewById(R.id.iv_22); break;
                case 6: selectedImage =  findViewById(R.id.iv_23); break;

                case 7: selectedImage =  findViewById(R.id.iv_31); break;
                case 8: selectedImage =  findViewById(R.id.iv_32); break;
                case 9: selectedImage =  findViewById(R.id.iv_33); break;
            }
            PlayGame(selectedBock, selectedImage);
        }
    }



    private void showAlert(String Title) {
        AlertDialog.Builder b=new AlertDialog.Builder(this,R.style.TransparentDialog);
        b.setTitle(Title).setMessage("Bir el daha?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        ResetGame();
                    }
                })
                .setNegativeButton("Menü", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Intent i =new Intent(getApplicationContext(),MenuActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info).show();
    }
    void ResetGame()
    {
        gameState=1;
        activePlayer=1;
        Player1.clear();
        Player2.clear();

        ImageView iv;
        iv= findViewById(R.id.iv_11); iv.setImageResource(0); iv.setEnabled(true);
        iv= findViewById(R.id.iv_12); iv.setImageResource(0); iv.setEnabled(true);
        iv= findViewById(R.id.iv_13); iv.setImageResource(0); iv.setEnabled(true);

        iv= findViewById(R.id.iv_21); iv.setImageResource(0); iv.setEnabled(true);
        iv= findViewById(R.id.iv_22); iv.setImageResource(0); iv.setEnabled(true);
        iv= findViewById(R.id.iv_23); iv.setImageResource(0); iv.setEnabled(true);

        iv= findViewById(R.id.iv_31); iv.setImageResource(0); iv.setEnabled(true);
        iv= findViewById(R.id.iv_32); iv.setImageResource(0); iv.setEnabled(true);
        iv= findViewById(R.id.iv_33); iv.setImageResource(0); iv.setEnabled(true);
    }

    void checkWinner(){
        int winner = 0;


        if(Player1.contains(1) && Player1.contains(2) && Player1.contains(3)){ winner = 1; }
        if(Player1.contains(4) && Player1.contains(5) && Player1.contains(6)){ winner = 1; }
        if(Player1.contains(7) && Player1.contains(8) && Player1.contains(9)){ winner = 1; }

        if(Player1.contains(1) && Player1.contains(4) && Player1.contains(7)){ winner = 1; }
        if(Player1.contains(2) && Player1.contains(5) && Player1.contains(8)){ winner = 1; }
        if(Player1.contains(3) && Player1.contains(6) && Player1.contains(9)){ winner = 1; }

        if(Player1.contains(1) && Player1.contains(5) && Player1.contains(9)){ winner = 1; }
        if(Player1.contains(3) && Player1.contains(5) && Player1.contains(7)){ winner = 1; }



        if(Player2.contains(1) && Player2.contains(2) && Player2.contains(3)){ winner = 2; }
        if(Player2.contains(4) && Player2.contains(5) && Player2.contains(6)){ winner = 2; }
        if(Player2.contains(7) && Player2.contains(8) && Player2.contains(9)){ winner = 2; }

        if(Player2.contains(1) && Player2.contains(4) && Player2.contains(7)){ winner = 2; }
        if(Player2.contains(2) && Player2.contains(5) && Player2.contains(8)){ winner = 2; }
        if(Player2.contains(3) && Player2.contains(6) && Player2.contains(9)){ winner = 2; }

        if(Player2.contains(1) && Player2.contains(5) && Player2.contains(9)){ winner = 2; }
        if(Player2.contains(3) && Player2.contains(5) && Player2.contains(7)){ winner = 2; }



        if(winner != 0 && gameState == 1){
            if(winner == 1){
                showAlert("Sen Kazandın");
            }else if(winner == 2){
                showAlert("Ben Kazandım");
            }
            gameState = 2;
        }
    }
}
