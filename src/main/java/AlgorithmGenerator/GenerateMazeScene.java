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

    private static boolean isPerfect;
    private static boolean isFullMode;
    private static int width;
    private static int height;
    private static Maze maze;
    public static PrimMazeGenerator generator;
    private static Scene mainMenuScene;
    private static Stage mainStage;


    public GenerateMazeScene(int width, int height, Maze maze, PrimMazeGenerator generator) {
        this.width = width;
        this.height = height;
        this.maze = maze;
        this.generator = generator;

    }

    public GenerateMazeScene(Stage mainStage, Scene mainMenuScene,boolean isPerfect,boolean isFullMode) {
        this.mainMenuScene = mainMenuScene;
        this.mainStage = mainStage;
        this.isPerfect = isPerfect;
        this.isFullMode = isFullMode;
    }



    public Scene createMazeScene() {
        HBox root = new HBox(20);
        root.setStyle("-fx-background-color: white;");
        root.setPadding(new Insets(20));
        VBox leftSection = createLeftSection();
        VBox rightSection = createRightSection();

        HBox.setHgrow(leftSection, Priority.ALWAYS);
        HBox.setHgrow(rightSection, Priority.ALWAYS);
        root.getChildren().addAll(leftSection, rightSection);


        return new Scene(root, 800, 600);
    }

    private static Pane createMazeContainer(Pane parentContainer) {
        Pane mazePane = new Pane();
        mazePane.setStyle("-fx-background-color: white;");

        // Liaison des dimensions au parent
        mazePane.prefWidthProperty().bind(parentContainer.widthProperty());
        mazePane.prefHeightProperty().bind(parentContainer.heightProperty());

        // Listener pour redessiner le labyrinthe quand la taille change
        mazePane.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            if (maze != null) {  // Vérification que le labyrinthe existe
                redrawMaze(mazePane, newVal.getWidth(), newVal.getHeight());
            }
        });

        return mazePane;
    }

    private static void redrawMaze(Pane mazePane, double width, double height) {

        // Calcul des dimensions en fonction de la taille disponible
        double cellWidth = width / maze.getWidth();
        double cellHeight = height / maze.getHeight();
        double cellSize = Math.min(cellWidth, cellHeight);

        // Centrage du labyrinthe
        double offsetX = (width - maze.getWidth() * cellSize) / 2;
        double offsetY = (height - maze.getHeight() * cellSize) / 2;

        // Effacement et redessin
        mazePane.getChildren().clear();
        drawMaze(mazePane, maze.getWidth(), maze.getHeight(),offsetX,offsetY,cellSize);
    }


    private static VBox createLeftSection() {
        VBox leftBox = new VBox(15);
        leftBox.setPrefWidth(400);
        leftBox.setAlignment(Pos.TOP_CENTER);
        if(isPerfect){
            generator.generate();
        }else{
            generator.generate();
            generator.noPerfect();
        }

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
        mazeContainer.minWidthProperty().bind(leftBox.widthProperty().subtract(20));
        mazeContainer.minHeightProperty().bind(leftBox.heightProperty().subtract(50));

        leftBox.getChildren().addAll(title, mazeContainer);


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
                    MazeConfigScene retour = new MazeConfigScene(mainStage, mainMenuScene);
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

    private static void drawMaze(Pane mazePane, double width, double height,double offsetX, double
                                 offsetY,double cellSize) {

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = maze.getCell(x, y);
                double posX = offsetX + x * cellSize;
                double posY = offsetY + y * cellSize;

                // Fond de cellule (transparent par défaut)
                Rectangle rect = new Rectangle(posX, posY, cellSize, cellSize);
                rect.setFill(Color.TRANSPARENT);
                rect.setStroke(Color.TRANSPARENT);
                mazePane.getChildren().add(rect);

                // Interaction
                rect.setOnMouseClicked(e -> {
                    rect.setFill(rect.getFill().equals(Color.TRANSPARENT)
                            ? Color.LIGHTBLUE : Color.TRANSPARENT);
                });

                // Dessin des murs (avec coordonnées absolues)
                if (cell.hasNorthWall()) {
                    Line wall = new Line(
                            posX, posY,
                            posX + cellSize, posY
                    );
                    wall.setStroke(Color.BLACK);
                    wall.setStrokeWidth(2);
                    mazePane.getChildren().add(wall);
                }

                if (cell.hasEastWall()) {
                    Line wall = new Line(
                            posX + cellSize, posY,
                            posX + cellSize, posY + cellSize
                    );
                    wall.setStroke(Color.BLACK);
                    wall.setStrokeWidth(2);
                    mazePane.getChildren().add(wall);
                }

                // Murs sud et ouest seulement si c'est la dernière cellule
                if (y == height - 1 && cell.hasSouthWall()) {
                    Line wall = new Line(
                            posX, posY + cellSize,
                            posX + cellSize, posY + cellSize
                    );
                    wall.setStroke(Color.BLACK);
                    wall.setStrokeWidth(2);
                    mazePane.getChildren().add(wall);
                }

                if (x == width - 1 && cell.hasWestWall()) {
                    Line wall = new Line(
                            posX, posY,
                            posX, posY + cellSize
                    );
                    wall.setStroke(Color.BLACK);
                    wall.setStrokeWidth(2);
                    mazePane.getChildren().add(wall);
                }
            }
        }

        // Bordures extérieures
        Line westBorder = new Line(
                offsetX, offsetY,
                offsetX, offsetY + height * cellSize
        );
        westBorder.setStroke(Color.BLACK);
        westBorder.setStrokeWidth(2);
        mazePane.getChildren().add(westBorder);

        // Mur nord (tout le côté haut)
        Line northBorder = new Line(
                offsetX, offsetY,
                offsetX + width * cellSize, offsetY
        );
        northBorder.setStroke(Color.BLACK);
        northBorder.setStrokeWidth(2);
        mazePane.getChildren().add(northBorder);

        // Mur est (tout le côté droit)
        Line eastBorder = new Line(
                offsetX + width * cellSize, offsetY,
                offsetX + width * cellSize, offsetY + height * cellSize
        );
        eastBorder.setStroke(Color.BLACK);
        eastBorder.setStrokeWidth(2);
        mazePane.getChildren().add(eastBorder);

        // Mur sud (tout le côté bas)
        Line southBorder = new Line(
                offsetX, offsetY + height * cellSize,
                offsetX + width * cellSize, offsetY + height * cellSize
        );
        southBorder.setStroke(Color.BLACK);
        southBorder.setStrokeWidth(2);
        mazePane.getChildren().add(southBorder);    }


}