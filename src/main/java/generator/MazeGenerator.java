package src.main.java.generator;

import src.main.java.model.Cell;

/**
 * Interface for maze generation algorithms
 */
public interface MazeGenerator {
    /**
     * Generates a maze
     * @param grid The grid of cells
     * @param perfectMaze true for a perfect maze, false for an imperfect maze
     */
    void generateMaze(Cell[][] grid, boolean perfectMaze);
    
    /**
     * Returns the name of the algorithm
     * @return The name of the algorithm
     */
    String getName();
}
