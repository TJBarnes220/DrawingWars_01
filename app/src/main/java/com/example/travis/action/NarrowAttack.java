package com.example.travis.action;

import android.util.Log;

import com.example.travis.battledisplay.Coordinates;
import com.example.travis.entity.Direction;
import com.example.travis.entity.Entity;
import com.example.travis.entity.GameAnimation;

import java.util.ArrayList;

import static com.example.travis.entity.Direction.*;

public class NarrowAttack extends AttackAction {
    private int attackRange;
    private Direction path;

    public NarrowAttack(Entity user, int id, String name, int damage, int cost, GameAnimation attackAnimation, int range, Direction path) {
        super(user, id, name, damage, cost, attackAnimation);
        this.attackRange = range;
        this.path = path;
    }

    public ArrayList<Entity> findTargets(ArrayList<Entity> allEntities){
        //Checks "range" spaces in all four directions
        Coordinates userLocation = this.getUser().getCurrent();
        Coordinates current = userLocation;
        Boolean found = false;
        ArrayList<Entity> potentialTargets = new ArrayList<Entity>();
        for (int i = 0; i < attackRange; i++) {
            //Uses switch to decide which direction to move in
            switch(path){
                case UP:
                    current = current.getNorth();
                    break;
                case RIGHT:
                    current = current.getEast();
                    break;
                case DOWN:
                    current = current.getSouth();
                    break;
                case LEFT:
                    current = current.getWest();
                    break;
            }
            //System.out.println(allEntities.size());
            for (int j = 0; j < allEntities.size(); j++) {
                //allEntities.get(j).getCurrent().printLocation();
                if (current.equals(allEntities.get(j).getCurrent())) {
                    Log.d("EntityList",allEntities.get(j).getName() + " has been targeted");
                    potentialTargets.add(allEntities.get(j));
                    found = true;
                    break;
                }
            }
            //If nothing was found continue to move in this direction
            //If something was found break and stop looking in this direction
            if(found == true){
                break;
            }
        }
        return potentialTargets;
    }

}
