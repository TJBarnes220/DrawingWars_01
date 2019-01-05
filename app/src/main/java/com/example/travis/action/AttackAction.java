package com.example.travis.action;

import android.util.Log;

import com.example.travis.battledisplay.Battle;
import com.example.travis.battledisplay.GameStates;
import com.example.travis.entity.Entity;
import com.example.travis.entity.EntityAE;
import com.example.travis.entity.GameAnimation;
import com.example.travis.tutorial.TutorialBattle;

import java.util.ArrayList;

/**
 * One of the basic categories of an Action. Attack actions
 * uniquely take in a damage value and a cost value.
 */
public abstract class AttackAction extends Action {
    private int attackDamage;
    private int cost;
    private GameAnimation attackAnimation;

    public AttackAction(Entity user, int id, String name, int damage, int cost, GameAnimation attackAnimation) {
        super(user, id, name);
        this.attackDamage = damage;
        this.cost = cost;
        this.attackAnimation = attackAnimation;
    }

    /**
     * Checks to see if the action can be used based on the user's
     * current energy. If it can be used, it checks Request Start
     * and shows the potential targets found by findTargets.
     * @param game the game where these actions take place
     * @return
     */
    @Override
    public boolean ActivateAction(Battle game) {
        if(((EntityAE)getUser()).getCurrentEnergy() >= cost) {
            boolean request = game.RequestStart(GameStates.Attacking);
            if(request) {
                Log.d("Action", "Set game to Attack");
                game.getBoard().showTargets(this.findTargets(game.getBoard().getAllEntities()));
            }
            return request;
        }
        Log.d("Action","Not enough energy for this attack");
        return false;
    }

    /**
     *
     * @param game
     * @return
     */
    @Override
    public boolean DeactivateAction(Battle game) {
        boolean request = game.RequestEnd(GameStates.Attacking);
        if(request) {
            game.getBoard().undoTargets(this.findTargets(game.getBoard().getAllEntities()));
        }
        return request;
    }

    @Override
    public boolean ActivateAction(TutorialBattle game) {
        if(((EntityAE)getUser()).getCurrentEnergy() >= cost) {
            boolean request = game.RequestStart(GameStates.Attacking);
            if(request) {
                Log.d("Action", "Set game to Attack");
                game.getBoard().showTargets(this.findTargets(game.getBoard().getAllEntities()));
            }
            return request;
        }
        Log.d("Action","Not enough energy for this attack");
        return false;
    }

    /**
     *
     * @param game
     * @return
     */
    @Override
    public boolean DeactivateAction(TutorialBattle game) {
        boolean request = game.RequestEnd(GameStates.Attacking);
        if(request) {
            game.getBoard().undoTargets(this.findTargets(game.getBoard().getAllEntities()));
        }
        return request;
    }

    public int getAttackDamage() {
        return attackDamage;
    }
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }

    public GameAnimation getAttackAnimation() {
        return attackAnimation;
    }

    public void setAttackAnimation(GameAnimation attackAnimation) {
        this.attackAnimation = attackAnimation;
    }

    public abstract ArrayList<Entity> findTargets(ArrayList<Entity> allEntities);
}
