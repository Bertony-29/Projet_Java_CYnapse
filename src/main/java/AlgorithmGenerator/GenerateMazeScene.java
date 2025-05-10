package src.main.java.AlgorithmGenerator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GenerateMazeScene {

    private static Stage mainStage;
    private static Scene mainMenuScene;

    // Méthode principale pour afficher la scène
    public static void display(Stage existingStage, Scene menuScene) {
        mainStage = existingStage;
        mainMenuScene = menuScene;

        // Conteneur principal (split horizontal)
        HBox root = new HBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // --- Partie gauche : Génération du labyrinthe ---
        VBox leftSection = createLeftSection();

        // --- Partie droite : Statistiques et options ---
        VBox rightSection = createRightSection();

        // Assemblage
        root.getChildren().addAll(leftSection, rightSection);

        // Configuration de la scène
        Scene generateScene = new Scene(root, 800, 500);
        mainStage.setScene(generateScene);
    }

    private static VBox createLeftSection() {
        VBox leftBox = new VBox(15);
        leftBox.setPrefWidth(400);
        leftBox.setAlignment(Pos.TOP_CENTER);

        // Titre
        Label title = new Label("Voici le labyrinthe générer");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        // Bouton Générer
        Button generateBtn = new Button("Générer");
        generateBtn.setStyle("-fx-font-size: 16px; -fx-min-width: 150px; -fx-min-height: 40px;");

        // Zone de visualisation du labyrinthe
        StackPane mazePreview = new StackPane();
        mazePreview.setStyle("-fx-border-color: #333; -fx-border-width: 2px;");
        mazePreview.setMinSize(350, 350);

        // Exemple de labyrinthe minimal
        Rectangle mazeExample = new Rectangle(300, 300, Color.LIGHTGRAY);
        mazePreview.getChildren().add(mazeExample);

        leftBox.getChildren().addAll(title, generateBtn, mazePreview);
        return leftBox;
    }

    private static VBox createRightSection() {
        VBox rightBox = new VBox(20);
        rightBox.setPrefWidth(350);

        // Section Sauvegarder/Résoudre
        VBox saveSection = new VBox(10);
        Label saveTitle = new Label("Sauvegarder");
        saveTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Button saveBtn = new Button("Sauvegarder");
        Button solveBtn = new Button("Résoudre");

        ComboBox<String> algorithmCombo = new ComboBox<>();
        algorithmCombo.getItems().addAll(
                "Algorithme A*",
                "Algorithme de Dijkstra",
                "BFS"
        );
        algorithmCombo.setValue("Algorithme A*");

        saveSection.getChildren().addAll(
                saveTitle,
                saveBtn,
                solveBtn,
                new Label("Choix de l'algorithme :"),
                algorithmCombo
        );

        // Section Statistiques
        VBox statsSection = new VBox(10);
        Label statsTitle = new Label("Statistiques");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label pathCells = new Label("Nombre de cases du chemin : 0");
        Label processedCells = new Label("Nombre de cases traitées : 0");
        Label execTime = new Label("Temps d'exécution : 0 ms");

        statsSection.getChildren().addAll(
                statsTitle,
                pathCells,
                processedCells,
                execTime
        );

        // Bouton Retour
        Button backBtn = new Button("Retour a la configuration");
        backBtn.setStyle("-fx-font-size: 14px;");
        backBtn.setOnAction(e -> {
                Stage mazeScene = new Stage();
                MazeConfigScene.display(mainStage,mainMenuScene);

        }
        );

        rightBox.getChildren().addAll(
                saveSection,
                new Separator(),
                statsSection,
                new Separator(),
                backBtn
        );

        return rightBox;
    }
}