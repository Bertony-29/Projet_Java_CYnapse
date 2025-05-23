package com.example.demo;

import com.example.demo.model.Cell;
import com.example.demo.model.SavedMaze;
import com.example.demo.solver.MazeSolver;
import com.example.demo.solver.SolverFactory;
import com.example.demo.util.MazeStorage;
import com.example.demo.view.HomeView;
import com.example.demo.view.ConfigView;
import com.example.demo.view.GameView;
import com.example.demo.view.RestoreView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;


/**
 * Main application class that serves as the controller in the MVC pattern
 */
public class MainApp extends Application {
    
    // Constant for the application title
    private static final String APP_TITLE = "YBAY Maze";

    private Stage primaryStage;
    private BorderPane root;
    private Cell[][] grid;
    private Cell startCell, goalCell;
    private int rows = 20;
    private int cols = 20;
    private String selectedAlgorithm = "Dijkstra"; // Default solving algorithm
    private String selectedGenerator = "Prim"; // Default generation algorithm
    private boolean perfectMaze = true; // Default perfect maze mode
    private boolean stepByStep = false; // Default complete mode
    
    // Variables for step-by-step mode
    private MazeSolver currentSolver;
    private Button btnNextStep;
    private Button btnCompleteExecution;
    private Label stepCountLabel;
    private VBox rightPanel;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(APP_TITLE);
        
        // Initialize the main scene
        root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);
        
        // Load CSS styles
        String cssPath = getClass().getResource("/styles/maze-styles.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        
        // Add the menu bar
        root.setTop(createMenuBar());
        
        // Display the home view
        showHomeView();
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates the application menu bar
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("☰");

        MenuItem homeItem = new MenuItem("Menu Principal");
        homeItem.setOnAction(e -> {
            // Clean the scene before returning to the main menu
            showHomeView();
        });
        
        MenuItem settingsItem = new MenuItem("Paramètres");
        settingsItem.setOnAction(e -> {
            // Go to the configuration screen
            showConfigView();
        });
        
        MenuItem saveItem = new MenuItem("Sauvegarder le Labyrinthe");
        saveItem.setOnAction(e -> {
            // Save the current maze
            saveMaze();
        });
        
        MenuItem restoreItem = new MenuItem("Historique des Labyrinthes");
        restoreItem.setOnAction(e -> {
            // Go to the restoration screen
            showRestoreView();
        });

        menu.getItems().addAll(homeItem, settingsItem, new SeparatorMenuItem(), saveItem, restoreItem);
        menuBar.getMenus().add(menu);
        return menuBar;
    }

    /**
     * Shows the home view
     */
    public void showHomeView() {
        // Reset state variables to allow a new game
        grid = null;
        startCell = null;
        goalCell = null;
        currentSolver = null;
        
        // Create and display the home view
        new HomeView(root, this);
    }
    
    /**
     * Shows the configuration view
     */
    public void showConfigView() {
        // Reset state variables to allow a new configuration
        grid = null;
        startCell = null;
        goalCell = null;
        
        // Create and display the configuration view
        new ConfigView(root, this);
    }
    
    /**
     * Shows the game view
     */
    public void showGameView() {
        // Reset state variables to allow selection of start and goal cells
        startCell = null;
        goalCell = null;
        currentSolver = null;
        
        // Create and display the game view
        new GameView(root, this);
    }
    
    /**
     * Shows the maze restoration view
     */
    public void showRestoreView() {
        // Reset state variables
        grid = null;
        startCell = null;
        goalCell = null;
        
        // Create and display the restoration view
        new RestoreView(root, this);
    }
    
    /**
     * Executes the next step in step-by-step mode
     */
    public void executeNextStep() {
        if (currentSolver == null) return;
        
        // Execute a step
        boolean finished = currentSolver.executeStep();
        
        // Update the display
        updateVisualization();
        
        // Update step counter
        int visitedCount = currentSolver.getVisitedCells().size();
        stepCountLabel.setText("Steps executed: " + visitedCount);
        
        // If the algorithm is finished
        if (finished) {
            List<Cell> finalPath = currentSolver.getCurrentPath();
            if (finalPath.isEmpty()) {
                showAlert("No path found", "The algorithm could not find a path between the start and goal.");
            } else {
                showAlert("Path found", "Path found with " + selectedAlgorithm + ", length: " + finalPath.size());
            }
            btnNextStep.setDisable(true);
            btnCompleteExecution.setDisable(true);
        }
    }
    
    /**
     * Completes execution in step-by-step mode
     */
    public void completeExecution() {
        if (currentSolver == null) return;
        
        // Execute all remaining steps
        boolean finished = false;
        while (!finished) {
            finished = currentSolver.executeStep();
        }
        
        // Update the display
        updateVisualization();
        
        // Update step counter
        int visitedCount = currentSolver.getVisitedCells().size();
        stepCountLabel.setText("Steps executed: " + visitedCount);
        
        // Display the result
        List<Cell> finalPath = currentSolver.getCurrentPath();
        if (finalPath.isEmpty()) {
            showAlert("No path found", "The algorithm could not find a path between the start and goal.");
        } else {
            showAlert("Path found", "Path found with " + selectedAlgorithm + ", length: " + finalPath.size());
        }
        
        btnNextStep.setDisable(true);
        btnCompleteExecution.setDisable(true);
    }
    
    /**
     * Updates the visualization of the maze solving process
     */
    private void updateVisualization() {
        // Reset colors (except start and goal)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                if (cell != startCell && cell != goalCell && cell.rect != null) {
                    cell.rect.setFill(Color.WHITE);
                }
            }
        }
        
        // Color visited cells
        for (Cell cell : currentSolver.getVisitedCells()) {
            if (cell != startCell && cell != goalCell && cell.rect != null) {
                cell.rect.setFill(Color.LIGHTBLUE);
            }
        }
        
        // Color the current path
        List<Cell> currentPath = currentSolver.getCurrentPath();
        for (Cell cell : currentPath) {
            if (cell != startCell && cell != goalCell && cell.rect != null) {
                cell.rect.setFill(Color.YELLOW);
            }
        }
    }
    
    /**
     * Saves the current maze
     */
    public void saveMaze() {
        if (grid == null) {
            showAlert("Error", "No maze to save");
            return;
        }
        
        // Ask for a name for the maze
        TextInputDialog dialog = new TextInputDialog("My maze");
        dialog.setTitle("Save maze");
        dialog.setHeaderText("Enter a name for this maze");
        dialog.setContentText("Name:");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String name = result.get().trim();
            
            // Create the SavedMaze object
            SavedMaze savedMaze = new SavedMaze(name, grid, perfectMaze, selectedGenerator, startCell, goalCell);
            
            // Save the maze
            if (MazeStorage.saveMaze(savedMaze)) {
                showAlert("Success", "The maze has been successfully saved.");
            } else {
                showAlert("Error", "Unable to save the maze.");
            }
        }
    }
    
    /**
     * Loads a saved maze
     */
    public void loadSavedMaze(SavedMaze savedMaze) {
        // Update parameters
        rows = savedMaze.getRows();
        cols = savedMaze.getCols();
        perfectMaze = savedMaze.isPerfectMaze();
        
        // Récupérer l'algorithme de génération s'il est défini
        if (savedMaze.getGeneratorAlgorithm() != null) {
            selectedGenerator = savedMaze.getGeneratorAlgorithm();
        }
        
        // Display the game view
        showGameView();
        
        // Apply the saved maze to the grid
        savedMaze.applyToGrid(grid);
        
        // Set start and goal cells if they exist
        if (savedMaze.hasStartCell()) {
            startCell = savedMaze.getStartCell(grid);
            startCell.rect.setFill(Color.BLUE);
        }
        
        if (savedMaze.hasGoalCell()) {
            goalCell = savedMaze.getGoalCell(grid);
            goalCell.rect.setFill(Color.RED);
        }
        
        // Redraw the maze walls
        redrawMazeWalls();
    }
    
    /**
     * Redraws the maze walls
     */
    private void redrawMazeWalls() {
        if (grid == null) return;
        
        // Get the maze pane
        BorderPane gameLayout = (BorderPane) root.getCenter();
        Pane mazePane = (Pane) gameLayout.getCenter();
        
        // Clear existing walls
        mazePane.getChildren().clear();
        
        // Add cells back
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                mazePane.getChildren().add(grid[r][c].rect);
            }
        }
        
        // Redraw walls
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
     * Displays an alert dialog
     */
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Getters and setters
    
    public Cell[][] getGrid() {
        return grid;
    }
    
    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }
    
    public Cell getStartCell() {
        return startCell;
    }
    
    public void setStartCell(Cell startCell) {
        this.startCell = startCell;
    }
    
    public Cell getGoalCell() {
        return goalCell;
    }
    
    public void setGoalCell(Cell goalCell) {
        this.goalCell = goalCell;
    }
    
    public int getRows() {
        return rows;
    }
    
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public void setCols(int cols) {
        this.cols = cols;
    }
    
    public String getSelectedAlgorithm() {
        return selectedAlgorithm;
    }
    
    public void setSelectedAlgorithm(String selectedAlgorithm) {
        this.selectedAlgorithm = selectedAlgorithm;
    }
    
    public String getSelectedGenerator() {
        return selectedGenerator;
    }
    
    public void setSelectedGenerator(String selectedGenerator) {
        this.selectedGenerator = selectedGenerator;
    }
    
    public boolean isPerfectMaze() {
        return perfectMaze;
    }
    
    public void setPerfectMaze(boolean perfectMaze) {
        this.perfectMaze = perfectMaze;
    }
    
    public boolean isStepByStep() {
        return stepByStep;
    }
    
    public void setStepByStep(boolean stepByStep) {
        this.stepByStep = stepByStep;
    }
    
    public MazeSolver getCurrentSolver() {
        return currentSolver;
    }
    
    public void setCurrentSolver(MazeSolver currentSolver) {
        this.currentSolver = currentSolver;
    }
    
    public Button getBtnNextStep() {
        return btnNextStep;
    }
    
    public void setBtnNextStep(Button btnNextStep) {
        this.btnNextStep = btnNextStep;
    }
    
    public Button getBtnCompleteExecution() {
        return btnCompleteExecution;
    }
    
    public void setBtnCompleteExecution(Button btnCompleteExecution) {
        this.btnCompleteExecution = btnCompleteExecution;
    }
    
    public Label getStepCountLabel() {
        return stepCountLabel;
    }
    
    public void setStepCountLabel(Label stepCountLabel) {
        this.stepCountLabel = stepCountLabel;
    }
    
    public VBox getRightPanel() {
        return rightPanel;
    }
    
    public void setRightPanel(VBox rightPanel) {
        this.rightPanel = rightPanel;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
