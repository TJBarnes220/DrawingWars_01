package com.example.travis.action;

import android.util.Log;

import com.example.travis.battledisplay.Coordinates;
import com.example.travis.entity.Entity;
import com.example.travis.entity.GameAnimation;

import java.util.ArrayList;

public class RangedAttack extends AttackAction{
    private int attackRange;

    public RangedAttack(Entity user, int id, String name, int damage, int cost, GameAnimation attackAnimation, int range) {
        super(user, id, name, damage, cost, attackAnimation);
        this.attackRange = range;
    }

    public ArrayList<Entity> findTargets(ArrayList<Entity> allEntities){
        //Checks "range" spaces in all four directions
        Coordinates userLocation = this.getUser().getCurrent();
        Coordinates current;
        Boolean found;
        ArrayList<Entity> potentialTargets = new ArrayList<Entity>();
        for(int k = 0; k < 4; k++) {
            //In the start of each direction we start at character location
            current = userLocation;
            found = false;
            for (int i = 0; i < attackRange; i++) {
                //Uses switch to decide which direction to move in
                switch(k){
                    case(0):
                        current = current.getNorth();
                        break;
                    case(1):
                        current = current.getEast();
                        break;
                    case(2):
                        current = current.getSouth();
                        break;
                    case(3):
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
        }
        return potentialTargets;
    }
}
