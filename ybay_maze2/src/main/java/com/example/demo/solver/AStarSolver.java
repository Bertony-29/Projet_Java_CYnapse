package com.example.demo.solver;

import com.example.demo.model.Cell;

import java.util.*;

/**
 * Implementation of the A* algorithm for maze solving
 */
public class AStarSolver implements MazeSolver {

    // Variables for step-by-step mode
    private Cell[][] grid;
    private Cell start;
    private Cell goal;
    private PriorityQueue<CellNode> openSet;
    private Set<Cell> closedSet;
    private Map<Cell, Cell> cameFrom;
    private Map<Cell, Integer> gScore;
    private Map<Cell, Integer> fScore;
    private boolean algorithmFinished;
    private Cell currentCell;

    @Override
    public List<Cell> solve(Cell[][] grid, Cell start, Cell goal) {
        PriorityQueue<CellNode> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.fScore));
        Set<Cell> closedSet = new HashSet<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        Map<Cell, Integer> gScore = new HashMap<>();
        Map<Cell, Integer> fScore = new HashMap<>();
        
        // Initialisation
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                gScore.put(grid[r][c], Integer.MAX_VALUE);
                fScore.put(grid[r][c], Integer.MAX_VALUE);
            }
        }
        
        gScore.put(start, 0);
        fScore.put(start, heuristic(start, goal));
        openSet.add(new CellNode(start, fScore.get(start)));
        
        while (!openSet.isEmpty()) {
            Cell current = openSet.poll().cell;
            
            if (current.equals(goal)) {
                break;
            }
            
            closedSet.add(current);
            
            for (Cell neighbor : current.getNeighbors(grid)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                
                int tentativeGScore = gScore.get(current) + 1;
                
                if (tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, gScore.get(neighbor) + heuristic(neighbor, goal));
                    
                    // Si le voisin n'est pas dans l'openSet, l'ajouter
                    boolean found = false;
                    for (CellNode node : openSet) {
                        if (node.cell.equals(neighbor)) {
                            found = true;
                            node.fScore = fScore.get(neighbor);
                            break;
                        }
                    }
                    
                    if (!found) {
                        openSet.add(new CellNode(neighbor, fScore.get(neighbor)));
                    }
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
        this.openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.fScore));
        this.closedSet = new HashSet<>();
        this.cameFrom = new HashMap<>();
        this.gScore = new HashMap<>();
        this.fScore = new HashMap<>();
        this.algorithmFinished = false;
        this.currentCell = start;
        
        // Initialisation
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                gScore.put(grid[r][c], Integer.MAX_VALUE);
                fScore.put(grid[r][c], Integer.MAX_VALUE);
            }
        }
        
        gScore.put(start, 0);
        fScore.put(start, heuristic(start, goal));
        openSet.add(new CellNode(start, fScore.get(start)));
    }
    
    @Override
    public boolean executeStep() {
        if (algorithmFinished || openSet.isEmpty()) {
            algorithmFinished = true;
            return true;
        }
        
        Cell current = openSet.poll().cell;
        currentCell = current;
        
        if (current.equals(goal)) {
            algorithmFinished = true;
            return true;
        }
        
        closedSet.add(current);
        
        for (Cell neighbor : current.getNeighbors(grid)) {
            if (closedSet.contains(neighbor)) {
                continue;
            }
            
            int tentativeGScore = gScore.get(current) + 1;
            
            if (tentativeGScore < gScore.get(neighbor)) {
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, gScore.get(neighbor) + heuristic(neighbor, goal));
                
                // Si le voisin n'est pas dans l'openSet, l'ajouter
                boolean found = false;
                for (CellNode node : openSet) {
                    if (node.cell.equals(neighbor)) {
                        found = true;
                        node.fScore = fScore.get(neighbor);
                        break;
                    }
                }
                
                if (!found) {
                    openSet.add(new CellNode(neighbor, fScore.get(neighbor)));
                }
            }
        }
        
        return false;
    }
    
    @Override
    public List<Cell> getCurrentPath() {
        if (algorithmFinished) {
            return reconstructPath(cameFrom, start, goal);
        } else {
            // Renvoyer le chemin partiel jusqu'à la cellule actuelle
            List<Cell> partialPath = new ArrayList<>();
            Cell step = currentCell;
            while (step != null) {
                partialPath.add(step);
                step = cameFrom.get(step);
            }
            Collections.reverse(partialPath);
            return partialPath;
        }
    }
    
    @Override
    public List<Cell> getVisitedCells() {
        return new ArrayList<>(closedSet);
    }
    
    @Override
    public void setCurrentPath(List<Cell> path) {
        if (path == null || path.isEmpty()) {
            return;
        }
        
        // Réinitialiser les structures de données
        this.cameFrom = new HashMap<>();
        
        // Reconstruire le chemin en sens inverse
        for (int i = 0; i < path.size() - 1; i++) {
            cameFrom.put(path.get(i + 1), path.get(i));
        }
        
        // Marquer l'algorithme comme terminé
        this.algorithmFinished = true;
        this.currentCell = path.get(path.size() - 1);
    }
    
    @Override
    public void setVisitedCells(List<Cell> cells) {
        if (cells == null) {
            this.closedSet = new HashSet<>();
        } else {
            this.closedSet = new HashSet<>(cells);
        }
    }
    
    /**
     * Calcule l'heuristique (distance de Manhattan) entre deux cellules
     */
    private int heuristic(Cell a, Cell b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
    
    @Override
    public String getName() {
        return "A*";
    }
    
    // Méthode utilitaire pour reconstruire le chemin
    private List<Cell> reconstructPath(Map<Cell, Cell> cameFrom, Cell start, Cell goal) {
        List<Cell> path = new ArrayList<>();
        Cell step = goal;
        if (!cameFrom.containsKey(step) && !step.equals(start)) {
            // pas de chemin trouvé
            return path;
        }
        while (step != null) {
            path.add(step);
            step = cameFrom.get(step);
        }
        Collections.reverse(path);
        return path;
    }
    
    private static class CellNode {
        Cell cell;
        int fScore;
        
        CellNode(Cell cell, int fScore) {
            this.cell = cell;
            this.fScore = fScore;
        }
    }
}
