package com.example.travis.action;

import android.util.Log;

import com.example.travis.battledisplay.Battle;
import com.example.travis.battledisplay.GameStates;
import com.example.travis.entity.Entity;
import com.example.travis.tutorial.TutorialBattle;

public class MovementAction extends Action{
    private int maxUses;
    private int currentUses;

    public MovementAction(Entity user, int id, String name, int max, int uses) {
        super(user, id, name);
        this.currentUses = uses;
        this.maxUses = max;
    }

    @Override
    public boolean ActivateAction(Battle game) {
        if(currentUses > 0) {
            //this.setUses(this.getUses() - 1);
            boolean request = game.RequestStart(GameStates.Moving);
            if (request) {
                Log.d("Action", "Set game to Moving");
                game.getBoard().layoutMovements();
            }
            return request;
        }
        Log.d("Action","You ran out of uses this turn");
        return false;
    }

    @Override
    public boolean DeactivateAction(Battle game) {
        boolean request = game.RequestEnd(GameStates.Moving);
        if(request) {
            game.getBoard().undoLayout();
        }
        return request;
    }

    @Override
    public boolean ActivateAction(TutorialBattle game) {
        if(currentUses > 0) {
            //this.setUses(this.getUses() - 1);
            boolean request = game.RequestStart(GameStates.Moving);
            if (request) {
                Log.d("Action", "Set game to Moving");
                game.getBoard().layoutMovements();
            }
            return request;
        }
        Log.d("Action","You ran out of uses this turn");
        return false;
    }

    @Override
    public boolean DeactivateAction(TutorialBattle game) {
        boolean request = game.RequestEnd(GameStates.Moving);
        if(request) {
            game.getBoard().undoLayout();
        }
        return request;
    }


    public int getMaxUses() {
        return maxUses;
    }
    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public int getCurrentUses() {
        return currentUses;
    }
    public void setCurrentUses(int uses) {
        this.currentUses = uses;
    }
}
