package src.main.java.AlgorithmGenerator;

import javafx.event.Event;
import javafx.scene.shape.Line;
import src.main.java.maze.*;
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
import src.main.java.maze.Cell;
import src.main.java.mazegenerator.PrimMazeGenerator;

import java.util.List;

public class GenerateMazeScene {
    private boolean isPerfect;
    private boolean isFullMode;
    private int width;
    private int height;
    private Maze maze;
    public PrimMazeGenerator generator;
    private Scene mainMenuScene;
    private Stage mainStage;
    private Pane mazePane;
    private double cellSize;
    private double offsetX;
    private double offsetY;


    public GenerateMazeScene(int width, int height, Maze maze, PrimMazeGenerator generator,Stage mainStage, Scene mainMenuScene) {
        this.width = width;
        this.height = height;
        this.maze = maze;
        this.generator = generator;
        this.mainMenuScene = mainMenuScene;
        this.mainStage = mainStage;
    }

    public GenerateMazeScene(Stage mainStage, Scene mainMenuScene, boolean isPerfect, boolean isFullMode) {
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

    private Pane createMazeContainer(Pane parentContainer) {
        mazePane = new Pane();
        mazePane.setStyle("-fx-background-color: white;");

        mazePane.prefWidthProperty().bind(parentContainer.widthProperty());
        mazePane.prefHeightProperty().bind(parentContainer.heightProperty());
        mazePane.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            if (maze != null) {
                redrawMaze(newVal.getWidth(), newVal.getHeight());
            }
        });

        return mazePane;
    }

    private void redrawMaze(double width, double height) {
        cellSize = Math.min(width / maze.getWidth(), height / maze.getHeight());
        offsetX = (width - maze.getWidth() * cellSize) / 2;
        offsetY = (height - maze.getHeight() * cellSize) / 2;

        mazePane.getChildren().clear();
        drawMaze(maze.getWidth(), maze.getHeight());

    }

    private VBox createLeftSection() {
        VBox leftBox = new VBox(15);
        leftBox.setPrefWidth(400);
        leftBox.setAlignment(Pos.TOP_CENTER);

        if (isPerfect) {
            generator.generate();
        } else {
            generator.generate();
            //generator.noPerfect();
        }
        System.out.println(generator.getStartX()+","+ generator.getStartY());
        System.out.println(generator.getEndX()+","+generator.getEndY());
        breakExitWall(generator.getEndX(), generator.getEndY());


        Label title = new Label("Voici le labyrinthe généré");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        Pane mazeContainer = new Pane();
        mazeContainer.setPadding(new Insets(18));
        mazeContainer.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        mazePane = createMazeContainer(mazeContainer);
        mazeContainer.getChildren().add(mazePane);
        mazeContainer.minWidthProperty().bind(leftBox.widthProperty().subtract(20));
        mazeContainer.minHeightProperty().bind(leftBox.heightProperty().subtract(50));

        leftBox.getChildren().addAll(title, mazeContainer);

        return leftBox;
    }

    private VBox createRightSection() {
        VBox rightBox = new VBox(20);
        rightBox.setPrefWidth(350);

        VBox saveSection = new VBox(10);
        Label saveTitle = new Label("Sauvegarder");
        saveTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Button saveBtn = new Button("Sauvegarder");
        Button solveBtn = new Button("Résoudre");

        ComboBox<String> algorithmCombo = new ComboBox<>();
        algorithmCombo.getItems().addAll(
                "A*",
                "Dijkstra",
                "BFS",
                "DFS"
        );
        algorithmCombo.setValue("A*");

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

        Button backBtn = new Button("Retour à la configuration");
        backBtn.setStyle("-fx-font-size: 14px;");
        backBtn.setOnAction(e -> {
            MazeConfigScene retour = new MazeConfigScene(mainStage, mainMenuScene);
            mainStage.setScene(retour.createConfigScene());
        });

        solveBtn.setOnAction(e -> {
            switch (algorithmCombo.getValue()){
                case "Algorithme A*" :
                    AStarSolver solver = new AStarSolver(maze, mazePane, cellSize, offsetX, offsetY);
                    long startTime = System.nanoTime();
                    List<AStarSolver.Node> path = solver.solve(generator.getStartX(), generator.getStartY(), generator.getEndX(), generator.getEndY());
                    long duration = (System.nanoTime() - startTime) / 1_000_000;


                    if (path.isEmpty()) {
                        showAlert("Aucun chemin trouvé",
                                "Il n'existe pas de chemin");
                    } else {
                        solver.drawPath(path);
                        pathCells.setText("Nombre de cases du chemin : " + path.size());
                        processedCells.setText("Cases explorées : " + (int) (path.size() * 1.5)); // Approximation
                        execTime.setText("Temps d'exécution : " + duration + " ms");
                    }
                case "Djikstra" :
                case "BFS" :





            }


        });

        rightBox.getChildren().addAll(
                saveSection,
                new Separator(),
                statsSection,
                new Separator(),
                backBtn
        );

        return rightBox;
    }
    private void breakExitWall(int exitX, int exitY) {
        Cell exitCell = maze.getCell(exitX, exitY);

        // Déterminer quel bord est le plus proche (pour les coins)
        boolean isWestEdge = exitX == 0;
        boolean isEastEdge = exitX == maze.getWidth() - 1;
        boolean isNorthEdge = exitY == 0;
        boolean isSouthEdge = exitY == maze.getHeight() - 1;

        // Cas spécial pour les coins
        if (isWestEdge && isNorthEdge) { // Coin NW
            exitCell.removeNorthWall(); // On choisit de casser le mur nord
        }
        else if (isWestEdge && isSouthEdge) { // Coin SW
            exitCell.removeSouthWall();
        }
        else if (isEastEdge && isNorthEdge) { // Coin NE
            exitCell.removeEastWall();
        }
        else if (isEastEdge && isSouthEdge) { // Coin SE
            exitCell.removeEastWall();
        }
        // Cas des bords standards
        else if (isWestEdge) {
            exitCell.removeWestWall();
        }
        else if (isEastEdge) {
            exitCell.removeEastWall();
        }
        else if (isNorthEdge) {
            exitCell.removeNorthWall();
        }
        else if (isSouthEdge) {
            exitCell.removeSouthWall();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void drawMaze(int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = maze.getCell(x, y);
                double posX = offsetX + x * cellSize;
                double posY = offsetY + y * cellSize;

                Rectangle rect = new Rectangle(posX, posY, cellSize, cellSize);
                rect.setFill(Color.TRANSPARENT);
                rect.setStroke(Color.TRANSPARENT);
                mazePane.getChildren().add(rect);

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

                if (cell.hasSouthWall()) {
                    Line wall = new Line(
                            posX, posY + cellSize,
                            posX + cellSize, posY + cellSize
                    );
                    wall.setStroke(Color.BLACK);
                    wall.setStrokeWidth(2);
                    mazePane.getChildren().add(wall);
                }

                if (cell.hasWestWall()) {
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

        /*Line westBorder = new Line(
                offsetX, offsetY,
                offsetX, offsetY + height * cellSize
        );
        westBorder.setStroke(Color.BLACK);
        westBorder.setStrokeWidth(2);
        mazePane.getChildren().add(westBorder);*/

        /*Line northBorder = new Line(
                offsetX, offsetY,
                offsetX + width * cellSize, offsetY
        );
        northBorder.setStroke(Color.BLACK);
        northBorder.setStrokeWidth(2);
        mazePane.getChildren().add(northBorder);

        Line eastBorder = new Line(
                offsetX + width * cellSize, offsetY,
                offsetX + width * cellSize, offsetY + height * cellSize
        );
        eastBorder.setStroke(Color.BLACK);
        eastBorder.setStrokeWidth(2);
        mazePane.getChildren().add(eastBorder);

        Line southBorder = new Line(
                offsetX, offsetY + height * cellSize,
                offsetX + width * cellSize, offsetY + height * cellSize
        );
        southBorder.setStroke(Color.BLACK);
        southBorder.setStrokeWidth(2);
        mazePane.getChildren().add(southBorder);*/
    }
}