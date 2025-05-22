package src.main;

import java.util.*;

public class DijkstraSolver {

    public static List<Cell> solve(Cell[][] grid, Cell start, Cell goal) {
        Map<Cell, Double> distances = new HashMap<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        PriorityQueue<Cell> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        int rows = grid.length;
        int cols = grid[0].length;

        // Initialisation : toutes les distances à ∞ sauf la cellule de départ
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Cell cell = grid[y][x];
                distances.put(cell, Double.POSITIVE_INFINITY);
            }
        }
        distances.put(start, 0.0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            if (current.equals(goal)) {
                return reconstructPath(cameFrom, goal);
            }

            for (Cell neighbor : current.getAccessibleNeighbors(grid)) {
                double tentativeDistance = distances.get(current) + 1; // Toutes les distances valent 1
                if (tentativeDistance < distances.get(neighbor)) {
                    distances.put(neighbor, tentativeDistance);
                    cameFrom.put(neighbor, current);
                    queue.remove(neighbor); // Au cas où il y est déjà, on le met à jour
                    queue.add(neighbor);
                }
            }
        }

        return Collections.emptyList(); // Pas de chemin trouvé
    }

    private static List<Cell> reconstructPath(Map<Cell, Cell> cameFrom, Cell end) {
        List<Cell> path = new ArrayList<>();
        Cell current = end;
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(path);
        return path;
    }
}
