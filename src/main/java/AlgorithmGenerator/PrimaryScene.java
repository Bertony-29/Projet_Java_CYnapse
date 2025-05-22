package src.main.java.AlgorithmGenerator;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PrimaryScene extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Configuration principale avec un fond sobre
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(0));
        root.setBackground(new Background(new BackgroundFill(
                Color.web("#f8f9fa"), CornerRadii.EMPTY, Insets.EMPTY)));

        HBox topBar = new HBox(400);
        topBar.setStyle("-fx-background-color: #2c3e50;");
        topBar.setPrefHeight(60);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 20, 0, 20));

        Label Menu = new Label("Menu Principal");
        Menu.setTextFill(Color.WHITE);
        Menu.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");


        Label windowTitle = new Label(" Cynapse");  // Icône + texte
        windowTitle.setTextFill(Color.WHITE);
        windowTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        topBar.getChildren().addAll(windowTitle,Menu);
        Menu.setCursor(Cursor.HAND);
        Menu.setOnMouseClicked(event ->{
                primaryStage.setScene(new Scene(root,600,450));
        });

        // En-tête avec le titre
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label title = new Label("CYNAPSE");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#2c3e50"));

        Label subtitle = new Label("Générateur et résolveur de labyrinthes");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        subtitle.setTextFill(Color.web("#7f8c8d"));

        headerBox.getChildren().addAll(title, subtitle);
        root.setTop(headerBox);
        BorderPane.setMargin(headerBox, new Insets(0, 0, 30, 0));

        // Section centrale avec les boutons
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setMaxWidth(300);

        Label welcomeLabel = new Label("Bienvenue sur Cynapse");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        welcomeLabel.setTextFill(Color.web("#34495e"));

        Button generateButton = createMenuButton("Générer un labyrinthe");
        Button restoreButton = createMenuButton("Restaurer");


        centerBox.getChildren().addAll(welcomeLabel, generateButton, restoreButton);
        root.setTop(topBar);
        root.setCenter(centerBox);

        // Configuration de la scène
        Scene scene = new Scene(root, 600, 450);
        primaryStage.setTitle("Cynapse - Menu Principal");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Gestion des événements
        generateButton.setOnAction(e -> {
            MazeConfigScene configScene = new MazeConfigScene(primaryStage, scene);
            primaryStage.setScene(configScene.createConfigScene());
        });

        restoreButton.setOnAction(e -> {
            System.out.println("Restauration du labyrinthe...");
            // Implémentez la logique de restauration ici
        });
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setCursor(Cursor.HAND);
        String baseStyle = "-fx-font-size: 16px; " +
                "-fx-min-width: 250px; " +
                "-fx-min-height: 45px; " +
                "-fx-background-radius: 5; " +
                "-fx-text-fill: white; ";

        // Appliquer la couleur violette uniquement pour le bouton Restaurer
        if (text.equals("Restaurer")) {
            baseStyle += "-fx-background-color: #8e44ad;";  // Violet
            String finalBaseStyle = baseStyle;
            button.setOnMouseEntered(e -> button.setStyle(finalBaseStyle + "-fx-background-color: #9b59b6;"));
            button.setOnMouseExited(e -> button.setStyle(finalBaseStyle + "-fx-background-color: #8e44ad;"));
        } else {
            baseStyle += "-fx-background-color: #3498db;";  // Bleu (par défaut)
            String finalBaseStyle1 = baseStyle;
            button.setOnMouseEntered(e -> button.setStyle(finalBaseStyle1 + "-fx-background-color: #2980b9;"));
            button.setOnMouseExited(e -> button.setStyle(finalBaseStyle1 + "-fx-background-color: #3498db;"));
        }

        button.setStyle(baseStyle);
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}