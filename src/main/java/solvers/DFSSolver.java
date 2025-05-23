package solvers;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import maze.Cell;
import maze.Maze;
import mazegenerator.PrimMazeGenerator.Direction;

import java.util.*;

public class DFSSolver {
    private final Maze maze;
    private final List<Cell> path = new ArrayList<>();
    public final Set<Cell> visited = new HashSet<>();

    public DFSSolver(Maze maze) {
        this.maze = maze;
    }

    public List<Cell> solve(Cell start, Cell end) {
        dfs(start, end);
        return path;
    }

    private boolean dfs(Cell current, Cell end) {
        visited.add(current);
        path.add(current);

        if (current.equals(end)) return true;

        for (Direction dir : Direction.values()) {
            if (!hasWall(current, dir)) {
                Cell neighbor = getNeighbor(current, dir);
                if (neighbor != null && !visited.contains(neighbor)) {
                    if (dfs(neighbor, end)) return true;
                }
            }
        }

        path.remove(path.size() - 1); // backtrack
        return false;
    }

    private boolean hasWall(Cell cell, Direction dir) {
        return switch (dir) {
            case NORTH -> cell.hasNorthWall();
            case SOUTH -> cell.hasSouthWall();
            case EAST  -> cell.hasEastWall();
            case WEST  -> cell.hasWestWall();
        };
    }

    private Cell getNeighbor(Cell cell, Direction dir) {
        int x = cell.getX();
        int y = cell.getY();
        return switch (dir) {
            case NORTH -> y > 0 ? maze.getCell(x, y - 1) : null;
            case SOUTH -> y < maze.getHeight() - 1 ? maze.getCell(x, y + 1) : null;
            case EAST  -> x < maze.getWidth() - 1 ? maze.getCell(x + 1, y) : null;
            case WEST  -> x > 0 ? maze.getCell(x - 1, y) : null;
        };
    }

    
    public void drawPath(Pane mazePane, double cellSize, double offsetX, double offsetY) {
        for (Cell cell : path) {
            Rectangle rect = new Rectangle(cellSize, cellSize, Color.YELLOW);
            rect.setX(cell.getX() * cellSize + offsetX);
            rect.setY(cell.getY() * cellSize + offsetY);
            mazePane.getChildren().add(rect);
        }
    }
}
