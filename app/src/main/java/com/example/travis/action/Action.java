package com.example.travis.action;

import com.example.travis.battledisplay.Battle;
import com.example.travis.entity.Entity;
import com.example.travis.tutorial.TutorialBattle;

/**
 * The Basic structure of an Action. Actions can range from
 * moving characters to attacking other characters. Each action is
 * assigned its user, an image id, and a name. All subclass actions
 * will need to activate and deactivate in their own ways so this
 * class gives them those abstract methods.
 */
public abstract class Action {
    private Entity user;
    private int id;
    private String name;
    //private int maxUses;
    //private int uses;
    //Number of uses
    //Action type?????
    //private int audio;

    public Action(Entity user, int id, String name){
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public Entity getUser() {
        return user;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public abstract boolean ActivateAction(Battle game);
    public abstract boolean DeactivateAction(Battle game);
    public abstract boolean ActivateAction(TutorialBattle game);
    public abstract boolean DeactivateAction(TutorialBattle game);
}
