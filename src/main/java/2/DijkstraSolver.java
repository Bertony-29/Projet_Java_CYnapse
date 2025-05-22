package src.main;

import java.util.*;

public class DijkstraSolver {

    public static List<Cell> solve(Cell[][] grid, Cell start, Cell goal) {
        Map<Cell, Integer> dist = new HashMap<>();
        Map<Cell, Cell> prev = new HashMap<>();
        PriorityQueue<CellDist> queue = new PriorityQueue<>(Comparator.comparingInt(cd -> cd.dist));

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

        // Reconstruction du chemin
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
