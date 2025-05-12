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
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f0f0f0;");

        Label title = new Label("Cynapse");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.DARKBLUE);
        BorderPane.setAlignment(title, Pos.CENTER);

        Label subtitle = new Label("Générateur et résolveur de labyrinthes");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        subtitle.setTextFill(Color.GRAY);
        BorderPane.setAlignment(subtitle, Pos.CENTER);

        VBox titleBox = new VBox(10, title, subtitle);
        titleBox.setAlignment(Pos.CENTER);
        root.setTop(titleBox);

        Button generateButton = new Button("Générer un labyrinthe");
        generateButton.setStyle("-fx-font-size: 16px; -fx-min-width: 200px; -fx-min-height: 40px;");

        Button restoreButton = new Button("Restaurer");
        restoreButton.setStyle("-fx-font-size: 16px; -fx-min-width: 200px; -fx-min-height: 40px;");

        VBox buttonBox = new VBox(20, generateButton, restoreButton);
        buttonBox.setAlignment(Pos.CENTER);
        root.setCenter(buttonBox);

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("Cynapse - Menu Principal");
        primaryStage.setScene(scene);
        primaryStage.show();

        generateButton.setOnAction(e -> {
            System.out.println("Génération d'un labyrinthe...");
            Stage generateStage = new Stage();
            MazeConfigScene configMaze = new MazeConfigScene(primaryStage,scene);
            primaryStage.setScene(configMaze.createConfigScene());
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}