package src.main.java.generator;

import com.example.demo.model.Cell;
import java.util.*;

/**
 * Implementation of Kruskal's algorithm for maze generation
 */
public class KruskalMazeGenerator implements MazeGenerator {

    private static final Random random = new Random();

    @Override
    public void generateMaze(Cell[][] grid, boolean perfectMaze) {
        // Generate a perfect maze first
        generatePerfectMaze(grid);
        
        // If we want an imperfect maze, remove some walls randomly
        if (!perfectMaze) {
            createImperfectMaze(grid);
        }
    }
    
    @Override
    public String getName() {
        return "Kruskal";
    }

    /**
     * Generates a perfect maze using Kruskal's algorithm
     */
    private void generatePerfectMaze(Cell[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        
        // Create disjoint-set data structure for tracking connected components
        DisjointSet disjointSet = new DisjointSet(rows * cols);
        
        // Create a list of all possible walls (edges)
        List<Wall> walls = new ArrayList<>();
        
        // Add all walls to the list
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int cellIndex = r * cols + c;
                
                // Add east wall if not on the right edge
                if (c < cols - 1) {
                    walls.add(new Wall(cellIndex, cellIndex + 1, 'E'));
                }
                
                // Add south wall if not on the bottom edge
                if (r < rows - 1) {
                    walls.add(new Wall(cellIndex, cellIndex + cols, 'S'));
                }
            }
        }
        
        // Shuffle the walls to ensure randomness
        Collections.shuffle(walls, random);
        
        // Process each wall
        for (Wall wall : walls) {
            int set1 = disjointSet.find(wall.cell1);
            int set2 = disjointSet.find(wall.cell2);
            
            // If cells are in different sets, remove the wall between them
            if (set1 != set2) {
                int r1 = wall.cell1 / cols;
                int c1 = wall.cell1 % cols;
                int r2 = wall.cell2 / cols;
                int c2 = wall.cell2 % cols;
                
                // Remove the wall
                if (wall.direction == 'E') {
                    grid[r1][c1].removeEastWall();
                    grid[r2][c2].removeWestWall();
                } else if (wall.direction == 'S') {
                    grid[r1][c1].removeSouthWall();
                    grid[r2][c2].removeNorthWall();
                }
                
                // Merge the sets
                disjointSet.union(set1, set2);
            }
        }
    }
    
    /**
     * Transforms a perfect maze into an imperfect maze by removing walls randomly
     */
    private void createImperfectMaze(Cell[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        
        // Determine the number of walls to remove (about 10-15% of cells)
        int wallsToRemove = (int) (rows * cols * 0.1);
        
        for (int i = 0; i < wallsToRemove; i++) {
            // Choose a random cell
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            Cell cell = grid[r][c];
            
            // Choose a random direction (0=North, 1=East, 2=South, 3=West)
            int direction = random.nextInt(4);
            
            // Check if we can remove the wall in this direction
            switch (direction) {
                case 0: // North
                    if (r > 0 && cell.hasNorthWall()) {
                        cell.removeNorthWall();
                        grid[r-1][c].removeSouthWall();
                    }
                    break;
                case 1: // East
                    if (c < cols-1 && cell.hasEastWall()) {
                        cell.removeEastWall();
                        grid[r][c+1].removeWestWall();
                    }
                    break;
                case 2: // South
                    if (r < rows-1 && cell.hasSouthWall()) {
                        cell.removeSouthWall();
                        grid[r+1][c].removeNorthWall();
                    }
                    break;
                case 3: // West
                    if (c > 0 && cell.hasWestWall()) {
                        cell.removeWestWall();
                        grid[r][c-1].removeEastWall();
                    }
                    break;
            }
        }
    }
    
    /**
     * Represents a wall between two cells
     */
    private static class Wall {
        int cell1, cell2;
        char direction; // 'E' for east, 'S' for south
        
        Wall(int c1, int c2, char dir) {
            cell1 = c1;
            cell2 = c2;
            direction = dir;
        }
    }
    
    /**
     * Disjoint-Set data structure for Kruskal's algorithm
     */
    private static class DisjointSet {
        private int[] parent;
        private int[] rank;
        
        DisjointSet(int size) {
            parent = new int[size];
            rank = new int[size];
            
            // Initialize each element as a separate set
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }
        
        // Find the representative of the set containing element x
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }
        
        // Merge the sets containing elements x and y
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) return;
            
            // Union by rank
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
}
