package mazegenerator;

/**
 *
 * @author Tomek
 */
public class Cell {
    public enum CellType { PASSAGE, WALL }
    
    private final Point coords;
    private CellType type;
    private boolean visited;
    private Node node = null;
    
    public Cell(Point coords){
        this.coords = coords;
        type = CellType.WALL;
        visited = false;
    }
    
    public void setToPassage(){
        type = CellType.PASSAGE;
    }
    
    public boolean isWall(){
        return type == Cell.CellType.WALL;
    }
    
    public Point getCoords(){
        return coords;
    }
    
    public void setAsVisited(){
        visited = true;
    }
    
    public boolean isVisited(){
        return visited;
    }
    
    public int getX(){
        return coords.getX();
    }
    
    public int getY(){
        return coords.getY();
    }
    
    public void setNode(Node node){
        this.node = node;
    }
    
    public Node getNode(){
        return node;
    }
    
    public void getVectorTo(Cell other){
        int ox = other.getX(), oy = other.getY();
        
    }
}
