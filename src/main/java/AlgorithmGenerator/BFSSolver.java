package AlgorithmGenerator;

import maze.Cell;
import maze.Maze;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class BFSSolver {
    // These are the maze and the drawing area we'll work with
    private final Maze maze;
    private final Pane mazePane;
    private final double cellSize;   // Size of each maze cell for drawing
    private final double offsetX;    // Horizontal offset to center the maze
    private final double offsetY;    // Vertical offset to center the maze

    // This inner class represents a position (node) in the maze
    public static class Node {
        public final int x, y;   // Coordinates in the maze grid
        public Node parent;      // To keep track of the path, we remember the parent node

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // Override equals so two nodes are equal if their coordinates are the same
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return x == node.x && y == node.y;
        }

        // Override hashCode to match equals, important for using in HashSet
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // Constructor: I give this solver the maze info and where/how to draw it
    public BFSSolver(Maze maze, Pane mazePane, double cellSize, double offsetX, double offsetY) {
        this.maze = maze;
        this.mazePane = mazePane;
        this.cellSize = cellSize;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    // This is the main BFS solving method
    // It finds a path from (startX, startY) to (endX, endY)
    public List<Node> solve(int startX, int startY, int endX, int endY) {
        // Create start and end nodes
        Node start = new Node(startX, startY);
        Node end = new Node(endX, endY);

        // Queue for BFS (FIFO)
        Queue<Node> queue = new LinkedList<>();
        // Set to keep track of visited nodes to avoid loops
        Set<Node> visited = new HashSet<>();

        // Start BFS by adding the start node
        queue.add(start);
        visited.add(start);

        // Loop until no nodes left to visit
        while (!queue.isEmpty()) {
            Node current = queue.poll(); // Get next node to explore

            // Check if we reached the end!
            if (current.equals(end)) {
                // Reconstruct the path by following parent nodes backwards
                return reconstructPath(current);
            }

            // Otherwise, explore neighbors accessible from current node
            for (Node neighbor : getAccessibleNeighbors(current)) {
                // Only visit neighbors not visited yet
                if (!visited.contains(neighbor)) {
                    neighbor.parent = current; // Remember how we got here
                    queue.add(neighbor);       // Add to queue to explore later
                    visited.add(neighbor);     // Mark visited
                }
            }
        }

        // If we finish the while loop without returning, no path was found
        return Collections.emptyList();
    }

    // This method finds neighbors we can move to from the current node
    private List<Node> getAccessibleNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        Cell cell = maze.getCell(node.x, node.y);

        // Check North: if not at top edge and no wall to north
        if (node.y > 0 && !cell.hasNorthWall()) {
            neighbors.add(new Node(node.x, node.y - 1));
        }
        // Check South: if not at bottom edge and the cell below doesn't have north wall (which means no south wall here)
        if (node.y < maze.getHeight() - 1 && !maze.getCell(node.x, node.y + 1).hasNorthWall()) {
            neighbors.add(new Node(node.x, node.y + 1));
        }
        // Check West: if not at left edge and no wall to west
        if (node.x > 0 && !cell.hasWestWall()) {
            neighbors.add(new Node(node.x - 1, node.y));
        }
        // Check East: if not at right edge and the cell to the right doesn't have west wall (means no east wall here)
        if (node.x < maze.getWidth() - 1 && !maze.getCell(node.x + 1, node.y).hasWestWall()) {
            neighbors.add(new Node(node.x + 1, node.y));
        }

        return neighbors;
    }

    // After reaching the end, we reconstruct the path by following parents backwards
    private List<Node> reconstructPath(Node endNode) {
        LinkedList<Node> path = new LinkedList<>();
        Node current = endNode;
        while (current != null) {
            path.addFirst(current);  // Add current node at the start of the list
            current = current.parent; // Move to parent node
        }
        return path;
    }

    // This method draws the solution path on the mazePane as red rectangles
    public void drawPath(List<Node> path) {
        // First, remove old red rectangles (previous paths)
        mazePane.getChildren().removeIf(node -> node instanceof Rectangle && ((Rectangle) node).getFill() == Color.RED);

        // Draw red rectangles for each node in the path
        for (Node node : path) {
            Rectangle rect = new Rectangle(
                    offsetX + node.x * cellSize + cellSize * 0.2, // x position with some padding
                    offsetY + node.y * cellSize + cellSize * 0.2, // y position with some padding
                    cellSize * 0.6, // width smaller than cell size for better visibility
                    cellSize * 0.6  // height smaller than cell size
            );
            rect.setFill(Color.RED);
            rect.setStroke(Color.TRANSPARENT);
            mazePane.getChildren().add(rect);
        }
    }
}
