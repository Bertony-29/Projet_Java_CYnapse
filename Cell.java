public class Cell {
    private int x,y ; // cell coordinates in the maze
    private boolean isVisited = false ; // Determine if a cell is visited
    private boolean up = false ;
    private boolean bottom = false ;
    private boolean right = false ;
    private boolean left = false ;
    
    
    //Cell class constructor(s ?) :

    // Create an object cell
    public Cell(int x, int y){
        this.x = x;
        this.y = y;
        //System.out.println("(" + x + "," + y + ")");
    }   

    //Cell class methods :

    // Return if a cell is visited
    public boolean getIsVisited(){
        return isVisited;
    }

    // Change the value of the variable isVisited
    public boolean setIsVisited(boolean isVisited){
        this.isVisited = isVisited ;
        return isVisited ;
    }

    

    public static void main(String[] args){
        // tests

        Cell c = new Cell(1,  2);
        System.out.println(c.isVisited);


        
        
    }
}
