import csc143.gol.GameOfLife;
import csc143.gol.MyGameOfLife;

import javax.swing.*;

/**
 * This class initializes a new game, sets some cells on the board to state ALIVE
 * and test the game to make sure the program works properly.
 *
 * @author Satine Paronyan
 */
public class TestGameOfLife {
    /**
     * Sets the state for some cells on the board, which by this creates
     * different moving and not moving forms.
     * @param board the Game Of Life board
     */
    public static void setBoard(GameOfLife board) {
        // the two alive cells in the upper left
        board.setCellState(3, 4, GameOfLife.ALIVE);
        board.setCellState(4, 4, GameOfLife.ALIVE);
        // the block in the upper right
        board.setCellState(3, 13, GameOfLife.ALIVE);
        board.setCellState(3, 14, GameOfLife.ALIVE);
        board.setCellState(4, 13, GameOfLife.ALIVE);
        board.setCellState(4, 14, GameOfLife.ALIVE);
        // the beehive in the center
        board.setCellState(8, 7, GameOfLife.ALIVE);
        board.setCellState(8, 8, GameOfLife.ALIVE);
        board.setCellState(9, 6, GameOfLife.ALIVE);
        board.setCellState(9, 9, GameOfLife.ALIVE);
        board.setCellState(10, 7, GameOfLife.ALIVE);
        board.setCellState(10, 8, GameOfLife.ALIVE);
        // the glider om the lower left
        board.setCellState(15, 6, GameOfLife.ALIVE);
        board.setCellState(16, 4, GameOfLife.ALIVE);
        board.setCellState(16, 6, GameOfLife.ALIVE);
        board.setCellState(17, 5, GameOfLife.ALIVE);
        board.setCellState(17, 6, GameOfLife.ALIVE);
        // the blinker in the lower right
        board.setCellState(13, 13, GameOfLife.ALIVE);
        board.setCellState(13, 14, GameOfLife.ALIVE);
        board.setCellState(13, 15, GameOfLife.ALIVE);
    }

    /**
     * Runs the program for the testing purposes.
     * @param args command-line parameter
     */
    public static void main(String[] args) {
        GameOfLife life;
        life = new MyGameOfLife();
        setBoard(life);
        for (int i = 0; i < 7; i++) {
            System.out.println("Generation " + i + ":");
            System.out.println(life.toString());
            System.out.println();
            life.nextGeneration();
           // JOptionPane.showMessageDialog(null, "Click OK to continue");
        }
    }

}
