package com.example.demo.solver;

import com.example.demo.model.Cell;

import java.util.*;

/**
 * Implementation of Dijkstra's algorithm for maze solving
 */
public class DijkstraSolver implements MazeSolver {

    // Variables for step-by-step mode
    private Cell[][] grid;
    private Cell start;
    private Cell goal;
    private Map<Cell, Integer> dist;
    private Map<Cell, Cell> prev;
    private PriorityQueue<CellDist> queue;
    private Set<Cell> visited;
    private boolean algorithmFinished;
    private Cell currentCell;
    
    @Override
    public List<Cell> solve(Cell[][] grid, Cell start, Cell goal) {
        // Initialize data structures
        Map<Cell, Integer> dist = new HashMap<>();
        Map<Cell, Cell> prev = new HashMap<>();
        PriorityQueue<CellDist> queue = new PriorityQueue<>(Comparator.comparingInt(cd -> cd.dist));

        // Initialisation des distances
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                dist.put(grid[r][c], Integer.MAX_VALUE);
            }
        }

        dist.put(start, 0);
        queue.add(new CellDist(start, 0));

        while (!queue.isEmpty()) {
            CellDist current = queue.poll();
            Cell u = current.cell;

            if (u.equals(goal)) break;

            for (Cell v : u.getNeighbors(grid)) {
                int alt = dist.get(u) + 1;
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    queue.add(new CellDist(v, alt));
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
        this.dist = new HashMap<>();
        this.prev = new HashMap<>();
        this.queue = new PriorityQueue<>(Comparator.comparingInt(cd -> cd.dist));
        this.visited = new HashSet<>();
        this.algorithmFinished = false;
        
        // Initialisation des distances
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                dist.put(grid[r][c], Integer.MAX_VALUE);
            }
        }

        dist.put(start, 0);
        queue.add(new CellDist(start, 0));
        currentCell = start;
        visited.add(start);
    }
    
    @Override
    public boolean executeStep() {
        if (algorithmFinished || queue.isEmpty()) {
            algorithmFinished = true;
            return true;
        }
        
        CellDist current = queue.poll();
        Cell u = current.cell;
        currentCell = u;
        visited.add(u);
        
        if (u.equals(goal)) {
            algorithmFinished = true;
            return true;
        }
        
        for (Cell v : u.getNeighbors(grid)) {
            int alt = dist.get(u) + 1;
            if (alt < dist.get(v)) {
                dist.put(v, alt);
                prev.put(v, u);
                queue.add(new CellDist(v, alt));
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
        return "Dijkstra";
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

    private static class CellDist {
        Cell cell;
        int dist;

        CellDist(Cell cell, int dist) {
            this.cell = cell;
            this.dist = dist;
        }
    }
}
