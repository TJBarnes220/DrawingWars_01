package com.example.travis.entity;

import com.example.travis.action.Action;
import com.example.travis.battledisplay.Battle;
import com.example.travis.battledisplay.Coordinates;
import com.example.travis.battledisplay.R;
import com.example.travis.tutorial.TutorialBattle;

import java.util.ArrayList;

public abstract class Enemy extends Entity {
    private ArrayList<Action> actions;
    private Action main;
    public Enemy(){
        super((R.drawable.icon_move),(R.drawable.icon_cancel),"Blank",0,new Coordinates(0,0));
        actions = new ArrayList<Action>();
    }

    public Enemy(int img, int stock, String name, int health, Coordinates current) {
        super(img, stock, name, health, current);
        actions = new ArrayList<Action>();
    }

    public void makeRidley(){
        setCurrent(new Coordinates(0,2));
        setName("Ridley");
        setHealth(10);
        //setImageId(R.drawable.ridley);
        //setStockId(R.drawable.ridleystock);
    }
    public ArrayList<Action> getActions() {
        return actions;
    }
    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
    public Action getMain() {
        return main;
    }
    public void setMain(Action main) {
        this.main = main;
    }

    public abstract void behavior(Battle game);
    public abstract void behavior(TutorialBattle game);
}
