package src.main.java.solvers;

import java.util.*;
import src.main.java.maze.Maze;
import src.main.java.maze.Cell;

public class DijkstraMazeSolver {

    private Maze maze;

    public DijkstraMazeSolver(Maze maze) {
        this.maze = maze;
    }

    public List<Cell> findShortestPath(Cell start, Cell goal) {
        int width = maze.getWidth();
        int height = maze.getHeight();

        // Distance minimale estimée à chaque cellule
        Map<Cell, Integer> dist = new HashMap<>();

        // Pour retrouver le chemin
        Map<Cell, Cell> previous = new HashMap<>();

        // Comparateur pour la PriorityQueue
        Comparator<Cell> cellComparator = Comparator.comparingInt(dist::get);

        PriorityQueue<Cell> queue = new PriorityQueue<>(cellComparator);

        // Initialisation
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell c = maze.getCell(x, y);
                dist.put(c, Integer.MAX_VALUE);
            }
        }
        dist.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            if (current.equals(goal)) {
                // On a atteint l’arrivée, on construit le chemin
                return reconstructPath(previous, goal);
            }

            for (Cell neighbor : getNeighbors(current)) {
                int alt = dist.get(current) + 1; // poids uniforme = 1

                if (alt < dist.get(neighbor)) {
                    dist.put(neighbor, alt);
                    previous.put(neighbor, current);
                    // Mettre à jour la queue en supprimant puis réajoutant
                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        // Pas de chemin trouvé
        return Collections.emptyList();
    }

    // Retourne la liste des voisins accessibles sans mur
    private List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int x = cell.getX();
        int y = cell.getY();
        int width = maze.getWidth();
        int height = maze.getHeight();

        // Nord
        if (!cell.hasNorthWall() && y > 0) {
            neighbors.add(maze.getCell(x, y - 1));
        }
        // Est
        if (!cell.hasEastWall() && x < width - 1) {
            neighbors.add(maze.getCell(x + 1, y));
        }
        // Sud
        if (!cell.hasSouthWall() && y < height - 1) {
            neighbors.add(maze.getCell(x, y + 1));
        }
        // Ouest
        if (!cell.hasWestWall() && x > 0) {
            neighbors.add(maze.getCell(x - 1, y));
        }

        return neighbors;
    }

    // Reconstruit le chemin à partir de la map des prédécesseurs
    private List<Cell> reconstructPath(Map<Cell, Cell> previous, Cell goal) {
        List<Cell> path = new LinkedList<>();
        Cell current = goal;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }
        return path;
    }
}
