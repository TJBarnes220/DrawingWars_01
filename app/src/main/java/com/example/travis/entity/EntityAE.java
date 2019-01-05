package com.example.travis.entity;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.travis.action.Action;
import com.example.travis.action.DefenseAction;
import com.example.travis.action.MovementAction;
import com.example.travis.battledisplay.Battle;
import com.example.travis.battledisplay.Coordinates;
import com.example.travis.tutorial.TutorialBattle;

import java.util.ArrayList;

/**
 * A step down from an Entity. AE stands for Action/Energy
 * meaning these Entities have actions and energy to used
 * said actions. All entities start with a certain amount
 * of energy but will slowly increase until they reach their
 * limit (maxEnergy). Although this is excluding potential
 * bonus and removed energy.
 */
public class EntityAE extends Entity {
    private ArrayList<Action> actions;
    int maxEnergy;
    int currentEnergy;
    int bonusEnergy;
    int removeEnergy;

    /**
     * Basic constructor for an EntityAE without actions.
     * @param img
     * @param stock
     * @param name
     * @param health
     * @param current
     * @param maxEnergy
     */
    public EntityAE(int img, int stock, String name, int health, Coordinates current,int maxEnergy) {
        super(img, stock, name, health, current);
        this.maxEnergy = maxEnergy;
        currentEnergy = 1;
        bonusEnergy = 0;
        removeEnergy = 0;
        actions = new ArrayList<Action>();
    }

    /**
     * Constructor for an EntityAE with actions.
     * @param img
     * @param stock
     * @param name
     * @param health
     * @param current
     * @param actions
     * @param maxEnergy
     */
    public EntityAE(int img, int stock, String name, int health, Coordinates current,ArrayList<Action> actions,int maxEnergy) {
        super(img, stock, name, health, current);
        this.maxEnergy = maxEnergy;
        this.actions = actions;
        currentEnergy = 1;
        bonusEnergy = 0;
        removeEnergy = 0;
    }

    /**
     * This refreshes the Entity restoring energy and action uses.
     * Base current energy is based off of the current turn number
     * up until it reaches the Entities max.
     * @param currentTurn
     */
    public void refresh(int currentTurn, Battle game) {
        if(currentTurn >= maxEnergy){
            currentEnergy = maxEnergy;
        }
        else{
            currentEnergy = currentTurn;
        }
        currentEnergy += (bonusEnergy - removeEnergy);
        for(int i = 0; i < actions.size(); i++){
            if(actions.get(i) instanceof MovementAction){
                ((MovementAction) actions.get(i)).setCurrentUses(((MovementAction) actions.get(i)).getMaxUses());
            }
        }
        Log.d("Refresh",getName() + " current state is " + getState().toString());
        if(getState() != EntityState.Neutral & getStateManipulate() != null){
            ((DefenseAction)getStateManipulate()).endUse(game);
        }
    }
    public void refresh(int currentTurn, TutorialBattle game) {
        if(currentTurn >= maxEnergy){
            currentEnergy = maxEnergy;
        }
        else{
            currentEnergy = currentTurn;
        }
        currentEnergy += (bonusEnergy - removeEnergy);
        for(int i = 0; i < actions.size(); i++){
            if(actions.get(i) instanceof MovementAction){
                ((MovementAction) actions.get(i)).setCurrentUses(((MovementAction) actions.get(i)).getMaxUses());
            }
        }
        Log.d("Refresh",getName() + " current state is " + getState().toString());
        if(getState() != EntityState.Neutral & getStateManipulate() != null){
            ((DefenseAction)getStateManipulate()).endUse(game);
        }
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }
    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }
    public void setCurrentEnergy(int currentEnergy) {
        this.currentEnergy = currentEnergy;
    }

    public int getBonusEnergy() {
        return bonusEnergy;
    }
    public void setBonusEnergy(int bonusEnergy) {
        this.bonusEnergy = bonusEnergy;
    }

    public int getRemoveEnergy() {
        return removeEnergy;
    }
    public void setRemoveEnergy(int removeEnergy) {
        this.removeEnergy = removeEnergy;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

}
