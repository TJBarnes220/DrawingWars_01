package com.example.travis.entity;

import android.util.Log;

import com.example.travis.action.Action;
import com.example.travis.action.GroupHeal;
import com.example.travis.action.NarrowAttack;
import com.example.travis.battledisplay.Battle;
import com.example.travis.battledisplay.Coordinates;
import com.example.travis.battledisplay.R;
import com.example.travis.tutorial.TutorialBattle;

import java.util.ArrayList;

public class HealBot extends Enemy {
    public HealBot(int img, int stock, String name, int health, Coordinates current, int attackAnimation){
        super(img,stock,name,health,current);
        GameAnimation heal = new GameAnimation(attackAnimation,1,1,1100);
        Action main = new GroupHeal(this, (R.drawable.icon_move),"Heal Friends",5,1, heal);
        setMain(main);
    }
    @Override
    public void behavior(Battle game) {
        //Log.d("HealBot","Start Behavior);
        if(getMain() instanceof GroupHeal){
           // Log.d("HealBot","This is where " + getName() + " would heal " + friends.get(i).getName());
            ArrayList<Entity> friends = new ArrayList<Entity>();
            ArrayList<Entity> inRange = ((GroupHeal) getMain()).findTargets(game.getAllEntities());
            for(int i = 0; i < inRange.size(); i++){
                if(inRange.get(i) instanceof Enemy & !(inRange.get(i).getName().equals(getName()))){
                    friends.add(inRange.get(i));
                }
            }
            for (int i = 0; i < friends.size(); i++){
                game.heal(friends.get(i),((GroupHeal) getMain()).getRecovery());
            }
        }
    }
    public void behavior(TutorialBattle game) {
        if(getMain() instanceof GroupHeal){
            ArrayList<Entity> friends = new ArrayList<Entity>();
            ArrayList<Entity> inRange = ((GroupHeal) getMain()).findTargets(game.getAllEntities());
            for(int i = 0; i < inRange.size(); i++){
                if(inRange.get(i) instanceof Enemy){
                    friends.add(inRange.get(i));
                }
            }
            for (int i = 0; i < friends.size(); i++){
                Log.d("HealBot","This is where " + getName() + " would heal " + friends.get(i).getName());
            }
        }
    }
}
