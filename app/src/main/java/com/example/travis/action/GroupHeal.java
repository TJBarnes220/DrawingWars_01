package com.example.travis.action;

import android.util.Log;

import com.example.travis.battledisplay.Coordinates;
import com.example.travis.entity.Entity;
import com.example.travis.entity.GameAnimation;

import java.util.ArrayList;

public class GroupHeal extends HealAction {
    public GroupHeal(Entity user, int id, String name, int recovery, int cost, GameAnimation healAnimation) {
        super(user, id, name, recovery, cost, healAnimation);
    }

    @Override
    public ArrayList<Entity> findTargets(ArrayList<Entity> allEntities) {
        ArrayList<Entity> potentialTargets = new ArrayList<Entity>();
        Coordinates user = getUser().getCurrent();
        for(int i = user.getY() - 1; i <= user.getY() + 1; i++){
            for(int j = user.getX() - 1; j <= user.getX() + 1; j++){
                Coordinates start = new Coordinates(j,i);
                start.printLocation();
                for(int index = 0; index < allEntities.size(); index++){
                    if(allEntities.get(index).getCurrent().equals(start)) {
                        potentialTargets.add(allEntities.get(index));
                    }
                }
            }
        }
        return potentialTargets;
    }
}
