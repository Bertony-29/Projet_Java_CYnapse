package main ;



import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;
public class DijkstraSolver {

    public static List<Cell> solve(Cell[][] grid, Cell start, Cell end) {
        Map<Cell, Double> distances = new HashMap<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        PriorityQueue<Cell> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                Cell cell = grid[row][col];
                distances.put(cell, Double.POSITIVE_INFINITY);
            }
        }

        distances.put(start, 0.0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            if (current == end) {
                return reconstructPath(cameFrom, end);
            }

            for (Cell neighbor : current.getNeighbors(grid)) {
                double tentative = distances.get(current) + 1;
                if (tentative < distances.get(neighbor)) {
                    distances.put(neighbor, tentative);
                    cameFrom.put(neighbor, current);
                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return Collections.emptyList();
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
