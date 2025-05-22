package src.main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

public class MainApp extends Application {

    private BorderPane root;
    private Cell[][] grid;
    private Cell startCell, goalCell;
    private int rows = 20;
    private int cols = 20;

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        root.setTop(createMenuBar(primaryStage));
        showAccueilView();

        primaryStage.setTitle("YBAY Maze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("☰");

        MenuItem accueilItem = new MenuItem("Menu Principal");
        accueilItem.setOnAction(e -> showAccueilView());

        menu.getItems().add(accueilItem);
        menuBar.getMenus().add(menu);
        return menuBar;
    }

    private void showAccueilView() {
        VBox accueilView = new VBox(10);
        accueilView.setPadding(new Insets(20));

        Button btnGenerate = new Button("Générer un labyrinthe");
        btnGenerate.setOnAction(e -> showJeuView());

        accueilView.getChildren().addAll(new Label("Bienvenue sur YBAY Maze"), btnGenerate);
        root.setCenter(accueilView);
    }

    private void showJeuView() {
        rows = 20;
        cols = 20;

        Pane mazePane = new Pane();
        mazePane.setPrefSize(600, 600);
        mazePane.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        grid = new Cell[rows][cols];
        double cellWidth = mazePane.getPrefWidth() / cols;
        double cellHeight = mazePane.getPrefHeight() / rows;

        // Création des cellules avec murs
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Rectangle rect = new Rectangle(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.GRAY);
                Cell cell = new Cell(r, c, rect);
                grid[r][c] = cell;
                mazePane.getChildren().add(rect);
            }
        }

        // Génération labyrinthe
        PrimMazeGenerator.generateMaze(grid);

        // Mise à jour visuelle des murs (très simple : on colore en noir les murs N,S,O,E)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                // Pour simplifier, on colore en noir la cellule si elle est un mur (utile si on veut)
                // Mais ici on colorie uniquement les murs sur les bords des cellules:
                double strokeWidth = 2;

                // On dessine les murs sous forme de rectangles noirs (bordure)
                // Pour l'exemple on va juste faire les contours avec des lignes noires

                if (cell.hasNorthWall()) {
                    Rectangle topWall = new Rectangle(c * cellWidth, r * cellHeight, cellWidth, strokeWidth);
                    topWall.setFill(Color.BLACK);
                    mazePane.getChildren().add(topWall);
                }
                if (cell.hasSouthWall()) {
                    Rectangle bottomWall = new Rectangle(c * cellWidth, (r + 1) * cellHeight - strokeWidth, cellWidth, strokeWidth);
                    bottomWall.setFill(Color.BLACK);
                    mazePane.getChildren().add(bottomWall);
                }
                if (cell.hasWestWall()) {
                    Rectangle leftWall = new Rectangle(c * cellWidth, r * cellHeight, strokeWidth, cellHeight);
                    leftWall.setFill(Color.BLACK);
                    mazePane.getChildren().add(leftWall);
                }
                if (cell.hasEastWall()) {
                    Rectangle rightWall = new Rectangle((c + 1) * cellWidth - strokeWidth, r * cellHeight, strokeWidth, cellHeight);
                    rightWall.setFill(Color.BLACK);
                    mazePane.getChildren().add(rightWall);
                }
            }
        }

        // Interaction clic sur cellules pour définir start et goal
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                cell.rect.setOnMouseClicked(e -> {
                    if (startCell == null) {
                        startCell = cell;
                        cell.rect.setFill(Color.BLUE);
                    } else if (goalCell == null && cell != startCell) {
                        goalCell = cell;
                        cell.rect.setFill(Color.RED);
                    }
                });
            }
        }

        Button btnSolve = new Button("Résoudre");
        btnSolve.setOnAction(e -> {
            if (startCell != null && goalCell != null) {
                List<Cell> path = DijkstraSolver.solve(grid, startCell, goalCell);
                if (path.isEmpty()) {
                    System.out.println("Aucun chemin trouvé !");
                } else {
                    for (Cell c : path) {
                        if (c != startCell && c != goalCell) {
                            c.rect.setFill(Color.YELLOW);
                        }
                    }
                    System.out.println("Chemin trouvé, longueur: " + path.size());
                }
            } else {
                System.out.println("Veuillez sélectionner start (bleu) et goal (rouge)");
            }
        });

        VBox rightPanel = new VBox(10, btnSolve);
        rightPanel.setPadding(new Insets(10));

        BorderPane gameLayout = new BorderPane();
        gameLayout.setCenter(mazePane);
        gameLayout.setRight(rightPanel);

        root.setCenter(gameLayout);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
