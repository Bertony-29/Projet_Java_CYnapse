package src.main.java.AlgorithmGenerator;

import javafx.event.Event;
import javafx.scene.shape.Line;
import src.main.Cell;
import src.main.Maze;
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
import src.main.PrimMazeGenerator;


public class GenerateMazeScene {

    private static  int width;
    private static  int height;
    private static Maze maze;
    public static PrimMazeGenerator generator;
    private static Scene mainMenuScene;

    public GenerateMazeScene(int width,int height, Maze maze, PrimMazeGenerator generator){
        this.width= width;
        this.height = height;
        this.maze = maze;
        this.generator = generator;

    }

    public GenerateMazeScene(Stage mainStage,Scene mainMenuScene){
        this.mainMenuScene = mainMenuScene;
        this.mainStage = mainStage;
    }


    private static Stage mainStage;

    public Scene createMazeScene() {
        HBox root = new HBox(20);
        root.setStyle("-fx-background-color: white;");
        root.setPadding(new Insets(20));
        VBox leftSection = createLeftSection();
        VBox rightSection = createRightSection();

        HBox.setHgrow(leftSection, Priority.ALWAYS);
        HBox.setHgrow(rightSection,Priority.ALWAYS);
        root.getChildren().addAll(leftSection, rightSection);



        return new Scene(root, 800, 600);
    }
    private static Pane createMazeContainer(Pane parentContainer) {
        Pane mazePane = new Pane();
        mazePane.setStyle("-fx-background-color: white;");

        mazePane.prefWidthProperty().bind(parentContainer.widthProperty());
        mazePane.prefHeightProperty().bind(parentContainer.heightProperty());

        mazePane.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            redrawMaze(mazePane, newVal.getWidth(), newVal.getHeight());
        });

        return mazePane;
    }

    private static void  redrawMaze(Pane mazePane, double width, double height) {
        mazePane.getChildren().clear();

        double cellWidth = width / maze.getWidth();
        double cellHeight = height / maze.getHeight();
        double cellSize = Math.min(cellWidth, cellHeight);

        double offsetX = (width - (maze.getWidth() * cellSize)) / 2;
        double offsetY = (height - (maze.getHeight() * cellSize)) / 2;

        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                drawGrid(mazePane, x, y, cellSize, offsetX, offsetY);
            }
        }
    }

    private static VBox createLeftSection() {
        VBox leftBox = new VBox(15);
        leftBox.setPrefWidth(400);
        leftBox.setAlignment(Pos.TOP_CENTER);
        generator.generate();

        // Titre
        Label title = new Label("Voici le labyrinthe générer");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        // Zone de visualisation du labyrinthe
        Pane mazeContainer = new Pane();
        mazeContainer.setPadding(new Insets(18));
        mazeContainer.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        Pane mazePane = createMazeContainer(mazeContainer);
        mazeContainer.getChildren().add(mazePane);

        leftBox.getChildren().addAll(title, mazeContainer);
        mazeContainer.minWidthProperty().bind(leftBox.widthProperty().subtract(20));
        mazeContainer.minHeightProperty().bind(leftBox.heightProperty().subtract(50));

        return leftBox;
    }

    private static VBox createRightSection() {
        VBox rightBox = new VBox(20);
        rightBox.setPrefWidth(350);

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

        Button backBtn = new Button("Retour a la configuration");
        backBtn.setStyle("-fx-font-size: 14px;");
        backBtn.setOnAction(e -> {
                    MazeConfigScene retour = new MazeConfigScene(mainStage,mainMenuScene);
                    mainStage.setScene(retour.createConfigScene());
                    
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
    private static void drawGrid(Pane mazePane, int x, int y, double cellSize, double offsetX, double offsetY) {
        Cell cell = maze.getCell(x, y);
        double posX = offsetX + x * cellSize;
        double posY = offsetY + y * cellSize;

        // Rectangle de fond (pour interactions)
        Rectangle rect = new Rectangle(posX, posY, cellSize, cellSize);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.BLACK);
        //mazePane.getChildren().add(rect);

        rect.setOnMouseClicked(e -> {
            if(rect.getFill().equals(Color.TRANSPARENT)){
                rect.setFill(Color.LIGHTBLUE);}

        });

        if (x == 0) {
            addWall(mazePane, x, y, x, y + 1, Color.BLACK, 2, cellSize);}

                    if (cell.hasNorthWall()) {
                        Line northWall = new Line(
                                x * cellSize,
                                y * cellSize,
                                (x + 1) * cellSize,
                                y * cellSize
                        );
                        northWall.setFill(Color.BLACK);
                        northWall.setStrokeWidth(2);
                        mazePane.getChildren().add(northWall);
                    }

                    if (cell.hasEastWall()) {
                        Line eastWall = new Line(
                                (x + 1) * cellSize,
                                y * cellSize,
                                (x + 1) * cellSize,
                                (y + 1) * cellSize
                        );
                        eastWall.setFill(Color.BLACK);
                        eastWall.setStrokeWidth(2);
                        mazePane.getChildren().add(eastWall);
                    }

                    if (y == height - 1 && cell.hasSouthWall()) {
                        Line southWall = new Line(
                                x * cellSize,
                                (y + 1) * cellSize,
                                (x + 1) * cellSize,
                                (y + 1) * cellSize
                        );
                        southWall.setFill(Color.BLACK);
                        southWall.setStrokeWidth(2);
                        mazePane.getChildren().add(southWall);
                    }

                    if (x == width - 1 && cell.hasWestWall()) {
                        Line westWall = new Line(
                                x * cellSize,
                                y * cellSize,
                                x * cellSize,
                                (y + 1) * cellSize
                        );
                        westWall.setFill(Color.BLACK);
                        westWall.setStrokeWidth(2);
                        mazePane.getChildren().add(westWall);
                    }
    }
        private static void addWall(Pane mazePane, double x1, double y1, double x2, double y2,
                                    Color color, double thickness, double cellSize) {
            Line wall = new Line(
                    x1 * cellSize,
                    y1 * cellSize,
                    x2 * cellSize,
                    y2 * cellSize
            );
            wall.setStroke(color);
            wall.setStrokeWidth(thickness);
            mazePane.getChildren().add(wall);
        }}