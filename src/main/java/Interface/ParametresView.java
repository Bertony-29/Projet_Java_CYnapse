package src.main ;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ParametresView {
    private static TextField hField = new TextField("20");
    private static TextField vField = new TextField("20");
    private static String algorithmeChoisi = "Dijkstra"; // valeur par défaut

    public static VBox getView(Runnable onValider) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        layout.getChildren().addAll(
                new Label("Nombre de colonnes :"), hField,
                new Label("Nombre de lignes :"), vField
        );

        Button algoButton = new Button("Choisir l'algorithme");
        algoButton.setOnAction(e -> afficherChoixAlgorithme());

        Button validerButton = new Button("Valider");
        validerButton.setOnAction(e -> onValider.run());

        layout.getChildren().addAll(algoButton, validerButton);
        return layout;
    }

    private static void afficherChoixAlgorithme() {
        List<String> choix = Arrays.asList("Dijkstra", "A*", "BFS");
        ChoiceDialog<String> dialog = new ChoiceDialog<>(algorithmeChoisi, choix);
        dialog.setTitle("Choix de l'algorithme");
        dialog.setHeaderText("Veuillez choisir un algorithme");
        dialog.setContentText("Algorithme :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(choixAlgo -> algorithmeChoisi = choixAlgo);
    }

    public static int getNombreColonnes() {
        try {
            return Integer.parseInt(hField.getText());
        } catch (NumberFormatException e) {
            return 20;
        }
    }

    public static int getNombreLignes() {
        try {
            return Integer.parseInt(vField.getText());
        } catch (NumberFormatException e) {
            return 20;
        }
    }

    public static String getAlgorithmeChoisi() {
        return algorithmeChoisi;
    }
}
