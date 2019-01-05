package com.example.travis.entity;

public enum EntityState {
    Neutral,
    Defending,
    Charging,
    Stunned,
    CoolDown;
    public String toString(){
        String name = "";
        switch(this){
            case Neutral:
                name = "Neutral";
                break;
            case Defending:
                name = "Defending";
                break;
            case Charging:
                name = "Charging";
                break;
            case Stunned:
                name = "Stunned";
                break;
            case CoolDown:
                name = "CoolDown";
                break;
        }
        return name;
    }
}
