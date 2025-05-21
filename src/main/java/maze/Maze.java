package src.main;



import src.main.java.AlgorithmGenerator.MazeConfigScene;

import java.util.ArrayList;
import java.util.List;

public class Maze {
    private int width, height;
    private Cell[][] grid;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];

        // Initialiser toutes les cellules
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell(x, y);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }


}
