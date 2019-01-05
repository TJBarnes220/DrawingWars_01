package com.example.travis.tutorial;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.travis.battledisplay.Battle;
import com.example.travis.battledisplay.MainActivity;
import com.example.travis.battledisplay.R;

import java.util.ArrayList;

public class TutorialActivity extends AppCompatActivity {
    int currentHint;
    ArrayList<Tip> hints;
    TutorialBattle game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        TextView header = findViewById(R.id.header);
        View root = findViewById(R.id.header).getRootView();
        root.setBackgroundResource(R.drawable.blackwhitenotebook);

        currentHint = 0;
        hints = new ArrayList<>();
        hints.add(new Tip("In this turn-based game, you will use your team to defeat all the bad guys. " +
                "Feel free to click on your characters on the right to move them around.",(R.drawable.swordsman_02)));
        hints.add(new Tip("Each of your characters have Health Points(HP) and Energy Points(EP). " +
                "If they run out of HP you can no longer use them. Your EP will increase every turn.",(R.drawable.display)));
        hints.add(new Tip("Now we have Actions. This first one is the Move action. This shows you how " +
                "many times your characters can move this turn.",(R.drawable.display_move)));
        hints.add(new Tip("Your Attack Actions cost EP. From Left to Right the numbers represent the" +
                " damage done by the action and the EP needed to use it. Each move has a different range of attack so test them out.", (R.drawable.display_action)));
        hints.add(new Tip("Enemies attack once you end your turn so make sure to end in a defensive manner. " +
                "Some Characters can switch on and off in a block state to soak up damage. Try it out!",(R.drawable.display_movement)));
        hints.add(new Tip("Enemies can block too. Unless you love punching walls you should try to break their guard. " +
                "Strong moves like Fighter's Force Palm can break an opponent's guard, use that to your advantage. ",(R.drawable.display_movement)));
        hints.add(new Tip("With that out of the way let's see if you can win. " +
                "If you defeat everyone on the left you'll be taken back to the home screen",(R.drawable.display_movement)));
        TextView Hint = findViewById(R.id.Hint);
        Hint.setText(hints.get(currentHint).getText());
        ImageView previous = findViewById(R.id.previousHint);
        previous.setAlpha(0f);
        previous.setClickable(false);
        Conditions(previous);

    }
    public void onClick(View view) {
        Intent start = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        //startActivity(start);
    }

    public void next(View view){
        currentHint++;
        Conditions(view);
        ImageView next = findViewById(R.id.nextHint);
        TextView Hint = findViewById(R.id.Hint);
        Hint.setText(hints.get(currentHint).getText());
        if(currentHint >= hints.size() - 1){
            next.setAlpha(0f);
            next.setClickable(false);
        }
        else{
            next.setAlpha(1f);
        }
        if(currentHint > 0){
            ImageView previous = findViewById(R.id.previousHint);
            previous.setAlpha(1f);
            previous.setClickable(true);
        }
    }

    public void previous(View view){
        currentHint--;
        Conditions(view);
        ImageView previous = findViewById(R.id.previousHint);
        TextView Hint = findViewById(R.id.Hint);
        Hint.setText(hints.get(currentHint).getText());
        if(currentHint <= 0){
            previous.setAlpha(0f);
            previous.setClickable(false);
        }
        else{
            previous.setAlpha(1f);
        }
        if(currentHint < hints.size() - 1){
            ImageView next = findViewById(R.id.nextHint);
            next.setAlpha(1f);
            next.setClickable(true);
        }
    }
    public void Conditions(View view){
        LinearLayout display = findViewById(R.id.linearLayout);
        Button reset = findViewById(R.id.Reset);
        Button turn = findViewById(R.id.turnBtn);
        switch (currentHint){
            case(0):
                display.setAlpha(0f);
                reset.setAlpha(0f);
                reset.setClickable(false);
                turn.setAlpha(0f);
                turn.setClickable(false);
                reset(view);
                break;
            case (1):
                display.setAlpha(1f);
                reset.setAlpha(0f);
                reset.setClickable(false);
                turn.setAlpha(0f);
                turn.setClickable(false);
                reset(view);
                break;
            case(2):
                display.setAlpha(1f);
                reset.setAlpha(1f);
                reset.setClickable(true);
                turn.setAlpha(0f);
                reset(view);
                break;
            case(3):
                display.setAlpha(1f);
                reset.setAlpha(1f);
                reset.setClickable(true);
                turn.setAlpha(0f);
                reset(view);
                break;
            case(4):
                display.setAlpha(1f);
                reset.setAlpha(1f);
                reset.setClickable(true);
                turn.setAlpha(1f);
                turn.setClickable(true);
                reset(view);
                break;

                default:
                    display.setAlpha(1f);
                    reset.setAlpha(1f);
                    reset.setClickable(true);
                    turn.setAlpha(1f);
                    turn.setClickable(true);
                    reset(view);
                    break;
        }
    }

    public void reset(View view){
        game = new TutorialBattle((TableLayout)findViewById(R.id.gameBoard), this,
                (ImageView)findViewById(R.id.headshot),(TextView)findViewById (R.id.selectedHealth),
                (TextView)findViewById(R.id.selectedName),(RecyclerView)findViewById(R.id.actionList),
                (Button)findViewById(R.id.turnBtn),
                (ImageView) findViewById(R.id.imageView3), (ConstraintLayout)findViewById(R.id.main),
                (TextView)findViewById(R.id.turnMessage), currentHint);
    }
}
