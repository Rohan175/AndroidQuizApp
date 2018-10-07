package com.rsking175453.com.iaa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class resultScreen extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private ConstraintSet c1 = new ConstraintSet();
    private ConstraintSet c2 = new ConstraintSet();
    boolean turn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_result_screen);

        ArrayList<Integer> result =  getIntent().getIntegerArrayListExtra("result");
        HashMap<Integer,Integer> data = new HashMap<Integer, Integer>();
        for(int i : result){
            if(!data.containsKey(i))
                data.put(i,1);
            else
                data.put(i,data.get(i)+1);
        }

        int value = (data.get(0)==null)?0:data.get(0);
        TextView un =(TextView) findViewById(R.id.unattempted);
        un.setText("UnAttempted : "+ value + "/" + result.size());

        value=(data.get(2)==null)?0:data.get(2);
        TextView co =(TextView) findViewById(R.id.correct);
        co.setText("Correct : "+ value + "/" + result.size());

        value=(data.get(1)==null)?0:data.get(1);
        TextView wr =(TextView) findViewById(R.id.wrong);
        wr.setText("Wrong : "+ value + "/" + result.size());

        constraintLayout = (ConstraintLayout) findViewById(R.id.constrainResult);
        c1.clone(constraintLayout);
        c2.clone(this, R.layout.activity_result_screen);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AutoTransition t = new AutoTransition();
                t.setDuration(3000);
                TransitionManager.beginDelayedTransition(constraintLayout,t);
                c2.applyTo(constraintLayout);

            }
        }, 500);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEmail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoTransition t = new AutoTransition();
                t.setDuration(5000);
                TransitionManager.beginDelayedTransition(constraintLayout,t);
                if(turn=!turn){
                    Log.v("fuck","hi");
                    c2.applyTo(constraintLayout);
                }
                else{
                    Log.v("fuck","bye");
                    c1.applyTo(constraintLayout);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(resultScreen.this, MainActivity.class));
    }
}
