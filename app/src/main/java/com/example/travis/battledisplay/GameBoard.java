package com.example.travis.battledisplay;
import com.example.travis.action.*;
import com.example.travis.entity.*;
import com.example.travis.entity.Character;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import java.util.ArrayList;

/**
 * GameBoard object used to soley represent the game board. A tableLayout
 * is used to show all positions of the game board where characters and
 * enemies reside.
 */
public class GameBoard {
    //Main components of the Game board
    private int height;
    private int width;
    private ImageView[][] tiles;
    private ArrayList<Coordinates>potentialMovement;
    private ArrayList<Entity>potentialTargets;

    private TableLayout board;
    private Context appContext;
    //Entity components of the game board
    private ArrayList<Character> players;
    private ArrayList<Entity> allEntities;
    private ArrayList<Enemy> enemies;
    //Reference to the overall game
    private Battle game;

    /**
     * Main constructor for Game Boards. The entity array list is filtered and the tiles
     * 2d array is constructed. Immediately makes board.
     * @param height the height of the game board
     * @param width the width of the game board
     * @param board the tableLayout used to hold the board
     * @param allEntities the array list of all entities on the board
     * @param app the app context for the current app
     * @param game the reference to the current battle
     */
    public GameBoard(int height, int width, TableLayout board, ArrayList<Entity> allEntities, Context app,Battle game){
        this.height = height;
        this.width = width;
        this.board = board;
        this.appContext = app;
        this.allEntities = allEntities;
        filter();
        tiles = new ImageView[width][height];
        potentialMovement = new ArrayList<Coordinates>();
        potentialTargets = new ArrayList<Entity>();
        this.game = game;
        makeBoard();
    }

    /**
     * Used to filter the entities in the Entity array list.
     * The contents are broken up between characters and enemies.
     */
    public void filter(){
        players = new ArrayList<Character>();
        enemies = new ArrayList<Enemy>();
        for(int i = 0; i < allEntities.size(); i++){
            if(allEntities.get(i) instanceof Character){
                players.add((Character) allEntities.get(i));
            }
            else if(allEntities.get(i) instanceof Enemy){
                enemies.add((Enemy) allEntities.get(i));
            }
        }
    }

    /**
     * Method used to construct board with its given data fields.
     * Rows are made and filled with imageviews depending on the height
     * and width of the board. On click listeners are made and assigned
     * to each imageview. Character/Enemy images are placed in their
     * respected locations.
     */
    public void makeBoard(){
        //Onclick listeners for game board tiles
        View.OnClickListener locate = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Coordinates location = (Coordinates)view.getTag();
                Log.i("GameBoard", "You clicked x = " + location.getX() + ", y = " + location.getY());
                if(game.getCurrentState() == GameStates.Neutral) {
                    //If user clicked on the currently selected character
                    if(location.equals(game.getSelected().getCurrent()) & game.getSelected() instanceof Character)
                    {
                        Log.i("GameBoard", "Clicked on " + game.getSelected().getName() + " again.");
                        game.getSelectedMove().performClick();
                        return;
                    }
                    else {//checks if user clicked on a character not currently selected
                        for (int i = 0; i < allEntities.size(); i++) {
                            if (location.equals(allEntities.get(i).getCurrent())) {
                                Log.i("GameBoard", "You clicked on " + allEntities.get(i).getName() +"!");
                                game.selection(allEntities.get(i));
                                game.setSelected(allEntities.get(i));
                                return;
                            }
                        }
                    }
                }
                else if(game.getCurrentState() == GameStates.Moving){
                    if(((MovementAction)game.getCurrentAction()).getCurrentUses() > 0) {
                        moveCharacter(location);
                    }
                }
                else if(game.getCurrentState() == GameStates.Attacking){
                    for(int i = 0; i < potentialTargets.size(); i++){
                        if(location.equals(potentialTargets.get(i).getCurrent())){
                            game.attack(potentialTargets.get(i),((AttackAction)game.getCurrentAction()).getAttackDamage());
                            game.getSelectedAction().performClick();
                        }
                    }
                }
            }
        };
        //Constructs actual table
        final TableRow.LayoutParams params = new TableRow.LayoutParams(100,
                100, 1);
        final TableRow.LayoutParams tableSize = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT, 1);
        for(int i = 0; i < height; i++){
            TableRow row = new TableRow(appContext);
            for(int j = 0; j < width; j++){
                Coordinates location = new Coordinates(j,i);
                ImageView pic = new ImageView(appContext);
                pic.setLayoutParams(params);
                pic.setOnClickListener(locate);
                pic.setTag(new Coordinates(j,i));
                row.addView(pic);
                tiles[j][i] = pic;
                //pic.setImageResource(R.drawable.smudge);
                //Places entity pics in their respective location
                for(int k = 0; k < allEntities.size(); k++) {
                    if (location.equals(allEntities.get(k).getCurrent())) {
                        pic.setImageResource(allEntities.get(k).getImageId());
                    }
                }
            }
            row.setLayoutParams(tableSize);
            board.addView(row,tableSize);
        }
    }

    /**
     * Method used to move the game's selected character. Checks if location
     * is the current spot of any entity on the board. Uses potentialMovement
     * array list to find a place to move to
     * @param location current spot on board being checked for possible movement
     */
    public void moveCharacter(Coordinates location){
        //If user clicks on selected character while trying to move they will end move phase
        if(location.equals(game.getSelected().getCurrent())) {
            Log.i("GameBoard","Clicked on " + game.getSelected().getName() + " again, ending movements.");
            game.getSelectedAction().performClick();
            return;
        }
        //Checks if user clicked on another entity on the board
        for(int i = 0; i < allEntities.size(); i++){
            if(location.equals(allEntities.get(i).getCurrent())){
                if(allEntities.get(i) instanceof Character) {
                    ////Switch to now move this character
                    Log.i("GameBoard", "Switch moving to " + allEntities.get(i).getName());
                    game.getSelectedMove().performClick();
                    game.selection(allEntities.get(i));
                    game.setSelected(allEntities.get(i));
                    //game.getSelectedMove().performClick();

                    return;
                }
                else if( allEntities.get(i) instanceof  Enemy){
                    Log.i("GameBoard", "You can't move over an enemy location");
                    return;
                }
            }
        }
        //If user clicked on movable area the selected character moves
        for(int i = 0; i < potentialMovement.size(); i++){
            if(location.equals(potentialMovement.get(i))){
                //Removes current movement indicators
                undoLayout();

                //Adds smudges to previous location of character moved
                tiles[game.getSelected().getCurrent().getX()][game.getSelected().getCurrent().getY()].setImageResource(android.R.color.transparent);
                tiles[game.getSelected().getCurrent().getX()][game.getSelected().getCurrent().getY()].setBackgroundResource(R.drawable.smudge);

                //Moves character image, sets new location of character, displays new movement options
                game.getSelected().setCurrent(location);
                tiles[game.getSelected().getCurrent().getX()][game.getSelected().getCurrent().getY()].setImageResource(game.getSelected().getImageId());
                layoutMovements();
                Log.i("GameBoard", game.getSelected().getName() + " moved to: x = " + location.getX() + ", y = " + location.getY());

                //Reduces number of moves left and updates display to reflect that change
                ((MovementAction)game.getCurrentAction()).setCurrentUses(((MovementAction) game.getCurrentAction()).getCurrentUses() - 1);
                Log.i("GameBoard", "You have " + ((MovementAction) game.getCurrentAction()).getCurrentUses() + " moves left");
                game.update();
                //If last move was performed then permanently ends movement
                if(((MovementAction) game.getCurrentAction()).getCurrentUses() == 0){
                    Log.i("GameBoard", "You have no moves left");
                    game.getSelectedAction().performClick();
                }
                return;
            }
        }
        Log.i("GameBoard", "A possible location was not selected");
    }

    /**
     * Highlights the locations where the player can currently move.
     * Typically checks north/south/east/west and wont display movement
     * if another entity is in the way or if the space is out of bounds
     */
    public void layoutMovements() {
        potentialMovement.add(game.getSelected().getCurrent().getNorth());
        potentialMovement.add(game.getSelected().getCurrent().getSouth());
        potentialMovement.add(game.getSelected().getCurrent().getWest());
        potentialMovement.add(game.getSelected().getCurrent().getEast());
        for (int i = 0; i < allEntities.size(); i++) {
            for (int j = 0; j < potentialMovement.size(); j++) {
                if (allEntities.get(i).getCurrent().equals(potentialMovement.get(j))) {
                    System.out.print("This matched an entity location - ");
                    potentialMovement.get(j).printLocation();
                    potentialMovement.remove(potentialMovement.get(j));
                    break;
                }
            }
        }
        for (int i = 0; i < potentialMovement.size(); i++) {
            //Above nested loops do not check if outside board so this checks if they are
            if (potentialMovement.get(i).getX() < 0 | potentialMovement.get(i).getX() >= width |
                    potentialMovement.get(i).getY() < 0 | potentialMovement.get(i).getY() >= height) {
                System.out.print("This is out of bounds 1 - ");
                potentialMovement.get(i).printLocation();
                potentialMovement.remove(potentialMovement.get(i));
                i--;
            }
        }
        for (int i = 0; i < potentialMovement.size(); i++) {
            tiles[potentialMovement.get(i).getX()][potentialMovement.get(i).getY()].setImageResource(game.getSelected().getImageId());
            tiles[potentialMovement.get(i).getX()][potentialMovement.get(i).getY()].setAlpha(.5f);
        }
    }

    /**
     * Removes highlight from LayoutMovements method to help transition
     * the player into a new movement choice or to show end of moving phase
     */
    public void undoLayout() {
        for(int i = 0; i < potentialMovement.size(); i++){
            if(potentialMovement.get(i).getX() < 0 || potentialMovement.get(i).getX() >= width ||
                    potentialMovement.get(i).getY() < 0 || potentialMovement.get(i).getY() >= height ){
                continue;
            }
            //tiles[potentialMovement.get(i).getX()][potentialMovement.get(i).getY()].setImageResource(R.drawable.smudge)
            tiles[potentialMovement.get(i).getX()][potentialMovement.get(i).getY()].setImageResource(android.R.color.transparent);
            tiles[potentialMovement.get(i).getX()][potentialMovement.get(i).getY()].setAlpha(1f);
        }
        potentialMovement = new ArrayList<Coordinates>();
    }

    /**
     * Used to show targets that the selected character can hit. Sets the current
     * potentialTargets.
     * @param targets the potentialTargets found by the character attack action
     */
    public void showTargets(ArrayList<Entity> targets){
        potentialTargets = targets;
        for(int i = 0; i < targets.size(); i++){
            targets.get(i).getCurrent().printLocation();
            tiles[targets.get(i).getCurrent().getX()][targets.get(i).getCurrent().getY()].setBackgroundColor(Color.GRAY);
            tiles[targets.get(i).getCurrent().getX()][targets.get(i).getCurrent().getY()].setAlpha(.5f);
        }
    }

    /**
     * Undoes showTargets reverting the board to its normal state.
     * Also removes all entites from potentialTarget list
     * @param targets the potentialTargets found by the character attack action
     */
    public void undoTargets(ArrayList<Entity> targets){
        for(int i = 0; i < targets.size(); i++){
            tiles[targets.get(i).getCurrent().getX()][targets.get(i).getCurrent().getY()].setBackgroundColor(Color.TRANSPARENT);
            tiles[targets.get(i).getCurrent().getX()][targets.get(i).getCurrent().getY()].setAlpha(1f);
        }
        potentialTargets = new ArrayList<Entity>();
    }

    //Getters of Game Board Objects
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public ImageView[][] getTiles() {
        return tiles;
    }
    public TableLayout getBoard() {
        return board;
    }
    public ArrayList<Entity> getAllEntities() {
        return allEntities;
    }
}

