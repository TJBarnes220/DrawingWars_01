package com.example.travis.action;

import android.util.Log;

import com.example.travis.battledisplay.Battle;
import com.example.travis.entity.Enemy;
import com.example.travis.entity.Entity;
import com.example.travis.entity.EntityState;
import com.example.travis.entity.GameAnimation;
import com.example.travis.tutorial.TutorialBattle;

public class ForcePalm extends RangedAttack {
    public ForcePalm(Entity user, int id, String name, int damage, int cost, GameAnimation attackAnimation, int range) {
        super(user, id, name, damage, cost, attackAnimation, range);
    }

    public void Use(Battle game, Entity Target){
        if(Target.getState() == EntityState.Defending){
            Log.d("ForcePalm", Target.getName() + " guard broke back to normal");
            int originalImg = ((DefenseAction)Target.getStateManipulate()).getStandardImg();
            game.getBoard().getTiles()[Target.getCurrent().getX()][Target.getCurrent().getY()].setImageResource(originalImg);
            Target.setImageId(originalImg);
            Target.setState(EntityState.Neutral);
            Target.setDefense(0);
        }
    }

    public void Use(TutorialBattle game, Entity Target){
        if(Target.getState() == EntityState.Defending){
            Log.d("ForcePalm", Target.getName() + " guard broke back to normal");
            int originalImg = ((DefenseAction)Target.getStateManipulate()).getStandardImg();
            game.getBoard().getTiles()[Target.getCurrent().getX()][Target.getCurrent().getY()].setImageResource(originalImg);
            Target.setImageId(originalImg);
            Target.setState(EntityState.Neutral);
            Target.setDefense(0);
        }
    }
}
