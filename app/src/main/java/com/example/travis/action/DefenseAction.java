package com.example.travis.action;

import android.util.Log;

import com.example.travis.battledisplay.Battle;
import com.example.travis.entity.Enemy;
import com.example.travis.entity.Entity;
import com.example.travis.entity.EntityAE;
import com.example.travis.entity.EntityState;
import com.example.travis.entity.Turrent;
import com.example.travis.tutorial.TutorialBattle;

public class DefenseAction extends Action{
    private int defenseImg;
    private int standardImg;
    private int defPoints;
    private int cost;

    public DefenseAction(Entity user, int id, String name, int defenseImg, int standardImg, int defPoints, int cost) {
        super(user, id, name);
        this.defenseImg = defenseImg;
        this.defPoints = defPoints;
        this.standardImg = standardImg;
        this.cost = cost;
    }

    @Override
    public boolean ActivateAction(Battle game) {
        return Use(game);
    }

    @Override
    public boolean DeactivateAction(Battle game) {
        if(getUser().getState() != EntityState.Defending){
            return false;
        }
        endUse(game);
        Refund();
        return true;
    }

    @Override
    public boolean ActivateAction(TutorialBattle game) {
        return Use(game);
    }

    @Override
    public boolean DeactivateAction(TutorialBattle game) {
        if(getUser().getState() != EntityState.Defending){
            return false;
        }
        endUse(game);
        Refund();
        return true;
    }

    public boolean Use(Battle game){
        //First checks if Enemy is using this action
        if(getUser() instanceof EntityAE){
            if(((EntityAE)getUser()).getCurrentEnergy() < cost){
                Log.d("DefenseAction","Failed to use action");
                return false;
            }
            Log.d("DefenseAction", getUser().getName() + " used Block Ep went from "
                    + ((EntityAE) getUser()).getCurrentEnergy() + " to " + (((EntityAE) getUser()).getCurrentEnergy() - cost));
            ((EntityAE)getUser()).setCurrentEnergy(((EntityAE) getUser()).getCurrentEnergy() - cost);
        }
        game.getBoard().getTiles()[getUser().getCurrent().getX()][getUser().getCurrent().getY()].setImageResource(defenseImg);
        getUser().setImageId(defenseImg);
        getUser().setStateManipulate(this);
        getUser().setState(EntityState.Defending);
        getUser().setDefense(defPoints);
        Log.d("DefenseAction", getUser().getName() + " switched to a defensive state");
        return true;
    }
    public void endUse(Battle game){
        Log.d("DefenseAction", getUser().getName() + " switched to a normal state");
        game.getBoard().getTiles()[getUser().getCurrent().getX()][getUser().getCurrent().getY()].setImageResource(standardImg);
        getUser().setImageId(standardImg);
        getUser().setStateManipulate(null);
        getUser().setState(EntityState.Neutral);
        getUser().setDefense(0);
    }

    public boolean Use(TutorialBattle game){
        //First checks if Enemy is using this action
        if(getUser() instanceof EntityAE){
            if(((EntityAE)getUser()).getCurrentEnergy() < cost){
                Log.d("DefenseAction","Failed to use action");
                return false;
            }
            Log.d("DefenseAction", getUser().getName() + " used Block Ep went from "
                    + ((EntityAE) getUser()).getCurrentEnergy() + " to " + (((EntityAE) getUser()).getCurrentEnergy() - cost));
            ((EntityAE)getUser()).setCurrentEnergy(((EntityAE) getUser()).getCurrentEnergy() - cost);
        }
        game.getBoard().getTiles()[getUser().getCurrent().getX()][getUser().getCurrent().getY()].setImageResource(defenseImg);
        getUser().setImageId(defenseImg);
        getUser().setStateManipulate(this);
        getUser().setState(EntityState.Defending);
        getUser().setDefense(defPoints);
        Log.d("DefenseAction", getUser().getName() + " switched to a defensive state");
        return true;
    }
    public void endUse(TutorialBattle game){
        Log.d("DefenseAction", getUser().getName() + " switched to a normal state");
        game.getBoard().getTiles()[getUser().getCurrent().getX()][getUser().getCurrent().getY()].setImageResource(standardImg);
        getUser().setImageId(standardImg);
        getUser().setStateManipulate(null);
        getUser().setState(EntityState.Neutral);
        getUser().setDefense(0);
    }

    public void Refund(){
        Log.d("Refund","gave back energy to " + getUser().getName() +
                ". EP went from " + ((EntityAE)getUser()).getCurrentEnergy() +
                " to " + (((EntityAE) getUser()).getCurrentEnergy() + cost));
        ((EntityAE)getUser()).setCurrentEnergy(((EntityAE) getUser()).getCurrentEnergy() + cost);
    }


    public int getDefenseImg() {
        return defenseImg;
    }
    public void setDefenseImg(int defenseImg) {
        this.defenseImg = defenseImg;
    }

    public int getDefPoints() {
        return defPoints;
    }
    public void setDefPoints(int defPoints) {
        this.defPoints = defPoints;
    }

    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getStandardImg() {
        return standardImg;
    }
    public void setStandardImg(int standardImg) {
        this.standardImg = standardImg;
    }
}
