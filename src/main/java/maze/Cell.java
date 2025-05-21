package src.main;


public class Cell {
    private int x, y;
    private boolean visited;
    private boolean northWall, eastWall, southWall, westWall;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.visited = false;
        // Par défaut, toutes les cellules ont des murs partout
        this.northWall = true;
        this.eastWall = true;
        this.southWall = true;
        this.westWall = true;
    }

    public boolean hasNorthWall() { return northWall; }
    public boolean hasEastWall() { return eastWall; }
    public boolean hasSouthWall() { return southWall; }
    public boolean hasWestWall() { return westWall; }

    public void removeNorthWall() { northWall = false; }
    public void removeEastWall() { eastWall = false; }
    public void removeSouthWall() { southWall = false; }
    public void removeWestWall() { westWall = false; }

    public void removeWall(PrimMazeGenerator.Direction direction) {
        switch (direction) {
            case NORTH: removeNorthWall(); break;
            case EAST: removeEastWall(); break;
            case SOUTH: removeSouthWall(); break;
            case WEST: removeWestWall(); break;
        }
    }
    public boolean isBorderCell(int x , int y , int height , int width){
        return (x==0 || x == y || width - 1 == x || y == height-1);

    }

    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }

}
