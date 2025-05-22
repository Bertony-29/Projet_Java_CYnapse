package src.main.java.AlgorithmGenerator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

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
    private final Spinner<Integer> vSpinner = new Spinner<>(5, 100, 15);
    private final Spinner<Integer> hSpinner = new Spinner<>(5, 100, 15);
    private Stage mainStage;
    private Scene mainMenuScene;
    private RadioButton fullModeRadio;
    private RadioButton perfectTypeRadio;

    public MazeConfigScene(Stage mainStage, Scene mainMenuScene) {
        this.mainStage = mainStage;
        this.mainMenuScene = mainMenuScene;
        setupSpinners();
    }

    private void setupSpinners() {
        setupSpinnerValidation(hSpinner);
        setupSpinnerValidation(vSpinner);
    }

    private void setupSpinnerValidation(Spinner<Integer> spinner) {
        spinner.setEditable(true);
        TextFormatter<Integer> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });
        spinner.getEditor().setTextFormatter(formatter);
    }

    public Scene createConfigScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f5f5f5;");

        Label title = new Label("Configuration du labyrinthe");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        VBox configSection = createConfigSection();
        VBox typeSection = createTypeSection();
        VBox modeSection = createModeSection();

        HBox buttonBox = createButtonBox();

        root.getChildren().addAll(title, configSection, typeSection, modeSection, buttonBox);
        return new Scene(root, 700, 550);
    }

    private VBox createConfigSection() {
        VBox configBox = new VBox(15);
        configBox.setPadding(new Insets(10));
        configBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        HBox hBox;
        hBox = new HBox(10.0,
                (Node) new Label("Nombre de cases horizontales :"),
                (Node) new Label("(Max 50)"));

        HBox vBox = new HBox(10.0,
                (Node) new Label("Nombre de cases verticales :"),
                (Node) new Label("(Max 35)"));

        configBox.getChildren().addAll(hBox, hSpinner, vBox, vSpinner);
        return configBox;
    }

    private VBox createTypeSection() {
        VBox typeBox = new VBox(10);
        typeBox.setPadding(new Insets(10));
        typeBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Label typeLabel = new Label("Type :");
        typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ToggleGroup typeGroup = new ToggleGroup();
        perfectTypeRadio = new RadioButton("Parfait");
        perfectTypeRadio.setToggleGroup(typeGroup);
        perfectTypeRadio.setSelected(true);

        RadioButton imperfectTypeRadio = new RadioButton("Non Parfait");
        imperfectTypeRadio.setToggleGroup(typeGroup);

        typeBox.getChildren().addAll(typeLabel, perfectTypeRadio, imperfectTypeRadio);
        return typeBox;
    }

    private VBox createModeSection() {
        VBox modeBox = new VBox(10);
        modeBox.setPadding(new Insets(10));
        modeBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

        Label modeLabel = new Label("Mode :");
        modeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ToggleGroup modeGroup = new ToggleGroup();
        fullModeRadio = new RadioButton("Mode Complet");
        fullModeRadio.setToggleGroup(modeGroup);
        fullModeRadio.setSelected(true);

        RadioButton stepModeRadio = new RadioButton("Mode Pas à Pas");
        stepModeRadio.setToggleGroup(modeGroup);

        modeBox.getChildren().addAll(modeLabel, fullModeRadio, stepModeRadio);
        return modeBox;
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button generateBtn = new Button("Générer");
        generateBtn.setStyle("-fx-font-size: 16px; -fx-min-width: 120px;");
        generateBtn.setOnAction(e -> {
            int width = hSpinner.getValue();
            int height = vSpinner.getValue();
            boolean isPerfect = perfectTypeRadio.isSelected();
            boolean isFullMode = fullModeRadio.isSelected();
            GenerateMazeScene g = new GenerateMazeScene(mainStage,mainMenuScene,isPerfect,isFullMode);
            SeedChoiceScene gridScene = new SeedChoiceScene(mainStage, mainMenuScene, width , height);
            mainStage.setScene(gridScene.createGridScene());
        });

        Button menuBtn = new Button("Retour au Menu");
        menuBtn.setStyle("-fx-font-size: 16px; -fx-min-width: 120px;");
        menuBtn.setOnAction(e -> mainStage.setScene(mainMenuScene));

        buttonBox.getChildren().addAll(generateBtn, menuBtn);
        return buttonBox;
    }

    // Getters pour les valeurs
    public int getHorizontalSize() {
        return hSpinner.getValue();
    }

    public int getVerticalSize() {
        return vSpinner.getValue();
    }

    public boolean isPerfectMaze() {
        return perfectTypeRadio.isSelected();
    }

    public boolean isFullMode() {
        return fullModeRadio.isSelected();
    }
}