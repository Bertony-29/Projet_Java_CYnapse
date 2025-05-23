package com.example.demo.view;

import com.example.demo.MainApp;
import com.example.demo.model.Cell;
import com.example.demo.generator.MazeGenerator;
import com.example.demo.generator.GeneratorFactory;
import com.example.demo.solver.MazeSolver;
import com.example.demo.solver.SolverFactory;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * View for the maze game screen
 */
public class JeuView {
    
    private BorderPane root;
    private MainApp mainApp;
    
    public JeuView(BorderPane root, MainApp mainApp) {
        this.root = root;
        this.mainApp = mainApp;
        createView();
    }
    
    private void createView() {
        // Get parameters from MainApp
        int rows = mainApp.getRows();
        int cols = mainApp.getCols();
        boolean perfectMaze = mainApp.isPerfectMaze();
        String selectedGenerator = mainApp.getSelectedGenerator();
        String selectedAlgorithm = mainApp.getSelectedAlgorithm();
        boolean stepByStep = mainApp.isStepByStep();
        
        // Create the maze pane
        Pane mazePane = new Pane();
        mazePane.setPrefSize(600, 600);
        mazePane.getStyleClass().add("maze-pane");

        // Create grid of cells
        Cell[][] grid = new Cell[rows][cols];
        double cellWidth = mazePane.getPrefWidth() / cols;
        double cellHeight = mazePane.getPrefHeight() / rows;

        // Creation of cells with walls
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
        
        // Store the grid in MainApp
        mainApp.setGrid(grid);

        // Maze generation using the selected generator
        MazeGenerator generator = GeneratorFactory.createGenerator(selectedGenerator);
        generator.generateMaze(grid, perfectMaze);

        // Visual update of walls
        drawMazeWalls(mazePane, grid, rows, cols);

        // Click interaction on cells to define start and goal
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                cell.rect.setOnMouseClicked(e -> {
                    if (mainApp.getStartCell() == null) {
                        mainApp.setStartCell(cell);
                        cell.rect.setFill(Color.BLUE);
                        // Force immediate refresh
                        mazePane.layout();
                    } else if (mainApp.getGoalCell() == null && cell != mainApp.getStartCell()) {
                        mainApp.setGoalCell(cell);
                        cell.rect.setFill(Color.RED);
                        // Force immediate refresh
                        mazePane.layout();
                    }
                });
            }
        }

        // Display the selected algorithm
        Label algorithmLabel = new Label("Selected algorithm: " + selectedAlgorithm);
        algorithmLabel.setStyle("-fx-font-weight: bold;");
        
        // Step counter for step-by-step mode
        Label stepCountLabel = new Label("No steps executed");
        stepCountLabel.setVisible(false);
        mainApp.setStepCountLabel(stepCountLabel);
        
        // Solve button
        Button btnSolve = new Button("Solve");
        btnSolve.setOnAction(e -> {
            if (mainApp.getStartCell() != null && mainApp.getGoalCell() != null) {
                // Reset cell colors (except start and goal)
                resetCellColors(grid, rows, cols);
                
                // Use the algorithm selected from the configuration screen
                MazeSolver currentSolver = SolverFactory.getSolverByName(selectedAlgorithm);
                mainApp.setCurrentSolver(currentSolver);
                
                if (stepByStep) {
                    // Step-by-step mode
                    currentSolver.initializeStepByStep(grid, mainApp.getStartCell(), mainApp.getGoalCell());
                    stepCountLabel.setText("No steps executed");
                    stepCountLabel.setVisible(true);
                    mainApp.getBtnNextStep().setVisible(true);
                    mainApp.getBtnNextStep().setDisable(false);
                    mainApp.getBtnCompleteExecution().setVisible(true);
                    mainApp.getBtnCompleteExecution().setDisable(false);
                    btnSolve.setDisable(true);
                } else {
                    // Complete mode
                    List<Cell> path = currentSolver.solve(grid, mainApp.getStartCell(), mainApp.getGoalCell());
                    
                    if (path.isEmpty()) {
                        mainApp.showAlert("No path found", "The algorithm could not find a path between the start and goal.");
                    } else {
                        for (Cell c : path) {
                            if (c != mainApp.getStartCell() && c != mainApp.getGoalCell()) {
                                c.rect.setFill(Color.YELLOW);
                            }
                        }
                        mainApp.showAlert("Path found", "Path found with " + selectedAlgorithm + ", length: " + path.size());
                    }
                }
            } else {
                mainApp.showAlert("Missing selection", "Please select a start point (blue) and an end point (red).");
            }
        });
        
        // Button for the next step (step-by-step mode)
        Button btnNextStep = new Button("Next step");
        btnNextStep.setVisible(false);
        btnNextStep.setPrefWidth(180);
        btnNextStep.setOnAction(e -> mainApp.executeNextStep());
        mainApp.setBtnNextStep(btnNextStep);
        
        // Button to complete execution (step-by-step mode)
        Button btnCompleteExecution = new Button("Complete execution");
        btnCompleteExecution.setVisible(false);
        btnCompleteExecution.setPrefWidth(180);
        btnCompleteExecution.setOnAction(e -> mainApp.completeExecution());
        mainApp.setBtnCompleteExecution(btnCompleteExecution);
        
        // Reset button
        Button btnReset = new Button("Reset");
        btnReset.setPrefWidth(180);
        btnReset.setOnAction(e -> {
            // Reset variables
            mainApp.setStartCell(null);
            mainApp.setGoalCell(null);
            mainApp.setCurrentSolver(null);
            
            // Reset interface
            stepCountLabel.setVisible(false);
            btnNextStep.setVisible(false);
            btnNextStep.setDisable(false);
            btnCompleteExecution.setVisible(false);
            btnCompleteExecution.setDisable(false);
            btnSolve.setDisable(false);
            
            // Reset cell colors
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    grid[r][c].rect.setFill(Color.WHITE);
                }
            }
        });
        
        // Button to save the maze
        Button btnSave = new Button("Save");
        btnSave.setPrefWidth(180);
        btnSave.setOnAction(e -> mainApp.saveMaze());
        
        // Button to return to parameters
        Button btnParameters = new Button("Parameters");
        btnParameters.setPrefWidth(180);
        btnParameters.setOnAction(e -> {
            // Reset variables to allow a new configuration
            mainApp.setStartCell(null);
            mainApp.setGoalCell(null);
            mainApp.setCurrentSolver(null);
            
            // Return to the configuration screen
            mainApp.showConfigView();
        });
        
        // Button to access maze history
        Button btnHistory = new Button("History");
        btnHistory.setPrefWidth(180);
        btnHistory.setOnAction(e -> {
            // Go to the restore screen
            mainApp.showRestoreView();
        });

        VBox rightPanel = new VBox(10, algorithmLabel, btnSolve, btnReset, btnSave, btnParameters, btnHistory, stepCountLabel, btnNextStep, btnCompleteExecution);
        rightPanel.setPadding(new Insets(10));
        rightPanel.getStyleClass().add("right-panel");
        mainApp.setRightPanel(rightPanel);

        BorderPane gameLayout = new BorderPane();
        gameLayout.setCenter(mazePane);
        gameLayout.setRight(rightPanel);

        root.setCenter(gameLayout);
    }
    
    /**
     * Draws the maze walls on the pane
     */
    private void drawMazeWalls(Pane mazePane, Cell[][] grid, int rows, int cols) {
        double cellWidth = mazePane.getPrefWidth() / cols;
        double cellHeight = mazePane.getPrefHeight() / rows;
        double strokeWidth = 2;
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                
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
    }
    
    /**
     * Resets cell colors (except start and goal)
     */
    private void resetCellColors(Cell[][] grid, int rows, int cols) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                if (cell == mainApp.getStartCell()) {
                    cell.rect.setFill(Color.BLUE);
                } else if (cell == mainApp.getGoalCell()) {
                    cell.rect.setFill(Color.RED);
                } else {
                    cell.rect.setFill(Color.WHITE);
                }
            }
        }
    }
}
