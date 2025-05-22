package src.main.java.Interface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AccueilView {

    public static VBox getView(Runnable onGenerateClicked) {
        VBox centerContent = new VBox(20);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(20));

        Label welcome = new Label("Bienvenue sur YBAY Maze");
        welcome.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label subtitle = new Label("Générateur et résolveur de labyrinthes");
        subtitle.setStyle("-fx-text-fill: grey;");

        Button generateBtn = new Button("Générer un labyrinthe");
        generateBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");
        generateBtn.setPrefWidth(200);
        generateBtn.setOnAction(e -> onGenerateClicked.run());

        Button restoreBtn = new Button("Restaurer");
        restoreBtn.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-size: 14px;");
        restoreBtn.setPrefWidth(200);

        centerContent.getChildren().addAll(welcome, subtitle, generateBtn, restoreBtn);

        return centerContent;
    }
}
