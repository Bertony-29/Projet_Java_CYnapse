import java.util.*;

public class Maze {
    private int width;
    private int height;
    private List[][] maze = new List[width][height] ;
    private boolean isPerfect ;

    // Maze class constructor(s ?) :

    // Create an object Maze
    public Maze(int width, int height){
        this.width = width;
        this.height = height ;
    }

     //Maze class methods :

     public void addCell(Cell c){
        for(int i = 0;i<width;i++ ){
            for(int j = 0;i<height;j++){
                if (maze[i][j].isEmpty()){
                    maze[i][j].add(c);
                }
            }
        }
    }

     public static void main(String[] args){
        //tests
        
        Maze maze1 = new Maze(5,6 );
        System.out.println(maze1.width);
        System.out.println(maze1.height);
        Cell c = new Cell(1,2);
        System.out.println(maze1.addCell(c));    

     }





    
}
