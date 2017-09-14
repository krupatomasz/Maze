package mazegenerator;
import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Tomek
 */
public class BoardRenderer extends JPanel{
    private final Board board;
    private final Cell[][] map;
    private final int CELL_SIZE;
    private aStarSolver solver;
    private boolean renderNodes = false, renderPath = false;
    
    public BoardRenderer(Board board, int pixels){
        this.board = board;
        this.map = board.getBoard();
        CELL_SIZE = pixels;
        solver = null;
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        for(int i=0; i<board.getWidth(); i++)
            for(int j=0; j<board.getHeight(); j++)
                drawCell(i, j, g);
        
        if(solver != null){
            if(renderPath)
                solver.renderPath(g);
            if(renderNodes)
                solver.renderNodeMap(g);
        }
    }
    
    private void drawCell(int x, int y, Graphics g){
        Cell cell = map[x][y];
        Color color = cell.isWall() ? Color.BLACK : Color.WHITE;
        g.setColor(color);
        g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
    
    public void setSolver(aStarSolver solver){
        this.solver = solver;
    }
    
    public void toggleNodeRendering(){
        renderNodes = !renderNodes;
        repaint();
    }
    
    public void toggleSolutionRendering(){
        renderPath = !renderPath;
        repaint();
    }
}
