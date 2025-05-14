package src.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PrimMazeGenerator {
    private Maze maze;
    private int width;
    private int height;

    private Random random;
    private int startX;

    private int startY;


    public PrimMazeGenerator(Maze maze) {
        this.maze = maze;
        this.width = maze.getWidth();
        this.height = maze.getHeight();
        this.random = new Random();
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }



    public void generate() {
        Cell startCell = maze.getCell(startX, startY);
        startCell.setVisited(true);


        List<Wall> walls = getCellWalls(startX, startY);

        while (!walls.isEmpty()) {
            Wall wall = walls.remove(random.nextInt(walls.size()));
            int x = wall.getX();
            int y = wall.getY();
            Direction direction = wall.getDirection();

            Cell cell = maze.getCell(x, y);

            int nx = getNeighborX(x, direction);
            int ny = getNeighborY(y, direction);
            if (isValid(nx, ny)) {
                Cell neighbor = maze.getCell(nx, ny);

                if (!neighbor.isVisited()) {
                    removeWall(cell, neighbor, direction);

                    neighbor.setVisited(true);

                    walls.addAll(getCellWalls(nx, ny));
                }
            }
        }
    }

    public void noPerfect() {
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Cell cell = maze.getCell(x, y);
            List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.values()));
            for (Direction dir : directions) {

                int nx = getNeighborX(x, dir);
                int ny = getNeighborY(y, dir);

                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    Cell neighbor = maze.getCell(nx, ny);

                    if (hasWall(cell, dir)) {
                        // Utiliser removeWall existant
                        removeWall(cell, neighbor, dir);
                    }
                }
            }
        }
    }

    public boolean hasWall(Cell cell, Direction direction){
        switch (direction){
            case NORTH -> {
                return cell.hasNorthWall();
            }
            case SOUTH -> {
                return cell.hasSouthWall();
            }
            case WEST -> {
                return cell.hasWestWall();
            }
            case EAST -> {
                return cell.hasEastWall();
            }
        }
        return false;
    }



    private List<Wall> getCellWalls(int x, int y) {
        List<Wall> walls = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            int nx = getNeighborX(x, direction);
            int ny = getNeighborY(y, direction);
            if (isValid(nx, ny)) {
                walls.add(new Wall(x, y, direction));
            }
        }
        return walls;
    }

    private int getNeighborX(int x, Direction direction) {
        return x + (direction == Direction.EAST ? 1 : (direction == Direction.WEST ? -1 : 0));
    }

    private int getNeighborY(int y, Direction direction) {
        return y + (direction == Direction.SOUTH ? 1 : (direction == Direction.NORTH ? -1 : 0));
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private void removeWall(Cell cell, Cell neighbor, Direction direction) {
        cell.removeWall(direction);
        neighbor.removeWall(getOppositeDirection(direction));
    }

    private Direction getOppositeDirection(Direction direction) {
        switch (direction) {
            case NORTH: return Direction.SOUTH;
            case SOUTH: return Direction.NORTH;
            case EAST: return Direction.WEST;
            case WEST: return Direction.EAST;
            default: throw new IllegalArgumentException("Direction invalide");
        }
    }

    private static class Wall {
        private int x;
        private int y;
        private Direction direction;

        public Wall(int x, int y, Direction direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        public int getX() { return x; }
        public int getY() { return y; }
        public Direction getDirection() { return direction; }
    }

    // Enumération pour les directions
    public enum Direction {
        NORTH, EAST, SOUTH, WEST
    }


}