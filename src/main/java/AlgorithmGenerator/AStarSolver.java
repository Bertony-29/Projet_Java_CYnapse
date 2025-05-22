package src.main.java.AlgorithmGenerator;

import src.main.java.maze.Cell;
import src.main.java.maze.Maze;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class AStarSolver {
    private final Maze maze;
    private final Pane mazePane;
    private final double cellSize;
    private final double offsetX;
    private final double offsetY;

    public static class Node {
        public final int x, y;
        public Node parent;
        public int g, h, f;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public AStarSolver(Maze maze, Pane mazePane, double cellSize, double offsetX, double offsetY) {
        this.maze = maze;
        this.mazePane = mazePane;
        this.cellSize = cellSize;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public List<Node> solve(int startX, int startY, int endX, int endY) {
        Node start = new Node(startX, startY);
        Node end = new Node(endX, endY);

        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
        Set<Node> closedList = new HashSet<>();

        start.g = 0;
        start.h = heuristic(start, end);
        start.f = start.g + start.h;
        openList.add(start);

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            if (current.equals(end)) {
                return reconstructPath(current);
            }

            closedList.add(current);

            for (Node neighbor : getAccessibleNeighbors(current)) {
                if (closedList.contains(neighbor)) continue;

                int tentativeG = current.g + 1;

                if (!openList.contains(neighbor) || tentativeG < neighbor.g) {
                    neighbor.parent = current;
                    neighbor.g = tentativeG;
                    neighbor.h = heuristic(neighbor, end);
                    neighbor.f = neighbor.g + neighbor.h;

                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Node> getAccessibleNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        Cell cell = maze.getCell(node.x, node.y);

        // Check North
        if (node.y > 0 && !cell.hasNorthWall()) {
            neighbors.add(new Node(node.x, node.y - 1));
        }
        // Check South
        if (node.y < maze.getHeight() - 1 && !maze.getCell(node.x, node.y + 1).hasNorthWall()) {
            neighbors.add(new Node(node.x, node.y + 1));
        }
        // Check West
        if (node.x > 0 && !cell.hasWestWall()) {
            neighbors.add(new Node(node.x - 1, node.y));
        }
        // Check East
        if (node.x < maze.getWidth() - 1 && !maze.getCell(node.x + 1, node.y).hasWestWall()) {
            neighbors.add(new Node(node.x + 1, node.y));
        }

        return neighbors;
    }

    private int heuristic(Node a, Node b) {

        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private List<Node> reconstructPath(Node endNode) {
        LinkedList<Node> path = new LinkedList<>();
        Node current = endNode;
        while (current != null) {
            path.addFirst(current);
            current = current.parent;
        }
        return path;
    }

    public void drawPath(List<Node> path) {
        // Clear previous path
        mazePane.getChildren().removeIf(node -> node instanceof Rectangle && ((Rectangle) node).getFill() == Color.RED);

        for (Node node : path) {
            Rectangle rect = new Rectangle(
                    offsetX + node.x * cellSize + cellSize * 0.2,
                    offsetY + node.y * cellSize + cellSize * 0.2,
                    cellSize * 0.6,
                    cellSize * 0.6
            );
            rect.setFill(Color.RED);
            rect.setStroke(Color.TRANSPARENT);
            mazePane.getChildren().add(rect);
        }
    }

    public boolean isCellAccessible(int x, int y) {
        if (x < 0 || x >= maze.getWidth() || y < 0 || y >= maze.getHeight()) {
            return false;
        }
        return getAccessibleNeighbors(new Node(x, y)).size() > 0;
    }
}