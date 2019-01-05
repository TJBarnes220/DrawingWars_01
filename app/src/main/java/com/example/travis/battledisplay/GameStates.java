package com.example.travis.battledisplay;

public enum GameStates {
    Neutral,
    Moving,
    Targeting,
    Attacking,
    Animation,
    Enemy;
    public String toString(){
        String name = "";
        switch(this){
            case Animation:
                name = "Animation";
                break;
            case Enemy:
                name = "Enemy";
                break;
            case Moving:
                name = "Moving";
                break;
            case Neutral:
                name = "Neutral";
                break;
            case Attacking:
                name = "Attacking";
                break;
            case Targeting:
                name = "Targeting";
                break;
        }
        return name;
    }
}

