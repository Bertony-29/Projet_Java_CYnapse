package src.main.java.view;

import src.main.java.MainApp;
import src.main.java.model.SavedMaze;
import src.main.java.util.MazeStorage;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

/**
 * View for restoring saved mazes
 */
public class RestoreView {
    
    private BorderPane root;
    private ListView<SavedMaze> mazeListView;
    private Button btnLoad;
    private Button btnDelete;
    private Button btnBack;
    private MainApp mainApp;
    
    public RestoreView(BorderPane root, MainApp mainApp) {
        this.root = root;
        this.mainApp = mainApp;
        createView();
    }
    
    private void createView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.CENTER);
        
        // Title
        Label titleLabel = new Label("Historique");
        titleLabel.getStyleClass().add("title-label");
        titleLabel.setStyle("-fx-font-size: 32px;");
        
        // List of saved mazes
        mazeListView = new ListView<>();
        mazeListView.setPrefHeight(300);
        mazeListView.setPrefWidth(500);
        
        // Load the maze list
        refreshMazeList();
        
        // Action buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        btnLoad = new Button("Load");
        btnLoad.setPrefWidth(120);
        btnLoad.setDisable(true); // Disabled until a maze is selected
        
        btnDelete = new Button("Delete");
        btnDelete.setPrefWidth(120);
        btnDelete.setDisable(true); // Disabled until a maze is selected
        
        btnBack = new Button("Back");
        btnBack.setPrefWidth(120);
        
        buttonBox.getChildren().addAll(btnLoad, btnDelete, btnBack);
        
        // Add elements to the container
        container.getChildren().addAll(titleLabel, mazeListView, buttonBox);
        
        // Set the container as the main content
        root.setCenter(container);
        
        // Event handlers
        mazeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnLoad.setDisable(newVal == null);
            btnDelete.setDisable(newVal == null);
        });
        
        btnLoad.setOnAction(e -> {
            SavedMaze selectedMaze = mazeListView.getSelectionModel().getSelectedItem();
            if (selectedMaze != null) {
                mainApp.loadSavedMaze(selectedMaze);
            }
        });
        
        btnDelete.setOnAction(e -> {
            SavedMaze selectedMaze = mazeListView.getSelectionModel().getSelectedItem();
            if (selectedMaze != null) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirm deletion");
                confirmation.setHeaderText("Delete maze");
                confirmation.setContentText("Are you sure you want to delete the maze \"" + selectedMaze.getName() + "\"?");
                
                Optional<ButtonType> result = confirmation.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (MazeStorage.deleteMaze(selectedMaze.getFileName())) {
                        refreshMazeList();
                    } else {
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Error");
                        error.setHeaderText(null);
                        error.setContentText("Could not delete the maze.");
                        error.showAndWait();
                    }
                }
            }
        });
        
        btnBack.setOnAction(e -> mainApp.showHomeView());
    }
    
    /**
     * Refreshes the list of saved mazes
     */
    private void refreshMazeList() {
        List<SavedMaze> mazes = MazeStorage.getSavedMazes();
        mazeListView.setItems(FXCollections.observableArrayList(mazes));
    }
}
