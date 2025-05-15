import java.util.*;

public class Maze {
    private int width;
    private int height;
    //private List[][] maze = new List[width][height] ;
    private List<List<Cell>> maze ;
    private boolean isPerfect ;

    // Maze class constructor(s ?) :

    public Maze(int width, int height){
        this.width = width;
        this.height = height ;
        List<List<Cell>> maze = new ArrayList<>();
        
        
    }

     //Maze class methods :

    public void addCell(){
    for(int i = 0;i < width ; i++ ){
        List<Cell> col = new ArrayList<>();
        for(int j = 0 ; i < height ; j++){
            col.add(new Cell(i, j));
            }
        maze.add(col);
    }
    }
     
    /*public void affichers(){
        for(int i = 0; i <width;i++ ){
            for(int j = 0; j <height;j++){
                /*if (maze[i][j].isEmpty()){
                    maze[i][j].add(c);
                }
                System.out.print(" | " + maze[i][j] );
            }
            System.out.println(" | ");
        }
        System.out.println();
    }*/

    public void afficher(){
        for(List<Cell> col : maze){
           for(Cell c : col){
            System.out.print(" | " + c.getIsVisited());

           }
        System.out.println(" | "); 
        }
        System.out.println();
    }
     public static void main(String[] args){
        //tests

        Maze maze1 = new Maze(8,8);
        System.out.println(maze1.width);
        maze1.afficher();
       

     }





    
}
