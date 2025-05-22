package src.java;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private int row, col;
    public Rectangle rect;

    // Murs : true = mur présent
    private boolean northWall = true;
    private boolean eastWall = true;
    private boolean southWall = true;
    private boolean westWall = true;

    private boolean visited = false;  // utile pour l'algo de génération

    public Cell(int row, int col, Rectangle rect) {
        this.row = row;
        this.col = col;
        this.rect = rect;

        // Par défaut, cellule avec murs, fond blanc
        this.rect.setFill(Color.WHITE);
        this.rect.setStroke(Color.GRAY);
    }

    // Getters pour position
    public int getRow() { return row; }
    public int getCol() { return col; }

    // Getters pour murs
    public boolean hasNorthWall() { return northWall; }
    public boolean hasEastWall() { return eastWall; }
    public boolean hasSouthWall() { return southWall; }
    public boolean hasWestWall() { return westWall; }

    // Suppression des murs
    public void removeNorthWall() { northWall = false; }
    public void removeEastWall() { eastWall = false; }
    public void removeSouthWall() { southWall = false; }
    public void removeWestWall() { westWall = false; }

    // Visited flag pour génération ou parcours
    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }

    // Récupère voisins accessibles (pas de mur entre)
    public List<Cell> getNeighbors(Cell[][] grid) {
        List<Cell> neighbors = new ArrayList<>();
        int rows = grid.length;
        int cols = grid[0].length;

        // Nord
        if (row > 0 && !hasNorthWall()) neighbors.add(grid[row - 1][col]);
        // Sud
        if (row < rows - 1 && !hasSouthWall()) neighbors.add(grid[row + 1][col]);
        // Ouest
        if (col > 0 && !hasWestWall()) neighbors.add(grid[row][col - 1]);
        // Est
        if (col < cols - 1 && !hasEastWall()) neighbors.add(grid[row][col + 1]);

        return neighbors;
    }

    // Equals et hashCode pour utiliser dans collections (HashMap etc.)
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

    // Optionnel : méthode utilitaire pour afficher l'état des murs (debug)
    @Override
    public String toString() {
        return "Cell(" + row + "," + col + ") N:" + northWall + " E:" + eastWall + " S:" + southWall + " W:" + westWall;
    }
}
