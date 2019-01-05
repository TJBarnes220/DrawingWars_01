package com.example.travis.entity;

import com.example.travis.action.Action;
import com.example.travis.action.NarrowAttack;
import com.example.travis.battledisplay.Battle;
import com.example.travis.battledisplay.Coordinates;
import com.example.travis.battledisplay.R;
import com.example.travis.tutorial.TutorialBattle;

import java.util.ArrayList;

public class Turrent extends Enemy {
    private Direction facing;
    public Turrent(int img, int stock, String name, int health, Coordinates current, Direction facing, int attackAnimation){
        super(img,stock,name,health,current);
        this.facing = facing;
        GameAnimation attack = new GameAnimation(attackAnimation,1,1,1350);
        Action main = new NarrowAttack(this, (R.drawable.icon_move),"Rock throw",1,1,attack,5,Direction.RIGHT);
        setMain(main);
    }
    @Override
    public void behavior(Battle game) {
        //fires every turn
        if(getMain() instanceof NarrowAttack){
            ArrayList<Character> targets = new ArrayList<Character>();
            ArrayList<Entity> all = ((NarrowAttack) getMain()).findTargets(game.getAllEntities());
            for(int i = 0; i < all.size(); i++){
                if(all.get(i) instanceof Character){
                    targets.add((Character) all.get(i));
                }
            }
            for (int i = 0; i < targets.size(); i++){
                game.attack(targets.get(i),((NarrowAttack) getMain()).getAttackDamage());
            }
        }
    }
    @Override
    public void behavior(TutorialBattle game) {
        //fires every turn
        if(getMain() instanceof NarrowAttack){
            ArrayList<Character> targets = new ArrayList<Character>();
            ArrayList<Entity> all = ((NarrowAttack) getMain()).findTargets(game.getAllEntities());
            for(int i = 0; i < all.size(); i++){
                if(all.get(i) instanceof Character){
                    targets.add((Character) all.get(i));
                }
            }
            for (int i = 0; i < targets.size(); i++){
                game.attack(targets.get(i),((NarrowAttack) getMain()).getAttackDamage());
            }
        }
    }
}
