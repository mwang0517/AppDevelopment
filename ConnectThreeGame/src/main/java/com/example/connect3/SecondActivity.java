package com.example.connect3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    TextView textView;
    SQLiteDatabase myDatabase;
    Cursor c;
    int scoreYellow = 0;
    int scoreRed = 0;


    // the following code let user play again
    public void playAgain(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView =  (TextView)findViewById(R.id.textView);

        Intent intent = getIntent();
        String message = intent.getStringExtra("Winner");

        // display who is the winner based on the language
        if(message.equals("yellow") || message.equals("red")){
            textView.setText(message + " player has won the game:)");
        }else if (message.equals("rouge")|| message.equals("jaune")){
            textView.setText(message + " pGagner le concours:)");
        }else{
            textView.setText("NULL");
        }

        // the following code save the score for the game.
        try{
            myDatabase = this.openOrCreateDatabase("scoreInfo",MODE_PRIVATE,null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users(winnerName VARCHAR)");
            if(message.equals("red")|| message.equals("rouge")){
                myDatabase.execSQL("INSERT INTO users (winnerName) VALUES ('red')");
            }else if(message.equals("yellow")|| message.equals("jaune")) {
                myDatabase.execSQL("INSERT INTO users (winnerName) VALUES ('yellow')");
            }else{
                myDatabase.execSQL("INSERT INTO users (winnerName) VALUES ('null')");
            }

            c = myDatabase.rawQuery("SELECT * FROM users", null);
            int index = c.getColumnIndex("winnerName");
            c.moveToFirst();

            while(!c.isAfterLast()){

                if(c.getString(index).equals("red")|| c.getString(index).equals("rouge")){
                    scoreRed += 1;
                }else if (c.getString(index).equals("yellow")|| c.getString(index).equals("jaune")){
                    scoreYellow += 1;
                }else{
                    scoreRed +=0;
                    scoreYellow += 0;
                }
                c.moveToNext();
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        // The following code display the score to the user
        if(message.equals("yellow") || message.equals("red")){
            Toast.makeText(this,"Yellow VS Red " + scoreYellow+ " : " + scoreRed ,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Jaune VS Rouge " + scoreYellow+ " : " + scoreRed ,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    // when we click the history, the we can see the history of the winner
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.History:
                String winnerList = "";
                c = myDatabase.rawQuery("SELECT * FROM users", null);
                int index = c.getColumnIndex("winnerName");
                c.moveToFirst();
                while(!c.isAfterLast()){
                    winnerList += c.getString(index)+ "\n";
                    c.moveToNext();
                }
                Toast.makeText(this,winnerList,Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
        }

    }
}
