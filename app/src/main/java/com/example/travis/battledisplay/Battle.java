package com.example.travis.battledisplay;
import com.example.travis.action.*;
import com.example.travis.entity.*;
import com.example.travis.entity.Character;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

public class Battle {
    private Context appContext;
    private GameStates currentState;

    private ArrayList<Entity> allEntities;
    private Entity selected;
    //fix this to allow double click move;
    private Battle selfReference;
    private ConstraintLayout screen;
    private TextView turnMessage;
    private ImageView selectedMove;
    private ImageView selectedAction;
    private Action currentAction;
    MediaPlayer mp;

    private int currentTurn;
    private Button turn;
    private Display bottom;
    private GameBoard board;

    public Battle(TableLayout board, Context app, ImageView stock, TextView health, TextView name,
                  RecyclerView actions, Button turn, ConstraintLayout screen, TextView turnMessage, ArrayList<Entity> all){
        this.selfReference = this;
        currentState = GameStates.Neutral;
        this.turnMessage = turnMessage;
        this.turnMessage.setAlpha(0f);
        this.appContext = app;
        this.mp = MediaPlayer.create(app,R.raw.shine);
        this.screen = screen;
        this.turn = turn;
        currentTurn =  0;
        View.OnClickListener performAction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentState != GameStates.Animation) {
                    Log.d("Game", "Ending turn");
                    currentTurn++;
                    endTurn();
                }
            }
        };
        this.turn.setOnClickListener(performAction);
        this.allEntities = all;
        this.board = new GameBoard(5,5,board,allEntities,app,this);
        selected = (Character)allEntities.get(0);
        this.bottom = new Display(this, selected, app,stock,health,name,actions);
        endTurn();
    }

    /**
     * When actions are activated they request to change the state of the game.
     * This is used to prevent actions from prematurely ending states and causing
     * errors. Actions can only change the state of the game if the state is
     * currently neutral.
     * @param request the state that an action wants to change to
     * @return true/false depending if the request was successful
     */
    public boolean RequestStart(GameStates request){
        if(currentState == GameStates.Enemy){
            return false;
        }
        else if(currentState != GameStates.Neutral){
            Log.d("RequestStart","Request failed");
            return false;
        }
        Log.d("RequestStart","game now " + request.toString());
        currentState = request;
        return true;
    }

    /**
     * Actions can request to end states and return back to neutral typically
     * once the action is completed. If the request matches the current state
     * the state will return to neutral.
     * @param request the state that an action wants to end
     * @return true/false depending if the request was successful
     */
    public boolean RequestEnd(GameStates request){
        if(currentState != request){
            Log.d("RequestEnd","Request failed");
            return false;
        }
        Log.d("RequestEnd","game now Neutral");
        currentState = GameStates.Neutral;
        return true;
    }

    /**
     * Changes display to reflect the character that has been chosen
     * Typically called from game board
     * @param selected the entity the user selected
     */
    public void selection(Entity selected){
       bottom.selection(selected);
    }

    /**
     * Updates the display to show reflections of Health, Energy
     * and Action usage.
     * Typically called from game board and attack method.
     */
    public void update(){
        bottom.update();
    }

    /**
     * This method will facilitate damage being dealt and all visual/audio effects
     * involved such as damage notifications and sound
     * @param receiver the entity receiving damage
     * @param damage the amount of damage being dealt
     */
    public void attack(final Entity receiver, int damage){
        //reduces energy of user
        if(selected instanceof EntityAE & currentState != GameStates.Enemy) {
            ((EntityAE) selected).setCurrentEnergy(((EntityAE) selected).getCurrentEnergy() - ((AttackAction) currentAction).getCost());
            selectedAction.performClick();
        }
        if(currentAction instanceof ForcePalm & receiver.getState() == EntityState.Defending){
           // damage /= 2;
            receiver.setDefense(0);
        }
        //change health of the receiver
        damage -= receiver.getDefense();
        if(damage < 0){
            damage = 0;
        }
        Log.d("Battle", "" + receiver.getName() + " took " + damage + " points of damage!");
        int updatedHealth = receiver.getHealth() - damage;
        if(updatedHealth <= 0){
            updatedHealth = 0;
        }
        receiver.setHealth(updatedHealth);
        update();

        //Grabs attacker and receiver image views
        final ImageView character = board.getTiles()[selected.getCurrent().getX()][selected.getCurrent().getY()];
        ImageView receiveIMG = board.getTiles()[receiver.getCurrent().getX()][receiver.getCurrent().getY()];

        //Sets damage text view position
        final TextView damageTaken = new TextView(appContext);
        screen.addView(damageTaken);
        damageTaken.setTextColor(Color.RED);
        damageTaken.setTypeface(null, Typeface.BOLD);
        damageTaken.setTextSize(30);
        damageTaken.setAlpha(0f);
        damageTaken.setX(0);
        damageTaken.setY(0);
        damageTaken.setText("-" + damage);
        damageTaken.setX(0 + receiveIMG.getWidth() * receiver.getCurrent().getX() + receiveIMG.getWidth()/2 - damageTaken.getWidth()/2);
        damageTaken.setY(0 + receiveIMG.getHeight() * receiver.getCurrent().getY());

        RequestStart(GameStates.Animation);
        character.setAlpha(0f);
        GameAnimation attackAnim = ((AttackAction) currentAction).getAttackAnimation();
        final ImageView attackAnimation = new ImageView(appContext);
        screen.addView(attackAnimation);
        attackAnimation.setImageResource(attackAnim.getAnimationId());
        attackAnimation.getLayoutParams().height = character.getLayoutParams().height * attackAnim.getHeight();
        attackAnimation.getLayoutParams().width = character.getLayoutParams().width * attackAnim.getWidth();
        attackAnimation.setAlpha(1f);
        attackAnimation.setX(0 - attackAnimation.getLayoutParams().width + character.getWidth() + character.getWidth() * selected.getCurrent().getX());
        attackAnimation.setY(0 - attackAnimation.getLayoutParams().height + character.getHeight() + character.getHeight() * selected.getCurrent().getY());
        ((AnimationDrawable) attackAnimation.getDrawable()).setOneShot(true);
        ((AnimationDrawable) attackAnimation.getDrawable()).start();

        System.out.println("ImageView this shouldn't make a difference");
        if (receiver.getCurrent().getY() != 0) {
            damageTaken.animate().translationY((float) damageTaken.getY() - receiveIMG.getHeight()/2).setDuration(1000).setStartDelay(attackAnim.getDuration());
        }
        damageTaken.animate().alpha(1f).setDuration(500).setStartDelay(attackAnim.getDuration()).withEndAction(new Runnable() {
            @Override
            public void run() {
                damageTaken.animate().alpha(0f).setDuration(500).setStartDelay(10);
                attackAnimation.setAlpha(0f);
                ((AnimationDrawable) attackAnimation.getDrawable()).stop();
                character.setAlpha(1f);
                //if shield breaker type move
                if(getCurrentAction() instanceof ForcePalm){
                    ((ForcePalm)getCurrentAction()).Use(selfReference,receiver);
                }
                if (receiver.getHealth() == 0) {
                    death(receiver);
                    if (win()) {
                        Toast endNotice = Toast.makeText(appContext, "You win game over", Toast.LENGTH_LONG);
                        endNotice.show();
                        ((MainActivity) appContext).setContentView(R.layout.level_select);
                    }
                    else if (lose()) {
                        Toast endNotice = Toast.makeText(appContext, "You lose game over", Toast.LENGTH_LONG);
                        endNotice.show();
                        ((MainActivity) appContext).setContentView(R.layout.level_select);
                    }
                }
                RequestEnd(GameStates.Animation);
            }
        });
    }

    public void heal(final Entity receiver, int recovery){
        //reduces energy of user
        if(selected instanceof EntityAE & currentState != GameStates.Enemy) {
            ((EntityAE) selected).setCurrentEnergy(((EntityAE) selected).getCurrentEnergy() - ((HealAction) currentAction).getCost());
            selectedAction.performClick();
        }

        //change health of the receiver
        Log.d("Battle", "" + receiver.getName() + " was healed " + recovery + " points of health!");
        int updatedHealth = receiver.getHealth() + recovery;
        if(updatedHealth > receiver.getMaxHealth()){
            updatedHealth = receiver.getMaxHealth();
        }
        receiver.setHealth(updatedHealth);
        update();

        //Grabs attacker and receiver image views
        final ImageView character = board.getTiles()[selected.getCurrent().getX()][selected.getCurrent().getY()];
        ImageView receiveIMG = board.getTiles()[receiver.getCurrent().getX()][receiver.getCurrent().getY()];

        //Sets damage text view position
        final TextView healthGiven = new TextView(appContext);
        screen.addView(healthGiven);
        healthGiven.setTextColor(Color.GREEN);
        healthGiven.setTypeface(null, Typeface.BOLD);
        healthGiven.setTextSize(30);
        healthGiven.setAlpha(0f);
        healthGiven.setX(0);
        healthGiven.setY(0);
        healthGiven.setText("+" + recovery);
        healthGiven.setX(0 + receiveIMG.getWidth() * receiver.getCurrent().getX() + receiveIMG.getWidth()/2 - healthGiven.getWidth()/2);
        healthGiven.setY(0 + receiveIMG.getHeight() * receiver.getCurrent().getY());

        RequestStart(GameStates.Animation);
        character.setAlpha(0f);
        GameAnimation attackAnim = ((HealAction) currentAction).getHealAnimation();
        final ImageView healAnimation = new ImageView(appContext);
        screen.addView(healAnimation);
        healAnimation.setImageResource(attackAnim.getAnimationId());
        healAnimation.getLayoutParams().height = character.getLayoutParams().height * attackAnim.getHeight();
        healAnimation.getLayoutParams().width = character.getLayoutParams().width * attackAnim.getWidth();
        healAnimation.setAlpha(1f);
        healAnimation.setX(2 - healAnimation.getLayoutParams().width + character.getWidth() + character.getWidth() * selected.getCurrent().getX());
        healAnimation.setY(0 - healAnimation.getLayoutParams().height + character.getHeight() + character.getHeight() * selected.getCurrent().getY());
        ((AnimationDrawable) healAnimation.getDrawable()).setOneShot(true);
        ((AnimationDrawable) healAnimation.getDrawable()).start();

        System.out.println("ImageView this shouldn't make a difference");
        if (receiver.getCurrent().getY() != 0) {
            healthGiven.animate().translationY((float) healthGiven.getY() - receiveIMG.getHeight()/2).setDuration(1000).setStartDelay(attackAnim.getDuration());
        }
        healthGiven.animate().alpha(1f).setDuration(500).setStartDelay(attackAnim.getDuration()).withEndAction(new Runnable() {
            @Override
            public void run() {
                healthGiven.animate().alpha(0f).setDuration(500).setStartDelay(10);
                healAnimation.setAlpha(0f);
                ((AnimationDrawable) healAnimation.getDrawable()).stop();
                character.setAlpha(1f);
                if (receiver.getHealth() == 0) {
                    death(receiver);
                    if (win()) {
                        Toast endNotice = Toast.makeText(appContext, "You win game over", Toast.LENGTH_LONG);
                        endNotice.show();
                        ((BattleActivity) appContext).finish();
                    }
                    else if (lose()) {
                        Toast endNotice = Toast.makeText(appContext, "You lose game over", Toast.LENGTH_LONG);
                        endNotice.show();
                        ((BattleActivity) appContext).finish();
                    }
                }
                RequestEnd(GameStates.Animation);
            }
        });
    }

    /**
     * Once a entity has lost all of its health they will be removed from the game.
     * This will signaled by them fading away leaving the typical smudge mark.
     * @param dead the entity that lost all HP
     */
    public void death(final Entity dead){
        board.getTiles()[dead.getCurrent().getX()][dead.getCurrent().getY()].animate().alpha(0f).setDuration(1500).withEndAction(new Runnable() {
            @Override
            public void run() {
                board.getTiles()[dead.getCurrent().getX()][dead.getCurrent().getY()].setBackgroundResource(R.drawable.smudge);
                board.getTiles()[dead.getCurrent().getX()][dead.getCurrent().getY()].setImageResource(android.R.color.transparent);
                board.getTiles()[dead.getCurrent().getX()][dead.getCurrent().getY()].setAlpha(1f);
            }
        });
        allEntities.remove(dead);
        board.getAllEntities().remove(dead);
    }

    /**
     * This calls for all characters to be refreshed, updates display and
     * deactivates any current action. This method is invoked in the on click
     * listener for the End Turn button.
     */
    public void endTurn(){
        if(currentTurn == 0){
            currentTurn++;
            turnMessage.setAlpha(1f);
            RequestStart(GameStates.Animation);
            turnMessage.setText("Turn: " + currentTurn);
            turnMessage.setX(0 - turnMessage.getLayoutParams().width - 70);
            turnMessage.animate().translationX(10).setDuration(1000).setStartDelay(200).withEndAction(new Runnable() {
                @Override
                public void run() {
                    turnMessage.animate().translationX(446).setDuration(1000).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            RequestEnd(GameStates.Animation);
                        }
                    });
                }
            });
            return;
        }
        else{
            //Checks if current selected is a character
            final Entity previouslySelected = selected;
            //Ends current action
            if(getCurrentState() != GameStates.Neutral) {
                selectedAction.performClick();
            }
            final ArrayList<Enemy> enemies = new ArrayList<Enemy>();
            for(int i = 0; i < allEntities.size(); i++) {
                if (allEntities.get(i) instanceof Enemy) {
                    if (allEntities.get(i) instanceof BasicTank) {
                        ((BasicTank) allEntities.get(i)).refresh(selfReference);
                    }
                    enemies.add((Enemy) allEntities.get(i));
                }
            }
            final int[] normalTurnDelay = {0};
            RequestStart(GameStates.Animation);
           /* final TextView turnMessage2 = new TextView(appContext);
            screen.addView(turnMessage2);
            turnMessage2.getLayoutParams().height = 67;
            turnMessage2.getLayoutParams().width = 228;*/
            turnMessage.setAlpha(1f);
            turnMessage.setText("Enemy Turn");
            turnMessage.setX(0 - turnMessage.getLayoutParams().width);
            turnMessage.animate().translationX(10).setDuration(1000).withEndAction(new Runnable() {
                @Override
                public void run() {
                    turnMessage.animate().translationX(440).setDuration(1000).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            RequestEnd(GameStates.Animation);
                            RequestStart(GameStates.Enemy);
                            for (int i = 0; i < enemies.size(); i++) {
                                //if (allEntities.get(i) instanceof Enemy) {
                                   // if(allEntities.get(i) instanceof BasicTank){
                                     //   ((BasicTank)allEntities.get(i)).refresh(selfReference);
                                    //}
                                    selected = enemies.get(i);
                                    Log.d("Enemy Turn","It's " + selected.getName() + " turn!");
                                    currentAction = enemies.get(i).getMain();
                                    Log.d("Enemy Turn","Using " + currentAction.getName());
                                    if(enemies.get(i) instanceof Turrent) {
                                        normalTurnDelay[0] = ((AttackAction) ((Turrent) enemies.get(i)).getMain()).getAttackAnimation().getDuration();
                                    }
                                    else if (enemies.get(i) instanceof HealBot){
                                        normalTurnDelay[0] = ((HealAction) ((HealBot) enemies.get(i)).getMain()).getHealAnimation().getDuration();
                                    }
                                    ((Enemy) selected).behavior(selfReference);
                                //}
                            }
                            turnMessage.setText("Turn: " + currentTurn);
                            turnMessage.bringToFront();
                            turnMessage.setX(0 - turnMessage.getLayoutParams().width);
                            turnMessage.animate().translationX(10).setDuration(1000).setStartDelay(normalTurnDelay[0]).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("EndTurn","First delay");
                                    //Requests moved inside so that the delayed enemy attacks don't prematurely
                                    //end the Animation state.
                                    RequestEnd(GameStates.Enemy);
                                    RequestStart(GameStates.Animation);
                                    turnMessage.animate().translationX(446).setDuration(1000).setStartDelay(10).withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("EndTurn","Second delay");
                                            //Crashes if end turn with enemy selected
                                            if(previouslySelected instanceof Character) {
                                                selected = previouslySelected;
                                                selection(previouslySelected);
                                            }
                                            else{
                                                selected = allEntities.get(0);
                                                selection(selected);
                                            }
                                            //Refreshes all characters
                                            for(int i = 0; i < allEntities.size(); i++){
                                                if(allEntities.get(i) instanceof Character){
                                                    ((Character) allEntities.get(i)).refresh(currentTurn,selfReference);
                                                }
                                            }
                                            update();
                                            Log.d("EndTurn","updated");
                                            RequestEnd(GameStates.Animation);
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
    }


    //win conditions
    public boolean win(){
        for(int i = 0; i < allEntities.size(); i++){
            if(allEntities.get(i) instanceof Enemy){
                return false;
            }
        }
        return true;
    }
    public boolean lose(){
        for(int i = 0; i < allEntities.size(); i++){
            if(allEntities.get(i) instanceof Character){
                return false;
            }
        }
        return true;
    }
    //lose conditions

    /**
     * Typical Getters and Setters
     * @return
     */
    public GameStates getCurrentState() {
        return currentState;
    }
    public Entity getSelected() {
        return selected;
    }
    public void setSelected(Entity selected) {
        this.selected = selected;
    }
    public ImageView getSelectedAction() {
        return selectedAction;
    }
    public void setSelectedAction(ImageView selected) {
        this.selectedAction = selected;
    }
    public GameBoard getBoard() {
        return board;
    }
    public Action getCurrentAction() {
        return currentAction;
    }
    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }

    public ImageView getSelectedMove() {
        return selectedMove;
    }
    public void setSelectedMove(ImageView selectedMove) {
        this.selectedMove = selectedMove;
    }

    public ArrayList<Entity> getAllEntities() {
        return allEntities;
    }
    public void setAllEntities(ArrayList<Entity> allEntities) {
        this.allEntities = allEntities;
    }

    public Display getBottom() {
        return bottom;
    }
    public void setBottom(Display bottom) {
        this.bottom = bottom;
    }

    //states of the game
        //neutral state
        //Choosing locations
        //Choosing other characters
        //Choosing environmental objects


}
