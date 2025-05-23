package com.example.demo.generator;

import com.example.demo.model.Cell;
import java.util.*;

/**
 * Implementation of Prim's algorithm for maze generation
 */
public class PrimMazeGenerator implements MazeGenerator {

    private static final Random random = new Random();

    @Override
    public void generateMaze(Cell[][] grid, boolean perfectMaze) {
        // Generate a perfect maze first
        generatePerfectMaze(grid);
        
        // If we want an imperfect maze, remove some walls randomly
        if (!perfectMaze) {
            createImperfectMaze(grid);
        }
    }
    
    @Override
    public String getName() {
        return "Prim";
    }

    /**
     * Generates a perfect maze using Prim's algorithm
     */
    private void generatePerfectMaze(Cell[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Choose a random starting cell
        int startRow = random.nextInt(rows);
        int startCol = random.nextInt(cols);

        Cell startCell = grid[startRow][startCol];
        startCell.setVisited(true);

        List<Edge> frontier = new ArrayList<>();

        // Add the walls of the start cell to the frontier list
        addFrontierEdges(startCell, grid, frontier);

        while (!frontier.isEmpty()) {
            // Choose a random wall
            Edge edge = frontier.remove(random.nextInt(frontier.size()));

            Cell cell1 = edge.cell1;
            Cell cell2 = edge.cell2;

            if (cell2.isVisited()) continue;

            // Remove wall between cell1 and cell2
            removeWallBetween(cell1, cell2);

            cell2.setVisited(true);
            addFrontierEdges(cell2, grid, frontier);
        }

        // Reset visited for other uses
        for (int r=0; r < rows; r++) {
            for (int c=0; c < cols; c++) {
                grid[r][c].setVisited(false);
            }
        }
    }
    
    /**
     * Transforms a perfect maze into an imperfect maze by removing walls randomly
     */
    private void createImperfectMaze(Cell[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        
        // Determine the number of walls to remove (about 10-15% of cells)
        int wallsToRemove = (int) (rows * cols * 0.1);
        
        for (int i = 0; i < wallsToRemove; i++) {
            // Choose a random cell
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            Cell cell = grid[r][c];
            
            // Choose a random direction (0=North, 1=East, 2=South, 3=West)
            int direction = random.nextInt(4);
            
            // Check if we can remove the wall in this direction
            switch (direction) {
                case 0: // North
                    if (r > 0 && cell.hasNorthWall()) {
                        cell.removeNorthWall();
                        grid[r-1][c].removeSouthWall();
                    }
                    break;
                case 1: // East
                    if (c < cols-1 && cell.hasEastWall()) {
                        cell.removeEastWall();
                        grid[r][c+1].removeWestWall();
                    }
                    break;
                case 2: // South
                    if (r < rows-1 && cell.hasSouthWall()) {
                        cell.removeSouthWall();
                        grid[r+1][c].removeNorthWall();
                    }
                    break;
                case 3: // West
                    if (c > 0 && cell.hasWestWall()) {
                        cell.removeWestWall();
                        grid[r][c-1].removeEastWall();
                    }
                    break;
            }
        }
    }

    private void addFrontierEdges(Cell cell, Cell[][] grid, List<Edge> frontier) {
        int r = cell.getRow();
        int c = cell.getCol();
        int rows = grid.length;
        int cols = grid[0].length;

        if (r > 0 && !grid[r-1][c].isVisited()) frontier.add(new Edge(cell, grid[r-1][c]));
        if (r < rows-1 && !grid[r+1][c].isVisited()) frontier.add(new Edge(cell, grid[r+1][c]));
        if (c > 0 && !grid[r][c-1].isVisited()) frontier.add(new Edge(cell, grid[r][c-1]));
        if (c < cols-1 && !grid[r][c+1].isVisited()) frontier.add(new Edge(cell, grid[r][c+1]));
    }

    private void removeWallBetween(Cell c1, Cell c2) {
        int dr = c2.getRow() - c1.getRow();
        int dc = c2.getCol() - c1.getCol();

        if (dr == 1) { // c2 is below c1
            c1.removeSouthWall();
            c2.removeNorthWall();
        } else if (dr == -1) { // c2 is above c1
            c1.removeNorthWall();
            c2.removeSouthWall();
        } else if (dc == 1) { // c2 is to the right of c1
            c1.removeEastWall();
            c2.removeWestWall();
        } else if (dc == -1) { // c2 is to the left of c1
            c1.removeWestWall();
            c2.removeEastWall();
        }
    }

    private static class Edge {
        Cell cell1, cell2;

        Edge(Cell c1, Cell c2) {
            cell1 = c1;
            cell2 = c2;
        }
    }
}
