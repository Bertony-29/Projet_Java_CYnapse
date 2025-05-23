package com.example.demo.solver;

import com.example.demo.model.Cell;

import java.util.*;

/**
 * Implementation of the BFS (Breadth-First Search) algorithm for maze solving
 */
public class BFSSolver implements MazeSolver {

    // Variables for step-by-step mode
    private Cell[][] grid;
    private Cell start;
    private Cell goal;
    private Queue<Cell> queue;
    private Map<Cell, Cell> prev;
    private Set<Cell> visited;
    private boolean algorithmFinished;
    private Cell currentCell;

    @Override
    public List<Cell> solve(Cell[][] grid, Cell start, Cell goal) {
        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> prev = new HashMap<>();
        Set<Cell> visited = new HashSet<>();
        
        queue.add(start);
        visited.add(start);
        
        while (!queue.isEmpty()) {
            Cell current = queue.poll();
            
            if (current.equals(goal)) {
                break;
            }
            
            for (Cell neighbor : current.getNeighbors(grid)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    prev.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        
        return reconstructPath(prev, start, goal);
    }
    
    @Override
    public void initializeStepByStep(Cell[][] grid, Cell start, Cell goal) {
        this.grid = grid;
        this.start = start;
        this.goal = goal;
        this.queue = new LinkedList<>();
        this.prev = new HashMap<>();
        this.visited = new HashSet<>();
        this.algorithmFinished = false;
        
        queue.add(start);
        visited.add(start);
        currentCell = start;
    }
    
    @Override
    public boolean executeStep() {
        if (algorithmFinished || queue.isEmpty()) {
            algorithmFinished = true;
            return true;
        }
        
        Cell current = queue.poll();
        currentCell = current;
        
        if (current.equals(goal)) {
            algorithmFinished = true;
            return true;
        }
        
        for (Cell neighbor : current.getNeighbors(grid)) {
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);
                prev.put(neighbor, current);
                queue.add(neighbor);
            }
        }
        
        return false;
    }
    
    @Override
    public List<Cell> getCurrentPath() {
        if (algorithmFinished) {
            return reconstructPath(prev, start, goal);
        } else {
            // Renvoyer le chemin partiel jusqu'à la cellule actuelle
            List<Cell> partialPath = new ArrayList<>();
            Cell step = currentCell;
            while (step != null) {
                partialPath.add(step);
                step = prev.get(step);
            }
            Collections.reverse(partialPath);
            return partialPath;
        }
    }
    
    @Override
    public List<Cell> getVisitedCells() {
        return new ArrayList<>(visited);
    }
    
    @Override
    public void setCurrentPath(List<Cell> path) {
        if (path == null || path.isEmpty()) {
            return;
        }
        
        // Réinitialiser les structures de données
        this.prev = new HashMap<>();
        
        // Reconstruire le chemin en sens inverse
        for (int i = 0; i < path.size() - 1; i++) {
            prev.put(path.get(i + 1), path.get(i));
        }
        
        // Marquer l'algorithme comme terminé
        this.algorithmFinished = true;
        this.currentCell = path.get(path.size() - 1);
    }
    
    @Override
    public void setVisitedCells(List<Cell> cells) {
        if (cells == null) {
            this.visited = new HashSet<>();
        } else {
            this.visited = new HashSet<>(cells);
        }
    }

    @Override
    public String getName() {
        return "BFS";
    }
    
    // Méthode utilitaire pour reconstruire le chemin
    private List<Cell> reconstructPath(Map<Cell, Cell> prev, Cell start, Cell goal) {
        List<Cell> path = new ArrayList<>();
        Cell step = goal;
        if (!prev.containsKey(step) && !step.equals(start)) {
            // pas de chemin trouvé
            return path;
        }
        while (step != null) {
            path.add(step);
            step = prev.get(step);
        }
        Collections.reverse(path);
        return path;
    }
}
