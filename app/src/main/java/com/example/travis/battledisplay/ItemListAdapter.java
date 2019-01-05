package com.example.travis.battledisplay;
import com.example.travis.action.*;
import com.example.travis.entity.Enemy;
import com.example.travis.entity.EntityAE;
import com.example.travis.entity.EntityState;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder>{
    private ArrayList<Action> actions;
    private ArrayList<ViewHolder> actionDisplay;
    private LayoutInflater mLayoutInflater;
    private Battle game;
    private Context app;

    public ItemListAdapter(Context context, ArrayList<Action> data, Battle game) {
        actions = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.game = game;
        actionDisplay = new ArrayList<ViewHolder>();
        this.app = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.action_list, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final Action current = actions.get(position);
        viewHolder.image.setImageResource(current.getId());
        Random rNumber = new Random();
        int choice = rNumber.nextInt(4) + 1;
        switch(choice){
            case(1):
                viewHolder.box.setBackgroundResource(R.drawable.box01);
                break;
            case(2):
                viewHolder.box.setBackgroundResource(R.drawable.box02);
                break;
            case(3):
                viewHolder.box.setBackgroundResource(R.drawable.box03);
                break;
            case(4):
                viewHolder.box.setBackgroundResource(R.drawable.box04);
                break;
        }

        viewHolder.cancel.setImageResource(R.drawable.icon_cancel);
        ///////this
        if(current instanceof MovementAction) {
            viewHolder.caption.setText(current.getName() + " x" + ((MovementAction) current).getCurrentUses());
            viewHolder.cost.setAlpha(0f);
            viewHolder.damage.setAlpha(0f);
        }
        else if(current instanceof AttackAction){
            viewHolder.caption.setText(current.getName());
            viewHolder.cost.setAlpha(1f);
            viewHolder.cost.setText("" + ((AttackAction) current).getCost());
            viewHolder.damage.setAlpha(1f);
            viewHolder.damage.setText("" + ((AttackAction) current).getAttackDamage());
        }
        else if(current instanceof DefenseAction){
            viewHolder.caption.setText(current.getName());
            viewHolder.cost.setAlpha(1f);
            viewHolder.cost.setText("" + ((DefenseAction) current).getCost());
            viewHolder.damage.setAlpha(0f);
            if(current.getUser().getState() == EntityState.Defending){
                viewHolder.cancel.setAlpha(1f);
            }
        }
        if(current.getName().equals("Move")){
            Log.d("Adapter", "Selected Move now belongs to " + current.getUser().getName());
            game.setSelectedMove(viewHolder.box);
            game.setCurrentAction(current);
        }
        View.OnClickListener performAction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Adapter", "Clicked on action: " + viewHolder.caption.getText());
                actionClick(viewHolder,current);
            }
        };
        viewHolder.box.setOnClickListener(performAction);

        if(current instanceof MovementAction && ((MovementAction) current).getCurrentUses() == 0){
            viewHolder.image.setAlpha(.5f);
            viewHolder.image.setBackgroundColor(Color.GRAY);
        }
        else if(!(current.getUser() instanceof Enemy) && current instanceof AttackAction
                && ((AttackAction) current).getCost() > ((EntityAE) current.getUser()).getCurrentEnergy()){
            viewHolder.image.setAlpha(.5f);
            viewHolder.image.setBackgroundColor(Color.GRAY);
        }
        else if(!(current.getUser() instanceof Enemy) & current instanceof DefenseAction){
            if(((DefenseAction) current).getCost() > ((EntityAE) current.getUser()).getCurrentEnergy()
                & current.getUser().getState() == EntityState.Neutral){
                viewHolder.image.setAlpha(.5f);
                viewHolder.image.setBackgroundColor(Color.GRAY);
            }
        }
        else{
            viewHolder.image.setAlpha(1f);
            viewHolder.image.setBackgroundColor(Color.TRANSPARENT);
        }
        actionDisplay.add(viewHolder);
        if(current.getUser() instanceof Enemy){
            viewHolder.box.setClickable(false);
        }
    }

    /**
     * When an action is clicked, this will check the current state of the game. If it is neutral
     * it will attempt to change the state of the game, displays the cancel icon and sets the
     * game's current action. If the game is not in neutral this will check if the cancel icon
     * is displayed. If it is not, nothing should happen. If it is then this will deactivate
     * the current action.
     * @param viewHolder the viewholder holding the entire action's display
     * @param current the current action being clicked on
     */
    public void actionClick(ViewHolder viewHolder, Action current){
        if(game.getCurrentState() == GameStates.Neutral) {
            if(current instanceof DefenseAction){
                if(viewHolder.cancel.getAlpha() == 0f && current.ActivateAction(game)){
                    viewHolder.cancel.setAlpha(1f);
                    game.getBottom().update();
                }
                else if(viewHolder.cancel.getAlpha() == 1f && current.DeactivateAction(game)){
                    viewHolder.cancel.setAlpha(0f);
                    game.getBottom().update();
                }
                return;
            }
            if (viewHolder.cancel.getAlpha() == 0f && current.ActivateAction(game)) {
                Log.d("Adapter", "Cancel appear");
                viewHolder.cancel.setAlpha(1f);
                game.setSelectedAction(viewHolder.box);
                Log.d("Adapter", "Changed current action to " + current.getUser().getName() + ": " + current.getName());
                game.setCurrentAction(current);
                Log.d("Adapter", "Game set to " + game.getCurrentState().toString());
            }
        }
        else if(game.getCurrentState() == GameStates.Animation | game.getCurrentState() == GameStates.Enemy){
            //Nothing is allowed to happen during animation
            return;
        }
        else{
            if (viewHolder.cancel.getAlpha() == 1f && current.DeactivateAction(game)) {
                //Used to deactivate an action when clicked again
                Log.d("Adapter", "Cancel disappear");
                viewHolder.cancel.setAlpha(0f);
                Log.d("Adapter", "Game set to Neutral");
                return;
            }
            else if (viewHolder.cancel.getAlpha() == 0f & viewHolder.image.getAlpha() == 1f) {
                ///Above checks for switching actions while one is activated
                //Will only swap if there is no cancel sign and if action is usable (not grayed out)

                //switch the action, first deactivate current action
                Log.d("Adapter", "Switch being made, Deactivate old Action");
                game.getSelectedAction().performClick();
                //then activate desired action
                Log.d("Adapter", "Activate new Action");
                current.ActivateAction(game);
                Log.d("Adapter", "Cancel appear");
                viewHolder.cancel.setAlpha(1f);
                game.setSelectedAction(viewHolder.box);
                game.setCurrentAction(current);
                Log.d("Adapter", "Game set to " + game.getCurrentState().toString());
            }
        }
        if(viewHolder.image.getAlpha() == .5f & !(current instanceof MovementAction)){
            Toast failed = Toast.makeText(app, "Not enough EP", Toast.LENGTH_LONG);
            failed.show();
        }
    }

    /**
     * Updates number of uses left for movement. Will grey out any actions
     * that can no longer be used. Changes actions to normal if the entity
     * is allowed to use them again.
     */
    public void update(){
        for(int i = 0; i < actionDisplay.size(); i++) {
            Action current = actions.get(i);
            if(current instanceof MovementAction) {
                actionDisplay.get(i).caption.setText(current.getName() + " x" + ((MovementAction) current).getCurrentUses());
            }
            if(current instanceof MovementAction && ((MovementAction) current).getCurrentUses() == 0){
                actionDisplay.get(i).image.setAlpha(.5f);
                actionDisplay.get(i).image.setBackgroundColor(Color.GRAY);
            }
            else if(current instanceof AttackAction && ((AttackAction) current).getCost() > ((EntityAE) current.getUser()).getCurrentEnergy()){
                actionDisplay.get(i).image.setAlpha(.5f);
                actionDisplay.get(i).image.setBackgroundColor(Color.GRAY);
            }
            else if(current instanceof DefenseAction && ((DefenseAction) current).getCost() > ((EntityAE) current.getUser()).getCurrentEnergy()
                    & current.getUser().getState() == EntityState.Neutral){
                actionDisplay.get(i).image.setAlpha(.5f);
                actionDisplay.get(i).image.setBackgroundColor(Color.GRAY);
            }
            else{
                actionDisplay.get(i).image.setAlpha(1f);
                actionDisplay.get(i).image.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public int getItemCount() {
        return actions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ImageView box;
        ImageView cancel;
        TextView caption;
        TextView damage;
        TextView cost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.actionImage);
            this.box = itemView.findViewById(R.id.box);
            this.cancel = itemView.findViewById(R.id.cancel);
            this.caption = itemView.findViewById(R.id.actionName);
            this.damage = itemView.findViewById(R.id.actionDamage);
            this.cost = itemView.findViewById(R.id.actionCost);
        }
    }
}
