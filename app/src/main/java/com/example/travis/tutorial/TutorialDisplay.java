package com.example.travis.tutorial;

import com.example.travis.action.*;
import com.example.travis.battledisplay.R;
import com.example.travis.entity.*;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import com.example.travis.entity.*;
import com.example.travis.entity.Character;

/**
 * Display object used to represent character information using
 * image,text, and recycle views
 */
public class TutorialDisplay {
    private Context appContext;
    private RecyclerView actionsList;
    private ImageView selectedCharacterIMG;
    private TextView selectedCharacterName;
    private TextView selectedCharacterHealth;
    private Entity selected;
    MediaPlayer mp;
    private TutorialBattle game;

    /**
     * Main constructor for the display objects all views are given
     * information through the selected character
     * @param game the reference to the current battle
     * @param selected the current selected character who's information
     *                 is being displayed
     * @param app the app context
     * @param stock the imageView used to represent the character icon
     * @param health the textView used to represent the character health
     * @param name the textView used to represent the character name
     * @param actions the recycleView used to show character actions
     */
    public TutorialDisplay(TutorialBattle game, Entity selected, Context app, ImageView stock, TextView health, TextView name, RecyclerView actions){
        this.appContext = app;
        this.mp = MediaPlayer.create(app,R.raw.shine);
        this.selectedCharacterIMG = stock;
        this.selectedCharacterName = name;
        this.selectedCharacterHealth = health;
        selectedCharacterName.setBackgroundResource(R.drawable.textbox01);
        selectedCharacterHealth.setBackgroundResource(R.drawable.textbox01);
        this.actionsList = actions;
        this.selected = selected;
        this.game = game;
        selectedCharacterIMG.setImageResource(selected.getStockId());
        selectedCharacterIMG.setBackgroundResource(R.drawable.box01);
        selectedCharacterName.setText(selected.getName());

        actionsList.setLayoutManager(new LinearLayoutManager(appContext,LinearLayoutManager.HORIZONTAL,false));
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) actionsList.getLayoutParams();
        marginLayoutParams.setMargins(0, 8, 0, 8);
        actionsList.setLayoutParams(marginLayoutParams);
        TutorialActionListAdapter adapter;
        if(selected instanceof Character) {
            selectedCharacterHealth.setText("HP:" + selected.getHealth() + " EP: " + ((EntityAE) selected).getCurrentEnergy());
            adapter = new TutorialActionListAdapter(appContext, ((Character)this.selected).getActions(), game);
        }
        else{
            selectedCharacterHealth.setText("HP:" + selected.getHealth());
            //adapter = new ItemListAdapter(appContext, ((Enemy)this.selected).getActions(), game);
            adapter = new TutorialActionListAdapter(appContext, ((Enemy)this.selected).getActions(), game);
        }
        actionsList.setAdapter(adapter);
        //
        //
        //
        //
        //
        //
        // Conditions

        switch(game.getCondition()){
            case(0):
                actionsList.setAlpha(0f);
                actionsList.setClickable(false);
                break;
            case(1):
                actionsList.setAlpha(0f);
                actionsList.setClickable(false);
                break;
            default:
                actionsList.setAlpha(1f);
                actionsList.setClickable(true);
                break;
        }
    }

    /**
     * Method used to change the display information to that of the newly
     * selected character or entity
     * @param selected
     */
    public void selection(Entity selected){
        if(selected instanceof Character) {
            Log.d("Display","Changing display to match " + selected.getName());
            this.selected = selected;
            selectedCharacterIMG.setImageResource(selected.getStockId());
            selectedCharacterName.setText(selected.getName());
            selectedCharacterHealth.setText("HP:" + selected.getHealth() + " EP: " + ((EntityAE) selected).getCurrentEnergy());
            actionsList.setLayoutManager(new LinearLayoutManager(appContext, LinearLayoutManager.HORIZONTAL, false));
            TutorialActionListAdapter adapter = new TutorialActionListAdapter(appContext, ((Character)selected).getActions(), game);
            actionsList.setAdapter(adapter);
            actionsList.setAlpha(1f);
        }
        else{
            this.selected = selected;
            selectedCharacterIMG.setImageResource(selected.getStockId());
            selectedCharacterName.setText(selected.getName());
            selectedCharacterHealth.setText("HP:" + selected.getHealth());
            actionsList.setLayoutManager(new LinearLayoutManager(appContext, LinearLayoutManager.HORIZONTAL, false));
            TutorialActionListAdapter adapter = new TutorialActionListAdapter(appContext, ((Enemy)selected).getActions(), game);
            actionsList.setAdapter(adapter);
            actionsList.setAlpha(0f);
        }
    }

    /**
     * This method is used to update the display usually after the current
     * selected entity has performed an action. Updates entity name
     * health and energy. Uses Item List Adapter update
     * method to update the action list
     */
    public void update(){
        selectedCharacterName.setText(selected.getName());
        if(selected instanceof  Character) {
            selectedCharacterHealth.setText("HP:" + selected.getHealth() + " EP: " + ((EntityAE) selected).getCurrentEnergy());
        }
        else{
            selectedCharacterHealth.setText("HP:" + selected.getHealth());
        }
        ((TutorialActionListAdapter)actionsList.getAdapter()).update();
    }
}
