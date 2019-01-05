package com.example.travis.battledisplay;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.travis.action.DefenseAction;
import com.example.travis.action.ForcePalm;
import com.example.travis.entity.BasicTank;
import com.example.travis.entity.Character;
import com.example.travis.entity.Direction;
import com.example.travis.entity.Entity;
import com.example.travis.entity.GameAnimation;
import com.example.travis.entity.HealBot;
import com.example.travis.entity.Turrent;
import com.example.travis.tutorial.TutorialActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View root = findViewById(R.id.startBtn).getRootView();
        root.setBackgroundResource(R.drawable.blackwhitenotebook);
        randomAnimation(root);
    }
    public void randomAnimation(View v){
        Random rNumber = new Random();
        int choice = rNumber.nextInt(7) + 1;
        //Used to text animations
        //choice = 2;
        ImageView animation = findViewById(R.id.imageView);
        switch(choice){
            case(1):
                animation.setImageResource(R.drawable.swordsman_attack01);
                break;
            case(2):
                animation.setImageResource(R.drawable.swordsman_attack05);
                break;
            case(3):
                animation.setImageResource(R.drawable.fighter_attack01);
                break;
            case(4):
                animation.setImageResource(R.drawable.fighter_attack02);
                break;
            case(5):
                animation.setImageResource(R.drawable.enemy_dummy_attack01);
                break;
            case(6):
                animation.setImageResource(R.drawable.enemy_healbot_heal01);
                break;
            case(7):
                animation.setImageResource(R.drawable.fighter_attack03);
                break;
        }
        ((AnimationDrawable) animation.getDrawable()).setOneShot(false);
        ((AnimationDrawable) animation.getDrawable()).start();
    }
    public void onClick(View v){
        Intent start;
        if(((Button)v).getText().toString().equals("Start")){
            v.setClickable(false);
            Log.d("Main","Scappppy Button");
            ArrayList<View> items = new ArrayList<View>();
            items.add(findViewById(R.id.Title));
            items.add(findViewById(R.id.imageView));
            items.add(findViewById(R.id.startBtn));
            items.add(findViewById(R.id.tutorialBtn));
            items.add(findViewById(R.id.settingsBtn));
            for(int i = 0; i < items.size(); i++){
                items.get(i).animate().alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        setContentView(R.layout.level_select);
                    }
                });
            }
            return;
        }
        else if(((Button)v).getText().toString().equals("Tutorial")){
            start = new Intent(this, TutorialActivity.class);
        }
        else if(((Button)v).getText().toString().equals("Level 1")){
            Character Fox = new Character();
            Fox.makeFox();
            GameAnimation strike = new GameAnimation((R.drawable.fighter_attack03),1,1,1300);
            Fox.getActions().add(new ForcePalm(Fox,(R.drawable.fighter_forcepalm),"Force Palm",4,5,strike,1));
            Character Link = new Character();
            Link.makeLink();
            Link.getActions().add(new DefenseAction(Link,(R.drawable.swordsman_block),"Block",(R.drawable.swordsman_03),(R.drawable.swordsman_02),5,1));
            Turrent Dummy1 = new Turrent(R.drawable.enemy_dummy_02,R.drawable.enemy_dummy_01,"Dummy",5,new Coordinates(0,0),Direction.RIGHT,(R.drawable.enemy_dummy_attack01));
            Turrent Dummy2 = new Turrent(R.drawable.enemy_dummy_02,R.drawable.enemy_dummy_01,"Lummy",5,new Coordinates(0,2),Direction.RIGHT,(R.drawable.enemy_dummy_attack01));
            Turrent Dummy3 = new Turrent(R.drawable.enemy_dummy_02,R.drawable.enemy_dummy_01,"Rummy",5,new Coordinates(0,4),Direction.RIGHT,(R.drawable.enemy_dummy_attack01));
            ArrayList<Entity> all = new ArrayList<Entity>();
            all.add(Fox);
            all.add(Link);
            all.add(Dummy1);
            all.add(Dummy2);
            all.add(Dummy3);
            setContentView(R.layout.battle_layout);
            Battle game = new Battle((TableLayout)findViewById(R.id.gameBoard), this,
                    (ImageView)findViewById(R.id.headshot),(TextView)findViewById (R.id.selectedHealth),
                    (TextView)findViewById(R.id.selectedName),(RecyclerView)findViewById(R.id.actionList),
                    (Button)findViewById(R.id.turnBtn), (ConstraintLayout)findViewById(R.id.main),
                    (TextView)findViewById(R.id.turnMessage),all);
            return;
        }
        else if(((Button)v).getText().toString().equals("Level 2")){
            Character Fox = new Character();
            Fox.makeFox();
            GameAnimation strike = new GameAnimation((R.drawable.fighter_attack03),1,1,1300);
            Fox.getActions().add(new ForcePalm(Fox,(R.drawable.fighter_forcepalm),"Force Palm",4,5,strike,1));
            Fox.setCurrent(new Coordinates(3,3));
            Character Link = new Character();
            Link.makeLink();
            Link.getActions().add(new DefenseAction(Link,(R.drawable.swordsman_block),"Block",(R.drawable.swordsman_03),(R.drawable.swordsman_02),5,1));
            Link.setCurrent(new Coordinates(3,1));
            HealBot healy = new HealBot(R.drawable.enemy_healbot_02,R.drawable.enemy_healbot_02,"H.Bot",5,new Coordinates(0,2),(R.drawable.enemy_healbot_heal01));
            Turrent Dummy1 = new Turrent(R.drawable.enemy_dummy_02,R.drawable.enemy_dummy_01,"Dummy",10,new Coordinates(0,1),Direction.RIGHT,(R.drawable.enemy_dummy_attack01));
            Turrent Dummy2 = new Turrent(R.drawable.enemy_dummy_02,R.drawable.enemy_dummy_01,"Lummy",10,new Coordinates(0,3),Direction.RIGHT,(R.drawable.enemy_dummy_attack01));
            BasicTank LeftOvers = new BasicTank(R.drawable.enemy_trash_02,R.drawable.enemy_trash_02,"LeftOvers",20,new Coordinates(1,2),(R.drawable.enemy_dummy_attack01));
            ArrayList<Entity> all = new ArrayList<Entity>();
            all.add(Fox);
            all.add(Link);
            all.add(Dummy1);
            all.add(Dummy2);
            all.add(LeftOvers);
            all.add(healy);
            setContentView(R.layout.battle_layout);
            Battle game = new Battle((TableLayout)findViewById(R.id.gameBoard), this,
                    (ImageView)findViewById(R.id.headshot),(TextView)findViewById (R.id.selectedHealth),
                    (TextView)findViewById(R.id.selectedName),(RecyclerView)findViewById(R.id.actionList),
                    (Button)findViewById(R.id.turnBtn), (ConstraintLayout)findViewById(R.id.main),
                    (TextView)findViewById(R.id.turnMessage),all);
            return;
        }
        else{
            return;
        }
        startActivity(start);
    }
    public void back(final View v){
        v.setClickable(false);
        ArrayList<View> items = new ArrayList<View>();
        items.add(findViewById(R.id.Title));
        items.add(findViewById(R.id.back));
        items.add(findViewById(R.id.level_01));
        items.add(findViewById(R.id.level_02));
        for(int i = 0; i < items.size(); i++){
            items.get(i).animate().alpha(0f).setDuration(1000).withEndAction(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.activity_main);
                    randomAnimation(v);
                }
            });
        }
    }
}
