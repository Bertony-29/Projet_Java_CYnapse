package src.main.java.solver;

import src.main.java.model.Cell;
import java.util.*;

/**
 * Implementation of the Depth-First Search algorithm for maze solving
 */
public class DFSSolver implements MazeSolver {
    
    // Variables for step-by-step mode
    private Cell[][] grid;
    private Cell start;
    private Cell goal;
    private Stack<Cell> stack;
    private Set<Cell> visited;
    private Map<Cell, Cell> cameFrom;
    private boolean algorithmFinished;
    private Cell currentCell;
    
    @Override
    public List<Cell> solve(Cell[][] grid, Cell start, Cell goal) {
        Stack<Cell> stack = new Stack<>();
        Set<Cell> visited = new HashSet<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        
        stack.push(start);
        visited.add(start);
        
        while (!stack.isEmpty()) {
            Cell current = stack.pop();
            
            if (current.equals(goal)) {
                break;
            }
            
            for (Cell neighbor : current.getNeighbors(grid)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    stack.push(neighbor);
                }
            }
        }
        
        return reconstructPath(cameFrom, start, goal);
    }
    
    @Override
    public void initializeStepByStep(Cell[][] grid, Cell start, Cell goal) {
        this.grid = grid;
        this.start = start;
        this.goal = goal;
        this.stack = new Stack<>();
        this.visited = new HashSet<>();
        this.cameFrom = new HashMap<>();
        this.algorithmFinished = false;
        
        stack.push(start);
        visited.add(start);
        currentCell = start;
    }
    
    @Override
    public boolean executeStep() {
        if (algorithmFinished || stack.isEmpty()) {
            algorithmFinished = true;
            return true;
        }
        
        currentCell = stack.pop();
        
        if (currentCell.equals(goal)) {
            algorithmFinished = true;
            return true;
        }
        
        for (Cell neighbor : currentCell.getNeighbors(grid)) {
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);
                cameFrom.put(neighbor, currentCell);
                stack.push(neighbor);
            }
        }
        
        return false;
    }
    
    @Override
    public List<Cell> getCurrentPath() {
        if (cameFrom.containsKey(goal)) {
            return reconstructPath(cameFrom, start, goal);
        } else if (algorithmFinished) {
            return new ArrayList<>(); // No path found
        } else {
            // Return the current partial path to the current cell
            return reconstructPath(cameFrom, start, currentCell);
        }
    }
    
    @Override
    public List<Cell> getVisitedCells() {
        return new ArrayList<>(visited);
    }
    
    @Override
    public String getName() {
        return "DFS";
    }
    
    /**
     * Reconstructs the path from start to goal using the cameFrom map
     */
    private List<Cell> reconstructPath(Map<Cell, Cell> cameFrom, Cell start, Cell goal) {
        List<Cell> path = new ArrayList<>();
        
        Cell current = goal;
        if (!cameFrom.containsKey(goal) && goal != start) {
            return path; // No path exists
        }
        
        while (current != null && !current.equals(start)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        
        if (current != null) { // Add the start cell
            path.add(start);
        }
        
        Collections.reverse(path); // Reverse to get path from start to goal
        return path;
    }
}
