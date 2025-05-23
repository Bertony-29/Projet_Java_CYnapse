package com.example.demo.solver;

import com.example.demo.model.Cell;
import java.util.List;

/**
 * Interface for maze solving algorithms
 */
public interface MazeSolver {
    /**
     * Solves the maze by finding a path from start to goal
     * @param grid The grid of cells representing the maze
     * @param start The starting cell
     * @param goal The goal cell
     * @return The list of cells forming the path, or an empty list if no path is found
     */
    List<Cell> solve(Cell[][] grid, Cell start, Cell goal);
    
    /**
     * Initializes the algorithm for step-by-step solving
     * @param grid The grid of cells representing the maze
     * @param start The starting cell
     * @param goal The goal cell
     */
    void initializeStepByStep(Cell[][] grid, Cell start, Cell goal);
    
    /**
     * Executes one step of the solving algorithm
     * @return true if the algorithm has finished, false otherwise
     */
    boolean executeStep();
    
    /**
     * Gets the current path (partial or complete) after one or more steps
     * @return The list of cells forming the current path
     */
    List<Cell> getCurrentPath();
    
    /**
     * Sets the current path
     * @param path The list of cells forming the path
     */
    void setCurrentPath(List<Cell> path);
    
    /**
     * Gets the cells visited during exploration
     * @return The list of visited cells
     */
    List<Cell> getVisitedCells();
    
    /**
     * Sets the visited cells
     * @param cells The list of visited cells
     */
    void setVisitedCells(List<Cell> cells);
    
    /**
     * Returns the name of the algorithm for display
     * @return The name of the algorithm
     */
    String getName();
}
