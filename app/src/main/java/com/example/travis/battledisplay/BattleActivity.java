package com.example.travis.battledisplay;

import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class BattleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_layout);
        View root = findViewById(R.id.turnBtn).getRootView();
        root.setBackgroundResource(R.drawable.blackwhitenotebook);
        /*Battle game = new Battle((TableLayout)findViewById(R.id.gameBoard), this,
                (ImageView)findViewById(R.id.headshot),(TextView)findViewById (R.id.selectedHealth),
                (TextView)findViewById(R.id.selectedName),(RecyclerView)findViewById(R.id.actionList),
                (Button)findViewById(R.id.turnBtn), (ConstraintLayout)findViewById(R.id.main),
                (TextView)findViewById(R.id.turnMessage));*/
    }
}
