package com.example.travis.action;

import android.util.Log;

import com.example.travis.battledisplay.Battle;
import com.example.travis.battledisplay.GameStates;
import com.example.travis.entity.Entity;
import com.example.travis.entity.EntityAE;
import com.example.travis.entity.GameAnimation;
import com.example.travis.tutorial.TutorialBattle;

import java.util.ArrayList;

public abstract class HealAction extends Action{
    private int recovery;
    private int cost;
    private GameAnimation healAnimation;

    public HealAction(Entity user, int id, String name, int recovery, int cost, GameAnimation healAnimation) {
        super(user, id, name);
        this.recovery = recovery;
        this.cost = cost;
        this.healAnimation = healAnimation;
    }
    public boolean ActivateAction(Battle game){
        if(((EntityAE)getUser()).getCurrentEnergy() >= cost) {
            boolean request = game.RequestStart(GameStates.Targeting);
            if(request) {
                Log.d("Action", "Set game to Targeting");
                game.getBoard().showTargets(this.findTargets(game.getBoard().getAllEntities()));
            }
            return request;
        }
        Log.d("Action","Not enough energy for this attack");
        return false;
    }
    public boolean DeactivateAction(Battle game){
        boolean request = game.RequestEnd(GameStates.Targeting);
        if(request) {
            game.getBoard().undoTargets(this.findTargets(game.getBoard().getAllEntities()));
        }
        return request;
    }
    public boolean ActivateAction(TutorialBattle game){
        if(((EntityAE)getUser()).getCurrentEnergy() >= cost) {
            boolean request = game.RequestStart(GameStates.Targeting);
            if(request) {
                Log.d("Action", "Set game to Targeting");
                game.getBoard().showTargets(this.findTargets(game.getBoard().getAllEntities()));
            }
            return request;
        }
        Log.d("Action","Not enough energy for this attack");
        return false;
    }
    public boolean DeactivateAction(TutorialBattle game){
        boolean request = game.RequestEnd(GameStates.Targeting);
        if(request) {
            game.getBoard().undoTargets(this.findTargets(game.getBoard().getAllEntities()));
        }
        return request;
    }

    public int getRecovery() {
        return recovery;
    }
    public void setRecovery(int recovery) {
        this.recovery = recovery;
    }
    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }
    public GameAnimation getHealAnimation() {
        return healAnimation;
    }
    public abstract ArrayList<Entity> findTargets(ArrayList<Entity> allEntities);
}
