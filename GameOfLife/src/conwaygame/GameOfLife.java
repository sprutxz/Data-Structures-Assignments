package conwaygame;
import java.util.ArrayList;

import javax.swing.plaf.TreeUI;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(file);
        grid = new boolean[StdIn.readInt()][StdIn.readInt()];
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                grid[i][j] = StdIn.readBoolean();
            }
        }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        if (grid[row][col] == true)
            return ALIVE;
        return DEAD; 
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if(grid[i][j] == true)
                    return ALIVE;
            }
        }
        return DEAD;
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {
        
        // WRITE YOUR CODE HERE
        int alive = 0;
        int prevRow = row-1, nextRow = row+1, prevCol = col-1, nextCol = col+1;
        if (prevRow == -1) prevRow = grid.length-1;
        if (nextRow == grid.length) nextRow = 0;
        if (prevCol == -1) prevCol = grid[row].length-1;
        if (nextCol == grid[row].length) nextCol = 0;

        boolean [][] neighbourCells = {
            {grid[prevRow][prevCol], grid[prevRow][col], grid[prevRow][nextCol]},
            {grid[row][prevCol], false, grid[row][nextCol]},
            {grid[nextRow][prevCol], grid[nextRow][col], grid[nextRow][nextCol]},
        };
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (neighbourCells[i][j] == true)
                    alive++;
            }
        }
        return alive;
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        // WRITE YOUR CODE HERE
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if (grid[i][j] == true){
                    if (numOfAliveNeighbors(i, j) <= 1)
                        newGrid[i][j] = false;
                    if (numOfAliveNeighbors(i, j) == 2 || numOfAliveNeighbors(i, j) == 3)
                        newGrid[i][j] = true;
                    if (numOfAliveNeighbors(i, j) >= 4)
                        newGrid[i][j] = false;
                }
                else {
                    if (numOfAliveNeighbors(i, j) == 3)
                        newGrid[i][j] = true;
                }
            }
        } 
        return newGrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {

        // WRITE YOUR CODE HERE
        grid = computeNewGrid();

        totalAliveCells = 0;

        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if (grid[i][j] == true)
                    totalAliveCells++;
            }
        }
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        // WRITE YOUR CODE HERE
        for (int i = 0; i < n; i++)
            nextGeneration();
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        // WRITE YOUR CODE HERE
        WeightedQuickUnionUF union = new WeightedQuickUnionUF(grid.length, grid[0].length);
        for(int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if (getCellState(i,j)){
                    int prevRow = i-1, nextRow = i+1, prevCol = j-1, nextCol = j+1;
                    if (prevRow == -1) prevRow = grid.length-1;
                    if (nextRow == grid.length) nextRow = 0;
                    if (prevCol == -1) prevCol = grid[i].length-1;
                    if (nextCol == grid[i].length) nextCol = 0;

                    if (getCellState(prevRow, prevCol))
                        union.union(i,j,prevRow,prevCol);

                    if (getCellState(prevRow,j))
                        union.union(i,j,prevRow,j);

                    if (getCellState(prevRow, nextCol))
                        union.union(i,j,prevRow,nextCol);

                    if (getCellState(i, prevCol))
                        union.union(i,j,i,prevCol);

                    if (getCellState(i, nextCol))
                        union.union(i,j,i,nextCol);

                    if (getCellState(nextRow, prevCol))
                        union.union(i,j,nextRow,prevCol);

                    if (getCellState(nextRow, j))
                        union.union(i,j,nextRow,j);

                    if (getCellState(nextRow, nextCol))
                        union.union(i,j,nextRow,nextCol);

                    }

            }
        }
        ArrayList<Integer> roots = new ArrayList<Integer>();
        for(int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if(getCellState(i,j) && !roots.contains(union.find(i,j)))
                    roots.add(union.find(i,j));    
                }
        }
        union.print();
        return roots.size(); // update this line, provided so that code compiles
    }
}
