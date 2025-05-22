package com.example.demo;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class JeuView {
    private static Cell[][] grid;
    private static Cell startCell;
    private static Cell goalCell;

    public static BorderPane getView(int rows, int cols) {
        Pane mazePane = new Pane();
        mazePane.setStyle("-fx-border-color: #2c3e50; -fx-border-width: 2px;");

        grid = new Cell[rows][cols];
        drawGrid(mazePane, rows, cols);

        Button solveBtn = createButton("Résoudre", "#2ecc71");

        solveBtn.setOnAction(e -> {
            if (startCell != null && goalCell != null) {
                List<Cell> path = DijkstraSolver.solve(grid, startCell, goalCell);
                if (path != null) {
                    for (Cell c : path) {
                        if (c != startCell && c != goalCell) {
                            c.rect.setFill(Color.YELLOW);
                        }
                    }
                }
            }
        });

        VBox rightPanel = new VBox(15, solveBtn);
        rightPanel.setPadding(new Insets(10));

        BorderPane layout = new BorderPane();
        layout.setCenter(mazePane);
        layout.setRight(rightPanel);
        layout.setPadding(new Insets(10));

        return layout;
    }

    private static void drawGrid(Pane pane, int rows, int cols) {
        pane.getChildren().clear();
        double width = 569;
        double height = 554;
        double cellWidth = width / cols;
        double cellHeight = height / rows;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Rectangle rect = new Rectangle(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.GRAY);
                Cell cell = new Cell(row, col, rect);
                grid[row][col] = cell;
                pane.getChildren().add(rect);

                rect.setOnMouseClicked(e -> {
                    if (startCell == null) {
                        startCell = cell;
                        rect.setFill(Color.BLUE);
                        removeWallsAround(cell);
                    } else if (goalCell == null && cell != startCell) {
                        goalCell = cell;
                        rect.setFill(Color.RED);
                    }
                });
            }
        }
    }

    // Suppression des murs entre la cellule et ses voisins directs (nord, sud, est, ouest)
    private static void removeWallsAround(Cell cell) {
        int r = cell.getRow();
        int c = cell.getCol();
        int rows = grid.length;
        int cols = grid[0].length;

        // Enlève les murs entre la cellule et ses voisins
        if (r > 0) {
            cell.removeNorthWall();
            grid[r - 1][c].removeSouthWall();
            grid[r - 1][c].rect.setFill(Color.WHITE);
        }
        if (r < rows - 1) {
            cell.removeSouthWall();
            grid[r + 1][c].removeNorthWall();
            grid[r + 1][c].rect.setFill(Color.WHITE);
        }
        if (c > 0) {
            cell.removeWestWall();
            grid[r][c - 1].removeEastWall();
            grid[r][c - 1].rect.setFill(Color.WHITE);
        }
        if (c < cols - 1) {
            cell.removeEastWall();
            grid[r][c + 1].removeWestWall();
            grid[r][c + 1].rect.setFill(Color.WHITE);
        }

        // La cellule elle-même ne doit pas être un mur
        cell.rect.setFill(Color.WHITE);
    }

    private static Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }
}
