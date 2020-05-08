package com.example.connect3;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.*;

public class MainActivity extends AppCompatActivity {

    TextView winnerTextView;
    Boolean englishFlag;
    ImageView imageView;
    GridLayout gridLayout;
    Boolean appear = false;
    MediaPlayer mediaPlayer;
    Intent intent;

    // Save the current game state
    int[] gameState = {2,2,2,2,2,2,2,2,2};

    // set the winning position
    int[][] winningPositions={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};

    // 0 for yellow and 1 for red, that represent who is the activePlayer;
    int activePlayer = 0;

    // check whether the game is active right now.
    boolean gameActive = true;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void dropIn(View view){

        // show the textView whose ture it is
        // and display who wins the game
        winnerTextView = (TextView)findViewById(R.id.winnertextView);

        //access which area we want to drop-in
        ImageView counter = (ImageView)view;

        // get the current position(through tag from 0-8)
        // and change the value of the current position(change 2 to 0 or 1) if the current positon tag is 2
        int trappedCounter = Integer.parseInt(counter.getTag().toString());

        if(gameState[trappedCounter] ==2 && gameActive){
            gameState[trappedCounter] = activePlayer;

            // based on the active user, the button(red or yellow)
            // appears below, with rotation last for 1500 ms
            counter.setTranslationY(1500);
            if(activePlayer ==0){
                if(englishFlag == true){
                    winnerTextView.setText("Red player Turn");
                }else{
                    winnerTextView.setText("Le prochain tour est rouge");
                }
                counter.setImageResource(R.drawable.yellow);
                activePlayer = 1;
            }else{
                if(englishFlag == true){
                    winnerTextView.setText("Yellow player Turn");
                }else{
                    winnerTextView.setText("Le prochain tour est jaune");
                }
                counter.setImageResource(R.drawable.red);
                activePlayer = 0;
            }
            counter.animate().translationY(7).rotation(3600).setDuration(1000);

            // check weather someone win
            // it is based on the winning position as we defined above
            String winner = "";
            for(int[] winningPosition : winningPositions){
                if(gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]]!=2){

                    // end the game
                    //check and return who is win
                    gameActive = false;

                    if(activePlayer == 0){
                        if(englishFlag == true){
                            winner = "red";
                        }else{
                            winner = "rouge";
                        }
                    }else{
                        if(englishFlag == true){
                            winner = "yellow";
                        }else{
                            winner = "jaune";
                        }
                    }


                    // jump to the new activity to show who win
                    intent = new Intent(getApplicationContext(), SecondActivity.class);
                    intent.putExtra("Winner",winner);
                    startActivity(intent);
                }

            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the initial position of the picture
        imageView= (ImageView) findViewById(R.id.imageView);
        imageView.animate().translationXBy(-1000);

        // get the mediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.marbles);


        // create the alert dialog
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_btn_speak_now)
                .setTitle("Choose a language")
                .setMessage("Which language would you like to use")
                .setPositiveButton("English", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // set English right now
                        englishFlag = true;
                    }
                })
                .setNegativeButton("Frence", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //set French
                        englishFlag = false;
                    }
                })
                .show();
    }

    //make the menu set up
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    // when we click the stop, a picture appear.
    // when we click the continue, the picture disappear.
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.Stop:
                imageView.animate().translationXBy(1000).setDuration(3000);
                mediaPlayer.start();
                appear = true;
                return true;
            case R.id.Continue:
                if(appear== false ){
                    return false;
                }else{
                    imageView.animate().translationXBy(-1000).rotation(1800).setDuration(1000);
                    return true;
                }
            default:
                return false;
        }

    }
}
