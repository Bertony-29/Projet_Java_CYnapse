package src.main.java.AlgorithmGenerator;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PrimaryScene extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Création du conteneur principal
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Titre principal
        Label title = new Label("Cynapse");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.DARKBLUE);
        BorderPane.setAlignment(title, Pos.CENTER);

        // Sous-titre
        Label subtitle = new Label("Générateur et résolveur de labyrinthes");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        subtitle.setTextFill(Color.GRAY);
        BorderPane.setAlignment(subtitle, Pos.CENTER);

        // Conteneur pour le titre et le sous-titre
        VBox titleBox = new VBox(10, title, subtitle);
        titleBox.setAlignment(Pos.CENTER);
        root.setTop(titleBox);

        // Boutons du menu
        Button generateButton = new Button("Générer un labyrinthe");
        generateButton.setStyle("-fx-font-size: 16px; -fx-min-width: 200px; -fx-min-height: 40px;");

        Button restoreButton = new Button("Restaurer");
        restoreButton.setStyle("-fx-font-size: 16px; -fx-min-width: 200px; -fx-min-height: 40px;");

        // Conteneur pour les boutons
        VBox buttonBox = new VBox(20, generateButton, restoreButton);
        buttonBox.setAlignment(Pos.CENTER);
        root.setCenter(buttonBox);

        // Création de la scène
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("Cynapse - Menu Principal");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Actions des boutons
        generateButton.setOnAction(e -> {
            System.out.println("Génération d'un labyrinthe...");
            Stage generateStage = new Stage();
            MazeConfigScene.display(primaryStage, scene);
        });

        restoreButton.setOnAction(e -> {
            System.out.println("Restauration d'un labyrinthe...");
            // Ici, vous pouvez ajouter la logique pour restaurer un labyrinthe sauvegardé
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}