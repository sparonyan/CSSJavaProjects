package csc143.gol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;

/**
 * Game of Life, Graphical Interface;
 * <p>The class represents csc143.gol.GameOfLifeBoard object, which provides the graphical user interface
 * and control for the Game Of Life.
 *
 * @author Satine Paronyan
 */
public class GameOfLifeBoard extends JPanel implements ActionListener, MouseListener, java.util.Observer {

    private GameOfLife game;

    private static final int BOARD_SIZE = 19; // board's number of rows and cells
    private static final int CELL_SIZE = 25; // width and height of each cell

    /**
     * Constructs and initializes csc143.gol.GameOfLifeBoard.
     * @param gameOfLife the cellular automation game
     */
    public GameOfLifeBoard(GameOfLife gameOfLife) {
        game = gameOfLife;
        setPreferredSize(new Dimension(BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE));

        // set all cells' state DEAD
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                game.setCellState(i + MyGameOfLife.FIRST_ROW, j + MyGameOfLife.FIRST_COLUMN, GameOfLife.DEAD);
            }
        }
        // creates mouse listener object for this class
        addMouseListener(this);
    }

    /**
     * Called whenever the observed object is changed.
     * @param o the Observable object that is notified
     * @param args an argument passed to the notifyObservers method
     */
    @Override
    public void update(java.util.Observable o, Object args) {
        repaint();
    }

    /**
     * Visualizes ALIVE cells on the board as the green ovals.
     * @param g the Graphics object for visualizing ALIVE cell on the board
     * @param row the row number of the given cell
     * @param col the column number of the given cell
     */
    public void paintAliveCell(Graphics g, int row, int col) {
        int Row = row;
        int Col = col;

        if (MyGameOfLife.isCountFromZero()) { // if FIRST_ROW and LAST_ROW == 0, add 1 to given row and column
               Row++;
               Col++;
            }
        int x = Col * CELL_SIZE; // x coordinate of the cell's top-left corner
        int y = Row * CELL_SIZE; // y coordinate of the cell's top-left corner


        int currentRow = row;
        int currentCol = col;
        if (!MyGameOfLife.isCountFromZero()) {
            currentRow++;
            currentCol++;
        }
        if(game.getCellState(currentRow, currentCol) == game.ALIVE) {
            g.setColor(Color.GREEN);
            g.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        }
    }

    /**
     * Paints ALIVE cells on the board.
     * @param g the Graphics object used as argument for a method call
     */
    public void updateCell(Graphics g) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                paintAliveCell(g, row, col);
            }
        }
    }

    /**
     * Paints this component.
     * @param g the Graphics object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // visualize Alive cells
        updateCell(g);

        // draw board using lines
        g.setColor(Color.BLACK);
        // horizontal lines
        int yHLine = 0;
        final int xHLine = getWidth();
        for (int i = 0; i <= BOARD_SIZE; i++) {
            g.drawLine(0, yHLine, xHLine, yHLine);
            yHLine += CELL_SIZE;
        }
        // vertical lines
        final int yVLine = getHeight();
        int xVLine = 0;
        for (int j = 0; j <= BOARD_SIZE; j++) {
            g.drawLine(xVLine, 0, xVLine, yVLine);
            xVLine += CELL_SIZE;
        }
    }

    /**
     * Performs an action on this csc143.gol.GameOfLifeBoard,when a meaningful action occurred,
     * such as calculate the next generation of the cellular automation and update
     * the cells on the board.
     *
     * @param a the event generated by pressing the "Next Generation" button
     */
    @Override
    public void actionPerformed(ActionEvent a) {
        game.nextGeneration();
        repaint();
    }

    /**
     * Sets all the cells on the board DEAD.
     */
    public void clearBoard() {
        // set all cells' state DEAD
        for (int i = 1; i <= BOARD_SIZE; i++) {
            for (int j = 1; j <= BOARD_SIZE; j++) {
                if (MyGameOfLife.isCountFromZero()) {
                    game.setCellState(i - 1, j - 1, GameOfLife.DEAD);
                } else {
                    game.setCellState(i, j, GameOfLife.DEAD);
                }

            }
        }
        repaint();
    }

    /**
     * Starts the game by clearing the board and setting the state for some cells on the board
     * to ALIVE, which by this creates different moving and not moving forms.
     */
    public void startGame() {
        clearBoard();
        // the two alive cells in the upper left
        game.setCellState(3, 4, GameOfLife.ALIVE);
        game.setCellState(4, 4, GameOfLife.ALIVE);
        // the block in the upper right
        game.setCellState(3, 13, GameOfLife.ALIVE);
        game.setCellState(3, 14, GameOfLife.ALIVE);
        game.setCellState(4, 13, GameOfLife.ALIVE);
        game.setCellState(4, 14, GameOfLife.ALIVE);
        // the beehive in the center
        game.setCellState(8, 7, GameOfLife.ALIVE);
        game.setCellState(8, 8, GameOfLife.ALIVE);
        game.setCellState(9, 6, GameOfLife.ALIVE);
        game.setCellState(9, 9, GameOfLife.ALIVE);
        game.setCellState(10, 7, GameOfLife.ALIVE);
        game.setCellState(10, 8, GameOfLife.ALIVE);
        // the glider om the lower left
        game.setCellState(15, 6, GameOfLife.ALIVE);
        game.setCellState(16, 4, GameOfLife.ALIVE);
        game.setCellState(16, 6, GameOfLife.ALIVE);
        game.setCellState(17, 5, GameOfLife.ALIVE);
        game.setCellState(17, 6, GameOfLife.ALIVE);
        // the blinker in the lower right
        game.setCellState(13, 13, GameOfLife.ALIVE);
        game.setCellState(13, 14, GameOfLife.ALIVE);
        game.setCellState(13, 15, GameOfLife.ALIVE);

        repaint();
    }

    // Overrides these methods in order to implement MouseListener

    /**
     * Invoked when the mouse enters a component.
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * Invoked when the mouse exits a component.
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * Invoked when a mouse button has been pressed on a component.
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {}

    /**
     * Invoked when a mouse button has been released on a component.
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {}

    /**
     * Invoked when the mouse button has been clicked (pressed and released).
     * When the mouse button is clicked, the state of the corresponding cell will toggle.
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // translate x and y coordinate into row and column
        // the formula: divide int coordinate by the size of a cell
        int row = e.getY() / CELL_SIZE;
        int column = e.getX() / CELL_SIZE;

        int curRow = row;
        int curCol = column;
        if (!MyGameOfLife.isCountFromZero()) { // if FIRST_ROW and LAST_ROW == 1, add 1 to curRow and curCol
            curRow++;
            curCol++;
        }
        if(game.getCellState(curRow, curCol) == game.ALIVE) {
            game.setCellState(curRow, curCol, game.DEAD);
        } else {
            game.setCellState(curRow , curCol, game.ALIVE);
        }
        repaint();
    }

    /**
     * Provides the user interface and control for the Game Of Life
     * @param args the command-line argument
     */
    public static void main(String[] args) {
        JFrame window = new JFrame("Game Of Life");
        window.setLocation(50, 50);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        MyGameOfLife board = new MyGameOfLife();
        GameOfLifeBoard displayBoard = new GameOfLifeBoard(board);
        JPanel panel = new JPanel();
        panel.add(displayBoard);
        window.add(panel);
        JPanel toolBar = new JPanel();
        JButton startPoint = new JButton("Start Point");
        // Performs an action on csc143.gol.GameOfLifeBoard, when an action occurred by
        // pressing the "Start Point" button, such as initialize the game
        startPoint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBoard.startGame();
            }
        });
        toolBar.add(startPoint);
        JButton nextGeneration = new JButton("Next Generation");
        nextGeneration.addActionListener(displayBoard);
        toolBar.add(nextGeneration);

        JButton clearBoard = new JButton("Clear Board");
        // Performs an action on csc143.gol.GameOfLifeBoard, when an action occurred by pressing
        // the "Clear Board" button such as set the state of all the cells DEAD
        clearBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayBoard.clearBoard();
            }
        });
        toolBar.add(clearBoard);
        window.add(toolBar, BorderLayout.SOUTH);
        window.pack();
        window.setVisible(true);
    }



}


