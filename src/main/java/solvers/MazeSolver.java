//l’interface commune à tous tes algorithmes de résolution de labyrinthe.
//garanti une méthode solve() que tous les solvers doivent implémenter
package solvers;

import maze.Maze;
import java.util.List;
import javafx.scene.paint.Color;

public interface MazeSolver {
    /**
     * Résout le labyrinthe en utilisant l'algorithme spécifique.
     * @param maze Le labyrinthe à résoudre
     * @param startX Coordonnée X de départ
     * @param startY Coordonnée Y de départ
     * @param endX Coordonnée X d'arrivée
     * @param endY Coordonnée Y d'arrivée
     * @return Une liste de coordonnées constituant le chemin solution
     */
    List<int[]> solve(Maze maze, int startX, int startY, int endX, int endY);

    /**
     * Retourne la liste des cellules visitées pendant la résolution.
     * @return Une liste de coordonnées des cellules visitées
     */
    List<int[]> getVisitedCells();

    /**
     * Retourne le temps de calcul de la résolution.
     * @return Le temps en millisecondes
     */
    long getExecutionTime();

    /**
     * Retourne la couleur associée à l'algorithme pour l'affichage.
     * @return La couleur de l'algorithme
     */
    Color getColor();

    /**
     * Retourne le nom de l'algorithme.
     * @return Le nom de l'algorithme
     */
    String getName();
}
