package com.example.travis.entity;

import com.example.travis.action.Action;
import com.example.travis.action.DefenseAction;
import com.example.travis.action.NarrowAttack;
import com.example.travis.battledisplay.Battle;
import com.example.travis.battledisplay.Coordinates;
import com.example.travis.battledisplay.R;
import com.example.travis.tutorial.TutorialBattle;

public class BasicTank extends Enemy {

    public BasicTank(int img, int stock, String name, int health, Coordinates current, int attackAnimation){
        super(img,stock,name,health,current);
        GameAnimation attack = new GameAnimation(attackAnimation,1,1,1350);
        Action main = new DefenseAction(this, (R.drawable.icon_move),"Block",(R.drawable.enemy_trash_blocking),img,4,1);
        setMain(main);
    }
    @Override
    public void behavior(Battle game) {
        if(getMain() instanceof DefenseAction){
            ((DefenseAction) getMain()).Use(game);
        }
    }
    @Override
    public void behavior(TutorialBattle game) {
        if(getMain() instanceof DefenseAction){
            ((DefenseAction) getMain()).Use(game);
        }
    }

    public void refresh(Battle game){
        if(getState() == EntityState.Defending){
            if(getMain() instanceof DefenseAction){
                ((DefenseAction) getMain()).endUse(game);
            }
        }
    }

    public void refresh(TutorialBattle game){
        if(getState() == EntityState.Defending){
            if(getMain() instanceof DefenseAction){
                ((DefenseAction) getMain()).endUse(game);
            }
        }
    }
}
