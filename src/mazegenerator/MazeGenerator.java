package mazegenerator;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
/**
 *
 * @author Tomek
 */
public class MazeGenerator {
    public static int pixels = 5, cells = 191;
    public static void main(String[] args) {
        
        JFrame window = new JFrame();
        window.setResizable(false);
        
        Board board = new Board(cells, cells);
        BoardRenderer br = new BoardRenderer(board, pixels);
        
        MouseListener nodesML = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                br.toggleNodeRendering();
            }
        };
        JButton nodeButton = new JButton("Nodes");
        nodeButton.addMouseListener(nodesML);
        nodeButton.setLocation(10, pixels*cells);
        nodeButton.setSize(80, 25);
        
        MouseListener pathML = new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                br.toggleSolutionRendering();
            }
        };
        JButton pathButton = new JButton("Path");
        pathButton.addMouseListener(pathML);
        pathButton.setLocation(100, pixels*cells);
        pathButton.setSize(80, 25);
        
        window.add(nodeButton);
        window.add(pathButton);
        window.add(br);
        window.pack();
        
        Insets insets = window.getInsets();
        int hor = insets.left + insets.right;
        int ver = insets.bottom + insets.top;
        window.setSize(pixels*cells+hor, pixels*cells+ver+25);
        
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);
        aStarSolver solver = new aStarSolver(board, br);
    }    
}
