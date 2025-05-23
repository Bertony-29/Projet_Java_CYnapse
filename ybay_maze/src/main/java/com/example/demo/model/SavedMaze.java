package com.example.demo.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing a saved maze
 */
public class SavedMaze implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private LocalDateTime creationDate;
    private int rows;
    private int cols;
    private boolean perfectMaze;
    private String generatorAlgorithm; // Generation algorithm used
    private boolean[][] northWalls;
    private boolean[][] eastWalls;
    private boolean[][] southWalls;
    private boolean[][] westWalls;
    private int startRow = -1;
    private int startCol = -1;
    private int goalRow = -1;
    private int goalCol = -1;
    
    // Stockage du chemin de solution
    private List<int[]> solutionPath = null; // Liste de coordonnées [row, col]
    
    /**
     * Constructor to create a saved maze from an existing grid
     */
    public SavedMaze(String name, Cell[][] grid, boolean perfectMaze, String generatorAlgorithm, Cell startCell, Cell goalCell) {
        this.name = name;
        this.creationDate = LocalDateTime.now();
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.perfectMaze = perfectMaze;
        this.generatorAlgorithm = generatorAlgorithm;
        
        // Initialize wall arrays
        this.northWalls = new boolean[rows][cols];
        this.eastWalls = new boolean[rows][cols];
        this.southWalls = new boolean[rows][cols];
        this.westWalls = new boolean[rows][cols];
        
        // Save wall state
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                northWalls[r][c] = cell.hasNorthWall();
                eastWalls[r][c] = cell.hasEastWall();
                southWalls[r][c] = cell.hasSouthWall();
                westWalls[r][c] = cell.hasWestWall();
                
                // Save start and goal positions
                if (cell == startCell) {
                    startRow = r;
                    startCol = c;
                }
                if (cell == goalCell) {
                    goalRow = r;
                    goalCol = c;
                }
            }
        }
        
        // Initialiser le chemin de solution comme une liste vide
        this.solutionPath = new ArrayList<>();
    }
    
    /**
     * Applies the saved maze to an existing grid
     */
    public void applyToGrid(Cell[][] grid) {
        if (grid.length != rows || grid[0].length != cols) {
            throw new IllegalArgumentException("The grid size does not match the saved maze");
        }
        
        // Restore wall state
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                
                // Reset all walls to true first (add all walls)
                // This ensures we start with a clean state
                cell.resetWalls();
                
                // Then apply the saved wall configuration
                if (!northWalls[r][c]) cell.removeNorthWall();
                if (!eastWalls[r][c]) cell.removeEastWall();
                if (!southWalls[r][c]) cell.removeSouthWall();
                if (!westWalls[r][c]) cell.removeWestWall();
            }
        }
    }
    
    /**
     * Checks if this saved maze has a defined start point
     */
    public boolean hasStartCell() {
        return startRow >= 0 && startCol >= 0;
    }
    
    /**
     * Checks if this saved maze has a defined goal point
     */
    public boolean hasGoalCell() {
        return goalRow >= 0 && goalCol >= 0;
    }
    
    /**
     * Gets the start cell in the given grid
     */
    public Cell getStartCell(Cell[][] grid) {
        if (hasStartCell()) {
            return grid[startRow][startCol];
        }
        return null;
    }
    
    /**
     * Gets the goal cell in the given grid
     */
    public Cell getGoalCell(Cell[][] grid) {
        if (hasGoalCell()) {
            return grid[goalRow][goalCol];
        }
        return null;
    }
    
    /**
     * Gets the name of the saved maze
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the creation date of the saved maze
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    
    /**
     * Gets the formatted creation date of the saved maze
     */
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return creationDate.format(formatter);
    }
    
    /**
     * Gets the number of rows in the saved maze
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Gets the number of columns in the saved maze
     */
    public int getCols() {
        return cols;
    }
    
    /**
     * Checks if this is a perfect maze
     */
    public boolean isPerfectMaze() {
        return perfectMaze;
    }
    
    /**
     * Gets the generation algorithm used
     */
    public String getGeneratorAlgorithm() {
        return generatorAlgorithm;
    }
    
    /**
     * Sets the generation algorithm used
     */
    public void setGeneratorAlgorithm(String generatorAlgorithm) {
        this.generatorAlgorithm = generatorAlgorithm;
    }
    
    /**
     * Returns the filename for this saved maze
     * @return The filename with extension
     */
    public String getFileName() {
        return sanitizeFilename(name) + ".maze";
    }
    
    /**
     * Sanitizes a filename by replacing invalid characters with underscores
     */
    private String sanitizeFilename(String name) {
        return name.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
    
    /**
     * Définit le chemin de solution pour ce labyrinthe
     * @param path Liste des cellules formant le chemin
     */
    public void setSolutionPath(List<Cell> path) {
        if (path == null || path.isEmpty()) {
            this.solutionPath = null;
            return;
        }
        
        this.solutionPath = new ArrayList<>();
        for (Cell cell : path) {
            this.solutionPath.add(new int[] {cell.getRow(), cell.getCol()});
        }
    }
    
    /**
     * Vérifie si ce labyrinthe a un chemin de solution défini
     */
    public boolean hasSolutionPath() {
        return solutionPath != null && !solutionPath.isEmpty();
    }
    
    /**
     * Récupère le chemin de solution pour ce labyrinthe
     * @param grid La grille de cellules
     * @return Liste des cellules formant le chemin de solution, ou liste vide si aucun chemin n'est défini
     */
    public List<Cell> getSolutionPath(Cell[][] grid) {
        List<Cell> path = new ArrayList<>();
        
        if (solutionPath == null || solutionPath.isEmpty()) {
            return path;
        }
        
        for (int[] coords : solutionPath) {
            int r = coords[0];
            int c = coords[1];
            if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length) {
                path.add(grid[r][c]);
            }
        }
        
        return path;
    }
    
    @Override
    public String toString() {
        return name + " (" + rows + "x" + cols + ") - " + getFormattedDate();
    }
}
