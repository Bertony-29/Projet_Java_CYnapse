package src.main.java.view;

import src.main.java.MainApp;
import src.main.java.model.Cell;
import src.main.java.generator.MazeGenerator;
import src.main.java.generator.GeneratorFactory;
import src.main.java.solver.MazeSolver;
import src.main.java.solver.SolverFactory;

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
public class GameView {
    
    private BorderPane root;
    private MainApp mainApp;
    
    public GameView(BorderPane root, MainApp mainApp) {
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
        
        // Create the maze pane with fixed size and background
        Pane mazePane = new Pane();
        mazePane.setPrefSize(520, 520);
        mazePane.getStyleClass().add("maze-pane");
        mazePane.setStyle("-fx-background-color: white; -fx-border-color: #999999; -fx-border-width: 1px;");

        // Create grid of cells
        Cell[][] grid = new Cell[rows][cols];
        
        // Calculate cell dimensions
        double cellWidth = mazePane.getPrefWidth() / cols;
        double cellHeight = mazePane.getPrefHeight() / rows;
        double strokeWidth = 2.0;
        
        // Create cells with rectangles
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Create the cell rectangle
                Rectangle cellRect = new Rectangle(c * cellWidth, r * cellHeight, cellWidth, cellHeight);
                cellRect.setFill(Color.WHITE);
                cellRect.setStroke(Color.TRANSPARENT);
                mazePane.getChildren().add(cellRect);
                
                // Create the cell with its rectangle
                grid[r][c] = new Cell(r, c, cellRect);
            }
        }
        
        // Generate the maze
        MazeGenerator generator = GeneratorFactory.createGenerator(selectedGenerator);
        generator.generateMaze(grid, perfectMaze);
        
        // Store the grid in MainApp
        mainApp.setGrid(grid);
        
        // Draw the maze and add click handlers
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                Rectangle cellRect = cell.rect;
                
                // Add click handler to select start/goal cells
                final int row = r;
                final int col = c;
                cellRect.setOnMouseClicked(e -> {
                    if (mainApp.getStartCell() == null) {
                        mainApp.setStartCell(grid[row][col]);
                        cellRect.setFill(Color.GREEN);
                    } else if (mainApp.getGoalCell() == null && grid[row][col] != mainApp.getStartCell()) {
                        mainApp.setGoalCell(grid[row][col]);
                        cellRect.setFill(Color.RED);
                        
                        // Enable the solve button now that start and goal are set
                        Button solveButton = (Button) root.lookup("#btnSolve");
                        if (solveButton != null) {
                            solveButton.setDisable(false);
                        }
                    }
                });
            }
        }
        
        // Draw the walls
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
        
        // Create the right panel with controls
        VBox rightPanel = new VBox(15); // Reduced spacing
        rightPanel.setPadding(new Insets(15));
        rightPanel.setPrefWidth(230); // Reduced width
        rightPanel.getStyleClass().add("panel");
        // Pas de style spécifique, utilisation du style par défaut
        
        // Info section - simplified, removed detailed parameters
        Label infoLabel = new Label("Instructions");
        infoLabel.getStyleClass().add("section-title");
        
        // Instructions - removed duplicate title
        
        Label startLabel = new Label("1. Cliquez pour définir le point de départ (vert)");
        Label goalLabel = new Label("2. Cliquez pour définir le point d'arrivée (rouge)");
        Label solveLabel = new Label("3. Cliquez sur 'Résoudre' pour trouver le chemin");
        
        // Buttons
        Button btnSolve = new Button("Résoudre le Labyrinthe");
        btnSolve.setId("btnSolve");
        btnSolve.setPrefWidth(200);
        btnSolve.setDisable(true); // Disabled until start/goal are set
        
        btnSolve.setOnAction(e -> {
            if (mainApp.getStartCell() != null && mainApp.getGoalCell() != null) {
                MazeSolver solver = SolverFactory.getSolverByName(selectedAlgorithm);
                
                if (stepByStep) {
                    // Initialize step-by-step mode
                    solver.initializeStepByStep(grid, mainApp.getStartCell(), mainApp.getGoalCell());
                    mainApp.setCurrentSolver(solver);
                    
                    // Show step controls
                    mainApp.getBtnNextStep().setDisable(false);
                    mainApp.getBtnCompleteExecution().setDisable(false);
                    mainApp.getStepCountLabel().setText("Étapes: 0");
                } else {
                    // Complete mode - solve immediately
                    List<Cell> path = solver.solve(grid, mainApp.getStartCell(), mainApp.getGoalCell());
                    
                    // Highlight the path
                    for (Cell cell : path) {
                        if (cell != mainApp.getStartCell() && cell != mainApp.getGoalCell()) {
                            Rectangle cellRect = new Rectangle(
                                cell.getCol() * cellWidth, 
                                cell.getRow() * cellHeight, 
                                cellWidth, 
                                cellHeight
                            );
                            cellRect.setFill(Color.YELLOW);
                            mazePane.getChildren().add(cellRect);
                        }
                    }
                    
                    // Disable the solve button
                    btnSolve.setDisable(true);
                }
            }
        });
        
        Button btnReset = new Button("Réinitialiser");
        btnReset.setPrefWidth(200);
        btnReset.setOnAction(e -> {
            // Reset state variables before creating a new game view
            mainApp.setGrid(null);
            mainApp.setStartCell(null);
            mainApp.setGoalCell(null);
            mainApp.showGameView();
        });
        
        Button btnSettings = new Button("Retour aux Paramètres");
        btnSettings.setPrefWidth(200);
        btnSettings.setOnAction(e -> {
            // Reset grid, startCell and goalCell before going back to config
            mainApp.setGrid(null);
            mainApp.setStartCell(null);
            mainApp.setGoalCell(null);
            mainApp.showConfigView();
        });
        
        Button btnSave = new Button("Sauvegarder le Labyrinthe");
        btnSave.setPrefWidth(200);
        btnSave.setOnAction(e -> mainApp.saveMaze());
        btnSave.getStyleClass().add("primary-button");
        
        Button btnHistory = new Button("Historique");
        btnHistory.setPrefWidth(200);
        btnHistory.setOnAction(e -> mainApp.showRestoreView());
        
        // Step-by-step controls (initially hidden)
        HBox stepControls = new HBox(10);
        stepControls.setAlignment(javafx.geometry.Pos.CENTER);
        
        Button btnNextStep = new Button("Prochaine Étape");
        btnNextStep.setPrefWidth(95);
        btnNextStep.setDisable(true);
        btnNextStep.setOnAction(e -> mainApp.executeNextStep());
        
        Button btnCompleteExecution = new Button("Terminer");
        btnCompleteExecution.setPrefWidth(95);
        btnCompleteExecution.setDisable(true);
        btnCompleteExecution.setOnAction(e -> mainApp.completeExecution());
        
        Label stepCountLabel = new Label("Étapes: 0");
        
        stepControls.getChildren().addAll(btnNextStep, btnCompleteExecution);
        
        // Store references in MainApp for step-by-step mode
        mainApp.setBtnNextStep(btnNextStep);
        mainApp.setBtnCompleteExecution(btnCompleteExecution);
        mainApp.setStepCountLabel(stepCountLabel);
        mainApp.setRightPanel(rightPanel);
        
        // Add all elements to the right panel - removed parameter details
        rightPanel.getChildren().addAll(
            infoLabel, startLabel, goalLabel, solveLabel,
            btnSolve, stepControls, stepCountLabel, btnReset, btnSettings, btnSave, btnHistory
        );
        
        // Set up the main layout with spacing
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(mazePane);
        mainLayout.setRight(rightPanel);
        
        // Add spacing between maze and panel
        BorderPane.setMargin(mazePane, new Insets(10));
        BorderPane.setMargin(rightPanel, new Insets(10, 10, 10, 25)); // Increased left margin
        
        // Pas de couleur de fond spécifique, utilisation du style par défaut
        
        root.setCenter(mainLayout);
    }
}
