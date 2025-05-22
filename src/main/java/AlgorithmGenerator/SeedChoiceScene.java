package src.main.java.AlgorithmGenerator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import src.main.java.maze.Cell;
import src.main.java.maze.Maze;
import src.main.java.mazegenerator.PrimMazeGenerator;

import java.util.Scanner;


public class SeedChoiceScene {

    private static Stage mainStage;
    private static Scene mainMenuScene;

    public SeedChoiceScene(Stage mainStage, Scene mainMenuScene, int width , int height) {
        this.mainStage = mainStage;
        this.mainMenuScene = mainMenuScene;
        this.width = width;
        this.height = height;
        this.maze = new Maze(height,width);
        this.generator = new PrimMazeGenerator(maze);
    }

    private static  int width;
    private static  int height;
    private static Maze maze;
    public static PrimMazeGenerator generator ;

    public Scene createGridScene(){
            HBox root = new HBox(20);
            root.setStyle("-fx-background-color: white;");
            root.setPadding(new Insets(20));
            VBox gridSection = createGridSection();
            HBox.setHgrow(gridSection, Priority.ALWAYS);
            root.getChildren().addAll(gridSection);



            return new Scene(root, 800, 600);

    }

    private static VBox createGridSection(){
        VBox leftBox = new VBox(15);
        leftBox.setPrefWidth(400);
        leftBox.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Veuillez choisir la graine de départ");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        Pane gridContainer = new Pane();
        gridContainer.setPadding(new Insets(18));
        gridContainer.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        Pane gridPane = new Pane();
        gridPane.setStyle("-fx-background-color: white;");
        gridPane.prefWidthProperty().bind(gridContainer.widthProperty());
        gridPane.prefHeightProperty().bind(gridContainer.heightProperty());

        gridPane.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            redrawMaze(gridPane, newVal.getWidth(), newVal.getHeight());
        });

        gridContainer.getChildren().add(gridPane);

        leftBox.getChildren().addAll(title, gridContainer);
        gridContainer.minWidthProperty().bind(leftBox.widthProperty().subtract(20));
        gridContainer.minHeightProperty().bind(leftBox.heightProperty().subtract(50));

        return leftBox;
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


    private static void drawGrid(Pane mazePane, int x, int y, double cellSize, double offsetX, double offsetY) {
        Cell cell = maze.getCell(x, y);
        double posX = offsetX + x * cellSize;
        double posY = offsetY + y * cellSize;

        Rectangle rect = new Rectangle(posX, posY, cellSize, cellSize);
        rect.setFill(Color.TRANSPARENT);
        rect.setStroke(Color.BLACK);
        mazePane.getChildren().add(rect);

        rect.setOnMouseClicked(e -> {
            if(!generator.getselectPoint()) {
                generator.setStartX(x);
                generator.setStartY(y);
                System.out.println("La cellule de début de génération est " + x + "," + y);

                Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.setTitle("Sélection de l'arrivée");

                VBox root = new VBox(40);
                root.setPadding(new Insets(10));

                Label instruction = new Label("Veuillez selectionner la case d'arrivée en bord de labyrinthe");
                Button okButton = new Button("D'accord");

                okButton.setOnAction(ev -> {
                    dialog.close();

                    generator.setStart(true);
                });
                root.getChildren().addAll(instruction, okButton);
                dialog.setScene(new Scene(root));
                dialog.showAndWait();}
            else {
                Cell endCell = new Cell(x, y);
                if (cell.isBorderCell(maze.getWidth(), maze.getHeight())) {
                    generator.setEndX(x);
                    generator.setEndY(y);
                    GenerateMazeScene mazeScene = new GenerateMazeScene(width, height, maze, generator, mainStage, mainMenuScene);
                    mainStage.setScene(mazeScene.createMazeScene());
                    System.out.println("La case d'arrivée est " + x + "," + y);
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Erreur de sélection");
                    error.setHeaderText(null);
                    error.setContentText("La sortie doit être sur un bord du labyrinthe !");
                    error.showAndWait();
                    return;
                }
                generator.setStart(false);
            }
        });
    }
}
