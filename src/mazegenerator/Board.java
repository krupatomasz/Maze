package mazegenerator;
import java.util.*;


public class Board {
    
    
    private final int width, height;
    private Cell[][] board;
    
    Board(int width, int height){
        this.width = width;
        this.height = height;
        generateMaze();
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public Cell[][] getBoard(){
        return board;
    }
    
    private void generateMaze(){
        board = new Cell[width][height];
        fillWithWalls();
        List<Cell> wallQueue = new ArrayList();
        createStartingCell(wallQueue);
        while(!wallQueue.isEmpty()){
            createBranch(wallQueue);
        }
        createStartAndEnd();
    }
    
    private Cell createStartingCell(List<Cell> wallQueue){
        Point startingPoint = getStartingPoint();
        int startX = startingPoint.getX();
        int startY = startingPoint.getY();
        Cell startingCell = board[startX][startY];
        startingCell.setToPassage();
        addAdjacentWalls(startingCell, wallQueue);
        return startingCell;
    }
    
    private void createBranch(List<Cell> queue){
        int index = getRandomCellIndex(queue);
        Cell randWall = queue.get(index);
        int x = randWall.getCoords().getX(), y = randWall.getCoords().getY();
        randWall.setAsVisited();
        if(canChange(randWall)){
            Direction vector = getVector(randWall);
            Cell nextCell = getNextCell(randWall, vector);
            randWall.setToPassage();
            nextCell.setToPassage();
            addAdjacentWalls(nextCell, queue);
        }
        queue.remove(randWall);
        
    }
    
    private Direction getVector(Cell newCell){ //searches for the first adjacent Cell, return opposite dir
        Cell[] adjCells = getAdjacentCells(newCell);
        Cell prev = null;
        for(Cell c : adjCells)
            if(c!=null && !c.isWall()){
                prev = c;
                break;
            }
        return getVector(prev, newCell);
    }
    
    public static Direction getVector(Cell prev, Cell current){
        int prevX = prev.getX(), prevY = prev.getY();
        int x = current.getX(), y = current.getY();
        int diffX = x - prevX, diffY = y - prevY;
        if(diffX < 0)
            return Direction.LEFT;
        else if(diffX > 0)
            return Direction.RIGHT;
        else if(diffY < 0)
            return Direction.UP;
        else
            return Direction.DOWN;        
    }
    
    private Cell getNextCell(Cell prev, Direction vector){
        int x = prev.getX(), y = prev.getY();
        x = tweakX(x, vector);
        y = tweakY(y, vector);
        return board[x][y];
    }
    
    public static int tweakX(int x, Direction vector){
        if(vector == Direction.RIGHT)
            x++;
        else if(vector == Direction.LEFT)
            x--;
        return x;
    }
    
    public static int tweakY(int y, Direction vector){
        if(vector == Direction.DOWN)
            y++;
        else if(vector == Direction.UP)
            y--;
        return y;
    }
    
    
    private void fillWithWalls(){
        for(int i=0; i<width; i++)
            for(int j=0; j<height; j++){
                Point coords = new Point(i, j);
                board[i][j] = new Cell(coords);
            }
    }
    
    private Point getStartingPoint(){
        int x = generateOddCoord(true);
        int y = generateOddCoord(false);
        Point sp = new Point(x, y);
        return sp;
    }
    
    private int generateOddCoord(boolean isX){
        int dim = isX ? width : height;
        Random gen = new Random();
        int c = gen.nextInt(dim/2-1);
        c*=2;
        c++;
        return c;
    }
    
    private void addAdjacentWalls(Cell cell, List<Cell> queue){
        Cell[] adjacentCells = getAdjacentCells(cell);
        for(Cell neighbour : adjacentCells)
            if(neighbour != null && neighbour.isWall()
                    && !neighbour.isVisited() && !queue.contains(neighbour))
                queue.add(neighbour);        
    }
    
    private boolean isPointValid(Point point){
        int x = point.getX(), y = point.getY();
        return x > 0 && x < width-1 && y > 0 && y < height-1;
    }
    
    private Cell[] getAdjacentCells(Cell cell){
        Point coords = cell.getCoords();
        int x = coords.getX(), y = coords.getY();
        Cell[] adjacentCells = new Cell[4];
        adjacentCells[0] = getCellIfValid(x, y-1);
        adjacentCells[1] = getCellIfValid(x, y+1);
        adjacentCells[2] = getCellIfValid(x-1, y);
        adjacentCells[3] = getCellIfValid(x+1, y);
        return adjacentCells;        
    }
    
    private Cell getCellIfValid(int x, int y){
        Cell cell = isPointValid(new Point(x, y)) ? board[x][y] : null;
        return cell;
    }
    
    private int getRandomCellIndex(List<Cell> queue){
        int size = queue.size();
        Random gen = new Random();
        return gen.nextInt(size);
    }
    
    private boolean canChange(Cell wall){
        List<Cell> adjPassages = getAdjacentPassages(wall);
        return adjPassages.size() == 1;            
    }
    
    public List getAdjacentPassages(Cell cell){
        Cell[] adjCells = getAdjacentCells(cell);
        List<Cell> passages = new ArrayList();
        for(Cell c : adjCells)
            if(c != null && !c.isWall())
                passages.add(c);
        return passages;
    }
    
    private void createStartAndEnd(){
        int startX = generateOddCoord(true);
        int endX = generateOddCoord(true);
        board[startX][0].setToPassage();
        board[endX][height-1].setToPassage();
    }
}
