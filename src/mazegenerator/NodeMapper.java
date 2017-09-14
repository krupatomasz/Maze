package mazegenerator;

import java.awt.Color;
import java.util.*;
import java.awt.Graphics;

/**
 *
 * @author Tomek
 */
public class NodeMapper {
    private Board board;
    private Cell[][] map;
    private Node startNode, endNode;

    public NodeMapper(Board board) {
        this.board = board;
        this.map = board.getBoard();
        this.startNode = endNode = null;
    }
    
    public Node getStartNode() { return startNode; }
    public Node getEndNode() { return endNode; }
    
    public void createMap(){
        Cell start = null;
        for(int i=0; i<board.getWidth(); i++){
            if(!map[i][0].isWall()){
                start = map[i][0];
            }
        }
        if(start != null){
            startNode = new Node(start);
            start.setNode(startNode);
            searchForNeighbours(startNode, Direction.DOWN);
        }
    }
    
    private void searchForNeighbours(Node node, Direction vector){
        int x = node.getCell().getX(), y = node.getCell().getY();
        Cell found;
        do{
            x = Board.tweakX(x, vector);
            y = Board.tweakY(y, vector);
            found = map[x][y];
            if(found.isWall())
                return;
        } while(!isNode(found));
        
        if(found.getNode() != null)
            return;
        
        
        Node next = new Node(found);
        found.setNode(next);
        node.addNeighbour(next);
        next.addNeighbour(node);
        
        
        
        if(y == board.getHeight() - 1){
            endNode = next;
            return;
        }
        
        for(Direction dir : Direction.values()){
            if(dir != getOppositeDir(vector))
                searchForNeighbours(next, dir);
        }
    }
    
    
    
    public Direction getOppositeDir(Direction dir){
        switch(dir){
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case LEFT:
                return Direction.RIGHT;
            default:
                return Direction.LEFT;
        }
    
    }
    
    private boolean isNode(Cell cell){
        
        Point coords = cell.getCoords();
        int x = coords.getX(), y = coords.getY();
        if(y == board.getHeight() - 1)
            return true;
        
        
        List<Cell> adjPassages = new ArrayList();
        addIfPassage(adjPassages, map[x][y-1]);        
        addIfPassage(adjPassages, map[x][y+1]);        
        addIfPassage(adjPassages, map[x+1][y]);
        addIfPassage(adjPassages, map[x-1][y]);
        int size =  adjPassages.size();
        return size != 2 || isCorner(adjPassages);
    }
    
    private void addIfPassage(List<Cell> list, Cell cell){
        if(!cell.isWall())
            list.add(cell);
    }
    
    private boolean isCorner(List<Cell> adjPassages){ 
        boolean corner = false;
        if(adjPassages.size() == 2){
            Cell c1 = null, c2 = null;
            for(Cell passage : adjPassages){
                if(c1 == null)
                    c1 = passage;
                else
                    c2 = passage;
            }
            if(c1.getX() != c2.getX() && c1.getY() != c2.getY())
                corner = true;
        }
        return corner;
    }
    
    public void renderNodeMap(Graphics g){
        List<Node> open = new ArrayList(), closed = new ArrayList();
        open.add(startNode);
        while(!open.isEmpty()){
            Node current = open.get(0);
            open.remove(current);
            closed.add(current);
            int pixels = MazeGenerator.pixels;
            int x = current.getCell().getX() * pixels;
            int y = current.getCell().getY() * pixels;
            g.setColor(new Color(0x008050));
            g.fillOval(x, y, pixels, pixels);
            for(Node neighbour : current.getNeighbours()){
                if(!closed.contains(neighbour))
                open.add(neighbour);
            }
        }
        
    }
}
