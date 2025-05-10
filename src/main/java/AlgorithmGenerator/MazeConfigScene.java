package src.main.java.AlgorithmGenerator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MazeConfigScene {

    private static Stage mainStage;
    private static Scene mainMenuScene;

    public static void display(Stage existingStage, Scene menuScene) {
        mainStage = existingStage;
        mainMenuScene = menuScene;

        // Conteneur principal
        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Titre
        Label title = new Label("Configuration du labyrinthe");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        // Section Mode
        VBox modeSection = createModeSection();

        // Section Configuration
        VBox configSection = createConfigSection();

        VBox typeSection = createTypeSection();

        // Boutons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button generateBtn = new Button("Générer");
        generateBtn.setStyle("-fx-font-size: 16px; -fx-min-width: 120px;");
        generateBtn.setOnAction(e ->{
                Stage generateMazeScene = new Stage();
                GenerateMazeScene.display(existingStage, menuScene);}

        );

        Button menuBtn = new Button("Retour au Menu");
        menuBtn.setStyle("-fx-font-size: 16px; -fx-min-width: 120px;");
        menuBtn.setOnAction(e -> mainStage.setScene(mainMenuScene));

        buttonBox.getChildren().addAll(generateBtn, menuBtn);

        // Assemblage
        root.getChildren().addAll(title, configSection, typeSection, modeSection, buttonBox);

        // Configuration de la scène
        Scene configScene = new Scene(root, 700, 550);
        mainStage.setScene(configScene);
    }

    private static VBox createModeSection() {
        VBox modeBox = new VBox(10);
        modeBox.setPadding(new Insets(10));
        modeBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Label modeLabel = new Label("Mode :");
        modeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ToggleGroup modeGroup = new ToggleGroup();

        RadioButton fullMode1 = new RadioButton("Mode Complet");
        fullMode1.setToggleGroup(modeGroup);
        fullMode1.setSelected(true);

        RadioButton stepMode = new RadioButton("Mode Pas à Pas");
        stepMode.setToggleGroup(modeGroup);

        modeBox.getChildren().addAll(modeLabel, fullMode1, stepMode);
        return modeBox;
    }
    private static VBox createTypeSection() {
        VBox modeBox = new VBox(10);
        modeBox.setPadding(new Insets(10));
        modeBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Label modeLabel = new Label("Type :");
        modeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ToggleGroup modeGroup = new ToggleGroup();

        RadioButton type1 = new RadioButton("Parfait");
        type1.setToggleGroup(modeGroup);
        type1.setSelected(true);

        RadioButton type2 = new RadioButton("Non Parfait");
        type2.setToggleGroup(modeGroup);

        modeBox.getChildren().addAll(modeLabel, type1, type2);
        return modeBox;
    }
    private static VBox createConfigSection() {
        VBox configBox = new VBox(15);
        configBox.setPadding(new Insets(10));
        configBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        // Configuration horizontale
        HBox hBox = new HBox(10);
        Label hLabel = new Label("Nombre de cases horizontales :");
        Label hMaxLabel = new Label("(Max 50)");
        hMaxLabel.setStyle("-fx-text-fill: #666;");
        hBox.getChildren().addAll(hLabel, hMaxLabel);

        Spinner<Integer> hSpinner = new Spinner<>(5, 50, 20);
        hSpinner.setEditable(true);

        // Configuration verticale
        HBox vBox = new HBox(10);
        Label vLabel = new Label("Nombre de cases verticales :");
        Label vMaxLabel = new Label("(Max 35)");
        vMaxLabel.setStyle("-fx-text-fill: #666;");
        vBox.getChildren().addAll(vLabel, vMaxLabel);

        Spinner<Integer> vSpinner = new Spinner<>(5, 35, 15);
        vSpinner.setEditable(true);

        configBox.getChildren().addAll(hBox, hSpinner, vBox, vSpinner);
        return configBox;
    }
    
}