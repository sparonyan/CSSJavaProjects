package csc143.gol;

import java.io.*;

/**
 * Programming Assignment 7: Game of Life, Serialization and Final Submission
 * <p>
 * The class represents csc143.gol.MyGameOfLife object that holds and updates the
 * status of the cellular automaton game. This class contains constructor and
 * overridden methods such as getCellState, setCellState, nextGeneration, toString.
 *
 * @author Satine Paronyan
 * @version ChallengePA1, StandardPA7
 */
public class MyGameOfLife extends java.util.Observable implements GameOfLife,
        GoBoardConstants {
        //CheckerBoardConstants {

    // Serializable
    private int[][] mainBoard; // holds the status of the Game Of Life board's cells

    // the workingBoard is used in rulesOfLife and nextGeneration method to
    // store the values of the cells in the next generation
    private int[][] workingBoard;

    /**
     * Constructs and initializes a new mainBoard with the size 19 x 19.
     */
    public MyGameOfLife() {
        mainBoard = new int[ROW_COUNT][COLUMN_COUNT];
        workingBoard = new int[ROW_COUNT][COLUMN_COUNT];
    }

    /**
     * Reads object's state from the specified file passed and updates this objects.
     * @param file the given input file to be read
     * @throws java.io.IOException if given file cannot be found or opened.
     */
    public void fileOpen(java.io.File file) throws java.io.IOException {
        try (FileInputStream inFileStream = new FileInputStream(file);
             ObjectInputStream objInStream = new ObjectInputStream(inFileStream)) {
            mainBoard = (int[][])objInStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    /**
     * Writes and saves this objects state to the specified file.
     * @param file the given output file to be written
     * @throws java.io.IOException when something is wrong writing to the file
     */
    public void fileSave(java.io.File file) throws java.io.IOException {
        try (FileOutputStream outFile = new FileOutputStream(file);
             ObjectOutputStream objStream = new ObjectOutputStream(outFile)) {
            objStream.writeObject(mainBoard);
        }
    }

    /**
     * Determines whether the FIRST_ROW and FIRST_COLUMN count starts from zero.
     * @return true if the count starts from 0 otherwise false.
     */
    protected static boolean isCountFromZero() {
        if (FIRST_ROW == 0 && FIRST_COLUMN == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the integer value of the given cell.
     * <p>Precondition: The row and column numbers must be in the range 1 - 19
     * @param row The row number of the given cell
     * @param col The column number of the given cell
     * @return value of the given cell
     * @throws IllegalArgumentException if the given values are not in the rage 1 - 19
     */
    @Override
    public int getCellState(int row, int col) {
        // input validation

        if (row < FIRST_ROW || row > LAST_ROW) {
            throw new IllegalArgumentException("The row number must be >= " +
                    FIRST_ROW + " or <= " + LAST_ROW);
        } else if (col < FIRST_COLUMN || col > LAST_COLUMN) {
            throw new IllegalArgumentException("The column number must be >= " +
                    FIRST_COLUMN + " or <= " + LAST_COLUMN);
        }
            return mainBoard[row - FIRST_ROW][col - FIRST_COLUMN];
    }

    /**
     * Sets the given state to the given cell.
     * <p>Precondition: The row and column numbers must be in the range 1 - 19
     * and the cell's state must be 0 for DEAD or 1 for ALIVE.
     * @param row The row number of the given cell
     * @param col The column number of the given cell
     * @param state The new state for the given cell.
     */
    @Override
    public void setCellState(int row, int col, int state) {
        // input validation
        if (row < FIRST_ROW || row > LAST_ROW) {
            throw new IllegalArgumentException("The row number must be >= " +
                    FIRST_ROW + " or <= " + LAST_ROW);
        } else if (col < FIRST_COLUMN || col > LAST_COLUMN) {
            throw new IllegalArgumentException("The column number must be >= " +
                    FIRST_COLUMN + " or <= " + LAST_COLUMN);
        } else if (state != ALIVE && state != DEAD) {
            throw new IllegalArgumentException("The state of a cell must be 0 for DEAD or 1 for ALIVE.");
        }
            mainBoard[row - FIRST_ROW][col - FIRST_COLUMN] = state;
    }

    /**
     * Calculates the next generation of the cellular automation
     */
    @Override
    public void nextGeneration() {
        // check each cell's neighbors state and count only the ones that are ALIVE
        for (int col = 0; col < COLUMN_COUNT; col++) {
            for (int row = 0; row < ROW_COUNT; row++) {
                int nCount = 0; // ALIVE neighbors counter

                int maxI = 1;
                int minI = -1;
                if (row == ROW_COUNT - 1) { // right edge of the board
                    maxI = 0;
                } else if (row == 0) { // left edge of the row
                    minI = 0;
                }

                int maxJ = 1;
                int minJ = -1;
                if (col == COLUMN_COUNT - 1) { // bottom edge of the board
                    maxJ = 0;
                } else if (col == 0) { // top edge of the board
                    minJ = 0;
                }
                for (int i = minI; i <= maxI; i++) {
                    for (int j = minJ; j <= maxJ; j++) {
                        // ignore current cell's state then check the neighbor's state
                        if ((i != 0 || j != 0) && mainBoard[row + i][col + j] == GameOfLife.ALIVE) {
                            nCount++;
                        }
                    }
                }

                // update the state of each cell on the board in the next generation
                // and store in the working workingBoard
                rulesOfLife(row, col, nCount);
            }
        }

        // update the current board
        int[][] temp = mainBoard;
        mainBoard = workingBoard;
        workingBoard = temp;
    }

    /**
     * Determines the state for each cell of the current board in the next
     * generation by applying the Rules Of Life
     * @param row row of the given cell
     * @param col column of the given cell
     * @param nCount the number of ALIVE neighbors
     */
    private void rulesOfLife(int row, int col, int nCount) {
        if ((mainBoard[row][col] == GameOfLife.ALIVE) && (nCount < 2)) {
            workingBoard[row][col] = GameOfLife.DEAD; // dies from loneliness
        } else if ((mainBoard[row][col] == GameOfLife.ALIVE) && (nCount > 3)) {
            workingBoard[row][col] = GameOfLife.DEAD; // dies due to overcrowding
        } else if ((mainBoard[row][col] == GameOfLife.DEAD) && (nCount == 3)) {
            workingBoard[row][col] = GameOfLife.ALIVE; // birth
        } else {
            workingBoard[row][col] = mainBoard[row][col]; // remains the same
        }
    }

    /**
     * Returns the String representation on the given cell's value.
     * If the state of a cell is ALIVE, which is 1 then return "O",
     * otherwise "." for a DEAD cell.
     * @param cell the value of the cell on the mainBoard.
     * @return the String representation on the given cell's value.
     */
    private String cellState(int cell) {
        if (cell == GameOfLife.ALIVE) {
            return "O";
        } else {
            return ".";
        }
    }

    /**
     * Returns the constructed String representation of the board
     * @param board the board of the Game Of Life
     * @return String representation of the board
     */
    private String displayBoard(int[][] board) {
        StringBuilder strRow = new StringBuilder();
        // loop through rows
        for (int i = 0; i < board.length; i++) {
            strRow.append(cellState(board[i][0])); // the first cell in each row
            // the rest of the cells in a row
            for (int j = 1; j < board[i].length; j++) {
                strRow.append(" ");
                strRow.append(cellState(board[i][j]));
            }
            strRow.append("\n"); //go to the next line
        }
        return strRow.toString();
    }

    /**
     * Returns the String representation of the Game of Life board.
     * @return String representing this object.
     */
    @Override
    public String toString() {
        return displayBoard(mainBoard);
    }
}
