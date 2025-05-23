package src.main.java.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cell in the maze grid
 */
public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int row, col;
    public transient Rectangle rect;

    // Walls: true = wall present
    private boolean northWall = true;
    private boolean eastWall = true;
    private boolean southWall = true;
    private boolean westWall = true;

    private boolean visited = false;  // useful for generation algorithm

    public Cell(int row, int col, Rectangle rect) {
        this.row = row;
        this.col = col;
        this.rect = rect;

        // By default, cell with walls, white background
        if (this.rect != null) {
            this.rect.setFill(Color.WHITE);
            this.rect.setStroke(Color.GRAY);
        }
    }
    
    public Cell(int row, int col) {
        this(row, col, null);
    }

    // Getters for position
    public int getRow() { return row; }
    public int getCol() { return col; }

    // Getters for walls
    public boolean hasNorthWall() { return northWall; }
    public boolean hasEastWall() { return eastWall; }
    public boolean hasSouthWall() { return southWall; }
    public boolean hasWestWall() { return westWall; }

    // Wall removal
    public void removeNorthWall() { northWall = false; }
    public void removeEastWall() { eastWall = false; }
    public void removeSouthWall() { southWall = false; }
    public void removeWestWall() { westWall = false; }

    // Visited flag for generation or traversal
    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }

    // Get accessible neighbors (no wall between)
    public List<Cell> getNeighbors(Cell[][] grid) {
        List<Cell> neighbors = new ArrayList<>();
        int rows = grid.length;
        int cols = grid[0].length;

        // North
        if (row > 0 && !hasNorthWall()) neighbors.add(grid[row - 1][col]);
        // South
        if (row < rows - 1 && !hasSouthWall()) neighbors.add(grid[row + 1][col]);
        // West
        if (col > 0 && !hasWestWall()) neighbors.add(grid[row][col - 1]);
        // East
        if (col < cols - 1 && !hasEastWall()) neighbors.add(grid[row][col + 1]);

        return neighbors;
    }

    // Equals and hashCode for use in collections (HashMap etc.)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }

    // Optional: utility method to display wall state (debug)
    @Override
    public String toString() {
        return "Cell(" + row + "," + col + ") N:" + northWall + " E:" + eastWall + " S:" + southWall + " W:" + westWall;
    }
}
