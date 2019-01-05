package com.example.travis.entity;

import com.example.travis.action.Action;
import com.example.travis.battledisplay.Coordinates;

/**
 * Abstract Super Class used to represent all selectable things that
 * take up a spot on the board. Each should have its
 * own image, stock/close up image, name, health and
 * coordinate indicating its location.
 */
public abstract class Entity {
    private int imageId;
    private int stockId;
    private String name;
    private int maxHealth;
    private int health;
    private int defense;
    private Coordinates current;
    private EntityState state;
    private Action stateManipulate;

    /**
     * Basic constructor for an Entity
     * @param img
     * @param stock
     * @param name
     * @param health
     * @param current
     */
    public Entity(int img, int stock, String name, int health, Coordinates current){
        this.imageId = img;
        this.stockId = stock;
        this.name = name;
        this.maxHealth = health;
        this.health = this.maxHealth;
        this.current = current;
        this.state = EntityState.Neutral;
        stateManipulate = null;
    }

    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getStockId() {
        return stockId;
    }
    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public Coordinates getCurrent() {
        return current;
    }
    public void setCurrent(Coordinates current) {
        this.current = current;
    }

    public EntityState getState() {
        return state;
    }
    public void setState(EntityState state) {
        this.state = state;
    }

    public int getDefense() {
        return defense;
    }
    public void setDefense(int defense) {
        this.defense = defense;
    }

    public Action getStateManipulate() {
        return stateManipulate;
    }
    public void setStateManipulate(Action stateManipulate) {
        this.stateManipulate = stateManipulate;
    }
}
