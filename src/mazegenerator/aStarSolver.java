package mazegenerator;
import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
/**
 *
 * @author Tomek
 */
public class aStarSolver {
    TreeSet<Node> open;
    HashSet<Node> closed;
    Node startNode, endNode;
    NodeMapper nm;
    
    public aStarSolver(Board board, BoardRenderer br) {
        nm = new NodeMapper(board);
        nm.createMap();
        br.setSolver(this);
        startNode = nm.getStartNode();
        endNode = nm.getEndNode();
        solve();
    }
    
    private void solve(){
        open = new TreeSet(new FComp());
        closed = new HashSet();
        
        startNode.setG(0);
        startNode.setH(euclidianHeuristic(startNode, endNode));
        open.add(startNode);
        
        while(!open.isEmpty()){
            Node tmp = open.pollFirst();
            //System.out.println(tmp.getCell().getX() + " " + tmp.getCell().getY());
            closed.add(tmp);
            if(tmp == endNode){
                break;
            }
            for(Node neighbour : tmp.getNeighbours()){
                if(closed.contains(neighbour))
                    continue;
                
                neighbour.updateG(tmp);
                if(neighbour.getH() == -1)
                    neighbour.setH(euclidianHeuristic(neighbour, endNode));
                if(open.contains(neighbour))
                    open.remove(neighbour);
                open.add(neighbour);
            }
        }
        //System.out.println(endNode.getPrev().getCell().getY());
    }
    
    private double euclidianHeuristic(Node node1, Node node2){
        int x = node1.getCell().getX();
        int y = node1.getCell().getY();
        int endX = node2.getCell().getX();
        int endY = node2.getCell().getY();
        double diffX = Math.abs(x-endX), diffY = Math.abs(y-endY);
        return Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));
    }
    
    public void renderPath(Graphics g){
        if(endNode.getPrev() == null)
            return;
        
        Node tmp = endNode;
        g.setColor(Color.green);
        do{
            Cell c = tmp.getCell();
            Cell prev = tmp.getPrev().getCell();
            int x = c.getX(), y = c.getY();
            int pixels = MazeGenerator.pixels;
            g.fillRect(x*pixels, y*pixels, pixels, pixels);
            paintBridge(c, prev, g);
            tmp = tmp.getPrev();
        } while(tmp != startNode);
    }
    
    private void paintBridge(Cell c1, Cell c2, Graphics g){
        int x1 = c1.getX(), y1 = c1.getY();
        int x2 = c2.getX(), y2 = c2.getY();
        int diffX = Math.abs(x1-x2)+1, diffY = Math.abs(y1-y2)+1;
        Direction vector = Board.getVector(c2, c1);
        Cell start = c2;
        int pixels = MazeGenerator.pixels;
        if(vector == Direction.UP || vector == Direction.LEFT){
            start = c1;
        }
        g.fillRect(start.getX()*pixels, start.getY()*pixels, pixels*diffX, pixels*diffY);
    }
    
    
    
    public void renderNodeMap(Graphics g){
        nm.renderNodeMap(g);
    }
}

class FComp implements Comparator<Node>{
 
    @Override
    public int compare(Node n1, Node n2) {
        if(n1.getF() > n2.getF()){
            return 1;
        } else {
            return -1;
        }
    }
}