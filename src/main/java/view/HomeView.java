package src.main.java.view;

import src.main.MainApp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * View for the home screen
 */
public class HomeView {
    
    private BorderPane root;
    private MainApp mainApp;
    
    public HomeView(BorderPane root, MainApp mainApp) {
        this.root = root;
        this.mainApp = mainApp;
        createView();
    }
    
    private void createView() {
        VBox homeView = new VBox(30);
        homeView.setPadding(new Insets(50));
        homeView.setAlignment(Pos.CENTER);
        
        // Container with visual effect
        VBox welcomeContainer = new VBox(30);
        welcomeContainer.getStyleClass().add("welcome-container");
        welcomeContainer.setPadding(new Insets(40));
        welcomeContainer.setAlignment(Pos.CENTER);
        welcomeContainer.setMaxWidth(600);

        // Main title
        Label titleLabel = new Label("YBAY Maze");
        titleLabel.getStyleClass().add("title-label");
        
        // Subtitle
        Label subtitleLabel = new Label("Générateur et Solveur de Labyrinthe");
        subtitleLabel.getStyleClass().add("subtitle-label");
        
        // Main buttons
        VBox buttonsBox = new VBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        
        Button btnGenerate = new Button("Générer un Labyrinthe");
        btnGenerate.setPrefWidth(250);
        btnGenerate.setPrefHeight(50);
        btnGenerate.getStyleClass().add("main-button");
        btnGenerate.setOnAction(e -> mainApp.showConfigView());
        
        Button btnRestore = new Button("Historique");
        btnRestore.setPrefWidth(250);
        btnRestore.setPrefHeight(50);
        btnRestore.getStyleClass().add("main-button");
        btnRestore.setOnAction(e -> mainApp.showRestoreView());
        
        buttonsBox.getChildren().addAll(btnGenerate, btnRestore);
        
        // Add elements to the container
        welcomeContainer.getChildren().addAll(titleLabel, subtitleLabel, buttonsBox);
        
        // Add the container to the home view
        homeView.getChildren().add(welcomeContainer);
        
        // Set the home view as the main content
        root.setCenter(homeView);
    }
}
