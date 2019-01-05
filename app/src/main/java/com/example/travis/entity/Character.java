package com.example.travis.entity;

import com.example.travis.action.Action;
import com.example.travis.action.DefenseAction;
import com.example.travis.action.MovementAction;
import com.example.travis.action.RangedAttack;
import com.example.travis.battledisplay.Coordinates;
import com.example.travis.battledisplay.R;

import java.util.ArrayList;

public class Character extends EntityAE{
    private int attackAudio;
    private int damage;

    public Character(){
        super((R.drawable.icon_walk),(R.drawable.icon_cancel),"Blank",0,new Coordinates(0,0),10);
        attackAudio = 0;
        damage = 0;
    }

    public Character(int audio, String name, int imageId, int stockId, ArrayList<Action> actions, Coordinates location, int health,int energy){
        super(imageId,stockId,name,health,location,actions,energy);
        attackAudio = audio;
        damage = 0;
    }
    //PreMade Characters
    public void makeLink(){
        setName("Swordsmen");
        setImageId(R.drawable.swordsman_02);
        setStockId(R.drawable.swordsman_01);
        setHealth(5);
        setCurrent(new Coordinates(3,2));
        getActions().add(new MovementAction(this,(R.drawable.icon_walk),"Move",2,2));
        //actions.add(new Action((R.drawable.loz_hookshot),"Hookshot"));
        //actions.add(new Action((R.drawable.loz_bomb),"Bomb"));
        GameAnimation bow = new GameAnimation((R.drawable.swordsman_attack05),1,1,1500);
        getActions().add(new RangedAttack(this,(R.drawable.swordsman_bow),"Bow",1,1,bow,5));
        GameAnimation swordswing = new GameAnimation((R.drawable.swordsman_attack03), 2,2,1100);
        getActions().add(new RangedAttack(this,(R.drawable.swordsman_slash),"Slash",5,2,swordswing,1));
        attackAudio = (R.raw.linkattack0);
    }

    public void makeFox(){
        setCurrent(new Coordinates(3,4));
        setName("Fighter");
        setHealth(5);
        setImageId(R.drawable.fighter_02);
        setStockId(R.drawable.fighter_01);
        getActions().add(new MovementAction(this,(R.drawable.icon_walk),"Move",2,2));
        //actions.add(new Action((R.drawable.shine),"Shine"));
        //actions.add(new Action((R.drawable.fox_arwing),"Arwing"));
        //actions.add(new Action((R.drawable.fox_smartbomb),"Smart bomb"));
        GameAnimation CloseCombat = new GameAnimation((R.drawable.fighter_attack02), 1,1,1100);
        getActions().add(new RangedAttack(this,(R.drawable.fighter_closecombat),"Close Combat",2,1,CloseCombat,1));
        GameAnimation Hadouken = new GameAnimation((R.drawable.fighter_attack01), 1,1,1400);
        getActions().add(new RangedAttack(this,(R.drawable.fighter_fireball),"Fireball",3,2,Hadouken,3));
        attackAudio = (R.raw.foxattack0);
    }


    public int getAttackAudio() {
        return attackAudio;
    }

    public void setAttackAudio(int attackAudio) {
        this.attackAudio = attackAudio;
    }

}
