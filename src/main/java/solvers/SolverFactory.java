
package com.example.demo.solver;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating and managing different solving algorithms
 */
public class SolverFactory {
    private static final List<MazeSolver> availableSolvers = new ArrayList<>();
    
    static {
        // Register available algorithms
        availableSolvers.add(new DijkstraSolver());
        availableSolvers.add(new BFSSolver());
        availableSolvers.add(new AStarSolver());
        availableSolvers.add(new DFSSolver());
    }
    
    /**
     * Returns the list of available algorithms
     * @return List of available solvers
     */
    public static List<MazeSolver> getAvailableSolvers() {
        return new ArrayList<>(availableSolvers);
    }
    
    /**
     * Returns the list of names of available algorithms
     * @return List of solver names
     */
    public static List<String> getAvailableSolverNames() {
        List<String> names = new ArrayList<>();
        for (MazeSolver solver : availableSolvers) {
            names.add(solver.getName());
        }
        return names;
    }
    
    /**
     * Returns a solver by its name
     * @param name Name of the solver
     * @return The corresponding solver or null if not found
     */
    public static MazeSolver getSolverByName(String name) {
        for (MazeSolver solver : availableSolvers) {
            if (solver.getName().equals(name)) {
                return solver;
            }
        }
        // By default, return Dijkstra if the name is not found
        return availableSolvers.get(0);
    }
}
