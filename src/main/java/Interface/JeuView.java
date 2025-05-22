package src.main ;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;

public class JeuView {
    private static Cell[][] grid;
    private static Cell startCell;
    private static Cell goalCell;
    private static VBox statsBox;

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
                    // Mets à jour les stats ici si besoin
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

    private static void removeWallsAround(Cell cell) {
        int r = cell.row;
        int c = cell.col;
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int[] d : directions) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr >= 0 && nr < grid.length && nc >= 0 && nc < grid[0].length) {
                grid[nr][nc].isWall = false;
                grid[nr][nc].rect.setFill(Color.WHITE);
            }
        }
        cell.isWall = false;
    }

    private static Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }
}
