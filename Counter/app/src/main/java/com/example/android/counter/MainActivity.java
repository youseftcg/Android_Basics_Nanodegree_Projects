package com.example.android.counter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    int i =0;
    int j = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //screens
    public void teamA(int i){
        TextView a= (TextView) findViewById(R.id.teamA);
        a.setText(String.valueOf(i));
    }
    public void teamB(int i){
        TextView a= (TextView) findViewById(R.id.teamB);
        a.setText(String.valueOf(i));

    }
    //buttons A
    public void aAdd1(View view){
        i++;
        teamA(i);
    }
    public void aMinus1(View view){
        if(i!=0){
        i--;

        }
        else{
            i=0;
        }
        teamA(i);
    }
    //buttons B
    public void bAdd1(View view){
        j++;
        teamB(j);
    }
    public void bMinus1(View view){
       if(j!=0) {
           j--;
       }
       else{
           j=0;
       }
        teamB(j);

       }

    public void reset(View view){
        i = 0;
        j=0;
        teamA(i);
        teamB(j);
    }

}

