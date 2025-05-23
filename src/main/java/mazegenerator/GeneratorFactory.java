package com.example.demo.generator;

/**
 * Factory for creating maze generators
 */
public class GeneratorFactory {
    
    /**
     * Creates a maze generator based on the specified algorithm name
     * @param generatorName The name of the generator algorithm
     * @return A maze generator implementation
     */
    public static MazeGenerator createGenerator(String generatorName) {
        switch (generatorName) {
            case "Kruskal":
                return new KruskalMazeGenerator();
            case "Prim":
            default:
                return new PrimMazeGenerator();
        }
    }
    
    /**
     * Gets the names of all available generator algorithms
     * @return Array of generator names
     */
    public static String[] getGeneratorNames() {
        return new String[] {"Prim", "Kruskal"};
    }
}
