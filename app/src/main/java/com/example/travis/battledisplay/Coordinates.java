package com.example.travis.battledisplay;

/**
 * This class is used to signify the location of entities on the
 * game board.
 */
public class Coordinates {
    int x;
    int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Comparison method
     * @param other Coordinate to be compared
     * @return true/false depending if coordinates match
     */
    public boolean equals(Coordinates other){
        return this.x == other.getX() && this.y == other.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Prints out Coordinate to System typically
     * used in testing
     */
    public void printLocation(){
        System.out.println("Coordinates: x = " + this.x + ", y = " + this.y);
    }

    /**
     * The following are used to return coordinates
     * surrounding the current location. North/South/East/West
     * @return
     */
    public Coordinates getNorth(){
        return new Coordinates(x,y-1);
    }
    public Coordinates getSouth(){
        return new Coordinates(x, y+1);
    }
    public Coordinates getWest(){
        return new Coordinates(x-1, y);
    }
    public Coordinates getEast(){
        return new Coordinates(x+1, y);
    }
}