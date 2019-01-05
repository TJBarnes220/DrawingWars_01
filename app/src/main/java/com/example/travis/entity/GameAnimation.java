package com.example.travis.entity;

public class GameAnimation {
    private int animationId;
    private int height;
    private int width;
    private int duration;

    public GameAnimation(int animationId, int height, int width, int duration){
        this.animationId = animationId;
        this.width = width;
        this.height = height;
        this.duration = duration;
    }

    public int getAnimationId() {
        return animationId;
    }
    public void setAnimationId(int animationId) {
        this.animationId = animationId;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
