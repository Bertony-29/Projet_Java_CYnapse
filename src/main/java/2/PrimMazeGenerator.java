package com.example.demo;

import java.util.*;

public class PrimMazeGenerator {

    private static final Random random = new Random();

    public static void generateMaze(Cell[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Choisir une cellule de départ au hasard
        int startRow = random.nextInt(rows);
        int startCol = random.nextInt(cols);

        Cell startCell = grid[startRow][startCol];
        startCell.setVisited(true);

        List<Edge> frontier = new ArrayList<>();

        // Ajouter les murs de la cellule start à la liste frontier
        addFrontierEdges(startCell, grid, frontier);

        while (!frontier.isEmpty()) {
            // Choisir un mur aléatoire
            Edge edge = frontier.remove(random.nextInt(frontier.size()));

            Cell cell1 = edge.cell1;
            Cell cell2 = edge.cell2;

            if (cell2.isVisited()) continue;

            // Enlever mur entre cell1 et cell2
            removeWallBetween(cell1, cell2);

            cell2.setVisited(true);
            addFrontierEdges(cell2, grid, frontier);
        }

        // Reset visited pour d'autres usages
        for (int r=0; r < rows; r++) {
            for (int c=0; c < cols; c++) {
                grid[r][c].setVisited(false);
            }
        }
    }

    private static void addFrontierEdges(Cell cell, Cell[][] grid, List<Edge> frontier) {
        int r = cell.getRow();
        int c = cell.getCol();
        int rows = grid.length;
        int cols = grid[0].length;

        if (r > 0) frontier.add(new Edge(cell, grid[r-1][c]));
        if (r < rows -1) frontier.add(new Edge(cell, grid[r+1][c]));
        if (c > 0) frontier.add(new Edge(cell, grid[r][c-1]));
        if (c < cols -1) frontier.add(new Edge(cell, grid[r][c+1]));
    }

    private static void removeWallBetween(Cell c1, Cell c2) {
        int dr = c2.getRow() - c1.getRow();
        int dc = c2.getCol() - c1.getCol();

        if (dr == 1) { // c2 est en bas de c1
            c1.removeSouthWall();
            c2.removeNorthWall();
        } else if (dr == -1) { // c2 est en haut de c1
            c1.removeNorthWall();
            c2.removeSouthWall();
        } else if (dc == 1) { // c2 est à droite de c1
            c1.removeEastWall();
            c2.removeWestWall();
        } else if (dc == -1) { // c2 est à gauche de c1
            c1.removeWestWall();
            c2.removeEastWall();
        }
    }

    private static class Edge {
        Cell cell1, cell2;

        Edge(Cell c1, Cell c2) {
            cell1 = c1;
            cell2 = c2;
        }
    }
}
