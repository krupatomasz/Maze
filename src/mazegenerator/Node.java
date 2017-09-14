package mazegenerator;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Tomek
 */
public class Node {
    private final Cell cell;
    private final List<Node> neighbours;
    private double g, h;
    private Node prev;

    public Node(Cell cell) {
        this.cell = cell;
        neighbours = new ArrayList();
        prev = null;
        g = h = -1;
    }
    
    public void addNeighbour(Node neighbour){
        neighbours.add(neighbour);
    }
    
    public Cell getCell() { return cell; }
    
    public List<Node> getNeighbours() { return neighbours; } 
    
    public double getF(){ return g+h; }
    public double getG() { return g; }
    
    public void updateG(Node parent) {
        Cell pCell = parent.getCell();
        double distToParent, pathThroughParent;
        if (cell.getX() == pCell.getX())
            distToParent = Math.abs(cell.getY() - pCell.getY());
        else
            distToParent = Math.abs(cell.getX() - pCell.getX());
        
        pathThroughParent = parent.getG() + distToParent;
        if(g == -1 || pathThroughParent < g){
            g = pathThroughParent;
            prev = parent;
        }
    
    }
    public void setG(double g) { this.g = g; }
    public double getH() { return h; }
    public void setH(double h) { this.h = h; }
    public Node getPrev() { return prev; }
    public void setPrev(Node prev) { this.prev = prev; }
    
    
}

