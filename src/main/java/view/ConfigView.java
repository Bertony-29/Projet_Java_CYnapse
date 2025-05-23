package src.main.java.view;

import com.example.demo.MainApp;
import com.example.demo.generator.GeneratorFactory;
import com.example.demo.solver.SolverFactory;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * View for maze configuration
 */
public class ConfigView {
    
    private BorderPane root;
    private MainApp mainApp;
    
    public ConfigView(BorderPane root, MainApp mainApp) {
        this.root = root;
        this.mainApp = mainApp;
        createView();
    }
    
    private void createView() {
        VBox configView = new VBox(20);
        configView.setPadding(new Insets(30));
        configView.setAlignment(Pos.CENTER);
        
        // Container with visual effect
        VBox configContainer = new VBox(20);
        configContainer.getStyleClass().add("panel");
        configContainer.setPadding(new Insets(30));
        configContainer.setAlignment(Pos.CENTER);
        configContainer.setMaxWidth(700);
        
        // Title
        Label titleLabel = new Label("Configuration du Labyrinthe");
        titleLabel.getStyleClass().add("title-label");
        titleLabel.setStyle("-fx-font-size: 32px;");
        
        // Configuration grid
        GridPane configGrid = new GridPane();
        configGrid.setHgap(20);
        configGrid.setVgap(15);
        configGrid.setAlignment(Pos.CENTER);
        
        // Maze size
        Label sizeLabel = new Label("Taille du labyrinthe:");
        sizeLabel.setStyle("-fx-font-size: 16px;");
        
        HBox sizeBox = new HBox(10);
        Label rowsLabel = new Label("Lignes:");
        Spinner<Integer> rowsSpinner = new Spinner<>(5, 50, mainApp.getRows());
        rowsSpinner.setEditable(true);
        rowsSpinner.valueProperty().addListener((obs, oldVal, newVal) -> mainApp.setRows(newVal));
        
        Label colsLabel = new Label("Colonnes:");
        Spinner<Integer> colsSpinner = new Spinner<>(5, 50, mainApp.getCols());
        colsSpinner.setEditable(true);
        colsSpinner.valueProperty().addListener((obs, oldVal, newVal) -> mainApp.setCols(newVal));
        
        sizeBox.getChildren().addAll(rowsLabel, rowsSpinner, colsLabel, colsSpinner);
        
        // Maze type
        Label typeLabel = new Label("Type de labyrinthe:");
        typeLabel.setStyle("-fx-font-size: 16px;");
        
        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton perfectRB = new RadioButton("Parfait");
        perfectRB.setToggleGroup(typeGroup);
        perfectRB.setSelected(mainApp.isPerfectMaze());
        perfectRB.setUserData(true);
        
        RadioButton imperfectRB = new RadioButton("Imparfait");
        imperfectRB.setToggleGroup(typeGroup);
        imperfectRB.setSelected(!mainApp.isPerfectMaze());
        imperfectRB.setUserData(false);
        
        typeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mainApp.setPerfectMaze((boolean) newVal.getUserData());
            }
        });
        
        HBox typeBox = new HBox(20, perfectRB, imperfectRB);
        
        // Generation algorithm
        Label genLabel = new Label("Algorithme de génération:");
        genLabel.setStyle("-fx-font-size: 16px;");
        
        ToggleGroup genGroup = new ToggleGroup();
        VBox genBox = new VBox(8);
        
        for (String genName : GeneratorFactory.getGeneratorNames()) {
            RadioButton rb = new RadioButton(genName);
            rb.setToggleGroup(genGroup);
            rb.setUserData(genName);
            
            if (genName.equals(mainApp.getSelectedGenerator())) {
                rb.setSelected(true);
            }
            
            genBox.getChildren().add(rb);
        }
        
        genGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mainApp.setSelectedGenerator(newVal.getUserData().toString());
            }
        });
        
        // Solving algorithm
        Label algoLabel = new Label("Algorithme de résolution:");
        algoLabel.setStyle("-fx-font-size: 16px;");
        
        ToggleGroup algoGroup = new ToggleGroup();
        VBox algoBox = new VBox(8);
        
        for (String algoName : SolverFactory.getAvailableSolverNames()) {
            RadioButton rb = new RadioButton(algoName);
            rb.setToggleGroup(algoGroup);
            rb.setUserData(algoName);
            
            if (algoName.equals(mainApp.getSelectedAlgorithm())) {
                rb.setSelected(true);
            }
            
            algoBox.getChildren().add(rb);
        }
        
        algoGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mainApp.setSelectedAlgorithm(newVal.getUserData().toString());
            }
        });
        
        // Solving mode
        Label modeLabel = new Label("Mode de résolution:");
        modeLabel.setStyle("-fx-font-size: 16px;");
        
        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton completeRB = new RadioButton("Complet");
        completeRB.setToggleGroup(modeGroup);
        completeRB.setSelected(!mainApp.isStepByStep());
        completeRB.setUserData(false);
        
        RadioButton stepByStepRB = new RadioButton("Pas à pas");
        stepByStepRB.setToggleGroup(modeGroup);
        stepByStepRB.setSelected(mainApp.isStepByStep());
        stepByStepRB.setUserData(true);
        
        modeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mainApp.setStepByStep((boolean) newVal.getUserData());
            }
        });
        
        HBox modeBox = new HBox(20, completeRB, stepByStepRB);
        
        // Add elements to the grid
        configGrid.add(sizeLabel, 0, 0);
        configGrid.add(sizeBox, 1, 0);
        configGrid.add(typeLabel, 0, 1);
        configGrid.add(typeBox, 1, 1);
        configGrid.add(genLabel, 0, 2);
        configGrid.add(genBox, 1, 2);
        configGrid.add(algoLabel, 0, 3);
        configGrid.add(algoBox, 1, 3);
        configGrid.add(modeLabel, 0, 4);
        configGrid.add(modeBox, 1, 4);
        
        // Buttons
        HBox buttonsBox = new HBox(20);
        buttonsBox.setAlignment(Pos.CENTER);
        
        Button btnBack = new Button("Retour");
        btnBack.setPrefWidth(120);
        btnBack.setOnAction(e -> mainApp.showHomeView());
        
        Button btnCreate = new Button("Créer");
        btnCreate.setPrefWidth(120);
        btnCreate.setStyle("-fx-font-weight: bold;");
        btnCreate.setOnAction(e -> mainApp.showGameView());
        
        buttonsBox.getChildren().addAll(btnBack, btnCreate);
        
        // Add all elements to the container
        configContainer.getChildren().addAll(titleLabel, configGrid, buttonsBox);
        
        // Add the container to the configuration view
        configView.getChildren().add(configContainer);
        
        // Set the configuration view as the main content
        root.setCenter(configView);
    }
}
