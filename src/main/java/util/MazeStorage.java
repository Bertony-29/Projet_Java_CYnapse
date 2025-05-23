package src.main.java.util;

import src.main.java.model.SavedMaze;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Class for managing maze saving and loading
 */
public class MazeStorage {
    private static final String STORAGE_DIR = "saved_mazes";
    private static final String FILE_EXTENSION = ".maze";
    
    /**
     * Saves a maze
     * @param maze The maze to save
     * @return true if the save was successful, false otherwise
     */
    public static boolean saveMaze(SavedMaze maze) {
        try {
            // Create the save directory if it doesn't exist
            Path dirPath = Paths.get(STORAGE_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            
            // Create the save file
            String filename = sanitizeFilename(maze.getName()) + FILE_EXTENSION;
            Path filePath = dirPath.resolve(filename);
            
            // Serialize the SavedMaze object
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new BufferedOutputStream(Files.newOutputStream(filePath)))) {
                oos.writeObject(maze);
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Loads a saved maze
     * @param filename Name of the file to load
     * @return The loaded maze or null in case of error
     */
    public static SavedMaze loadMaze(String filename) {
        try {
            Path filePath = Paths.get(STORAGE_DIR, filename);
            
            // Deserialize the SavedMaze object
            try (ObjectInputStream ois = new ObjectInputStream(
                    new BufferedInputStream(Files.newInputStream(filePath)))) {
                return (SavedMaze) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets the list of saved mazes
     * @return List of filenames of saved mazes
     */
    public static List<String> getSavedMazeFiles() {
        List<String> files = new ArrayList<>();
        
        try {
            Path dirPath = Paths.get(STORAGE_DIR);
            if (!Files.exists(dirPath)) {
                return files; // Returns an empty list if the directory doesn't exist
            }
            
            // List all files with the .maze extension
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*" + FILE_EXTENSION)) {
                for (Path path : stream) {
                    files.add(path.getFileName().toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return files;
    }
    
    /**
     * Gets the list of saved mazes
     * @return List of saved mazes
     */
    public static List<SavedMaze> getSavedMazes() {
        List<SavedMaze> mazes = new ArrayList<>();
        
        for (String filename : getSavedMazeFiles()) {
            SavedMaze maze = loadMaze(filename);
            if (maze != null) {
                mazes.add(maze);
            }
        }
        
        return mazes;
    }
    
    /**
     * Deletes a saved maze
     * @param filename Name of the file to delete
     * @return true if the deletion was successful, false otherwise
     */
    public static boolean deleteMaze(String filename) {
        try {
            Path filePath = Paths.get(STORAGE_DIR, filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Sanitizes a filename by replacing invalid characters with underscores
     */
    private static String sanitizeFilename(String name) {
        return name.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}
