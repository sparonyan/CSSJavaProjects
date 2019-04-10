import csc143.gol.GameOfLife;
import csc143.gol.MyGameOfLife;

/**
 * This is the sample test program for the initial Game of Life
 * implementation. It is set up to test on a 8x8 checker board.
 */
public class TestCheckerBoard {

    /**
     * Set the initial pattern of ALIVE cells
     * @param board The GameOfLife board to update
     */
    public static void setBoard(GameOfLife board) {
        // the blinker in the upper left
        board.setCellState(1, 1, GameOfLife.ALIVE);
        board.setCellState(2, 1, GameOfLife.ALIVE);
        board.setCellState(3, 1, GameOfLife.ALIVE);
        // the block in the upper right
        board.setCellState(1, 5, GameOfLife.ALIVE);
        board.setCellState(1, 6, GameOfLife.ALIVE);
        board.setCellState(2, 5, GameOfLife.ALIVE);
        board.setCellState(2, 6, GameOfLife.ALIVE);
        // the glider in the lower left
        board.setCellState(5, 4, GameOfLife.ALIVE);
        board.setCellState(6, 2, GameOfLife.ALIVE);
        board.setCellState(6, 4, GameOfLife.ALIVE);
        board.setCellState(7, 3, GameOfLife.ALIVE);
        board.setCellState(7, 4, GameOfLife.ALIVE);
    }
    
    /**
     * The driver for this test case
     * @param args The command-line arguments
     */
    public static void main(String[] args) {
        GameOfLife life = null;
        life = new MyGameOfLife();
        setBoard(life);
        System.out.println("Starting point:");
        System.out.println(life.toString());
        System.out.println();
        life.nextGeneration();
        System.out.println("First Generation:");
        System.out.println(life);
        System.out.println();
        life.nextGeneration();
        System.out.println("Second Generation:");
        System.out.println(life);
        System.out.println();
    }

}
