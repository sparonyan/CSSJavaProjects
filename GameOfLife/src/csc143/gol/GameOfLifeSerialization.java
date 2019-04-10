package csc143.gol;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

/**
 * Programming Assignment 7: Game of Life, Serialization and Final Submission
 * <p>
 * Represents the user interface of Game Of Life. This class supports the use of threads for
 * Game Of Life animation and has ability of saving and recalling the state of this object.
 *
 * @author Satine Paronyan
 * @version Standard Level
 */
public class GameOfLifeSerialization implements Runnable {
    // the model of Game Of Life
    private MyGameOfLife model;

    // the view of Game Of Life
    private GameOfLifeBoard view;

    // animated speed
    private int gensPerMin;

    // thread sleep time
    private int sleep;

    // indicates the state of the thread, which is running or not
    private boolean running;

    private boolean isExit;

    /**
     * Constructs and initializes this object
     */
    public GameOfLifeSerialization() {
        // set-up the game of life
        model = new MyGameOfLife();
        view = new GameOfLifeBoard(model);
        model.addObserver(view);

        // set-up for the animation
        gensPerMin = 120;
        sleep = 500;
        running = false;

        // indicates the window is open or closed,
        // if closed the thread should stop.
        isExit = false;
    }

    /**
     * Sets the sleep time in milliseconds for the current thread.
     * This method called in the controls' action listeners after the
     * genPerMin changed its value.
     */
    private void setSleep() {
        // calculate  the sleep time in milliseconds for the animated speed
        sleep = 60000 / gensPerMin;
    }

    /**
     * Started thread causes the run method to be called in that separately executing thread.
     */
    @Override
    public void run() {
        while (!isExit) {
            try {
                // make the current thread pause (sleep) so
                // we can see the movement
                Thread.sleep(sleep);
            } catch(InterruptedException e) {

            }
            if (running) {
                model.nextGeneration();
                //view.repaint();
                view.update(model, null);
            }
        }
    }

    /**
     * Drives Game Of Life Animation and Serialization.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        JFrame win = new JFrame("Animated Game Of Life");
        win.setLayout(new BorderLayout());
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setLocation(25, 25);
        win.setResizable(false); // disables the window resizing

        GameOfLifeSerialization ani = new GameOfLifeSerialization();
        Thread thread = new Thread(ani);

        JPanel spacer = new JPanel();
        spacer.add(ani.view);
        win.add(spacer);

        // make sure thread is terminated after window closed operation
        win.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                ani.isExit = true;
                thread.interrupt();

            }
        });

        JPanel toolBar1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        GridBagConstraints c = new GridBagConstraints();
        JButton animation = new JButton("Start Animation");
        animation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ani.running) {
                    ani.running = false;
                    animation.setText("Start Animation");
                } else {
                    ani.running = true;
                    animation.setText("Stop Animation");
                }
            }
        });
        toolBar1.add(animation);

        JLabel textName = new JLabel("Generations per minute");
        JTextField textField = new JTextField(3);
        textField.setText("" + ani.gensPerMin);
        Font f = new Font("Helvetica", Font.PLAIN, 18);
        textField.setFont(f);
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                int val = -1; // not acceptable value;
                try {
                    val = Integer.parseInt(text);
                } catch (NumberFormatException ex) {
                    // val value stays not acceptable
                }
                if (val >= 60 && val <= 500) {
                    ani.gensPerMin = val;
                    ani.setSleep();
                } else {
                    // show message dialog if the input is not an integer or invalid range
                    JOptionPane.showMessageDialog(win,
                            "Bad input value",
                            "error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        toolBar1.add(textName);
        toolBar1.add(textField);

        Toolkit toolkit = Toolkit.getDefaultToolkit();

        JButton plus = new JButton("+");
        plus.setFont(f);
        plus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ani.gensPerMin + 20 <= 500) {
                    ani.gensPerMin += 20;
                    ani.setSleep();
                    textField.setText("" + ani.gensPerMin);
                } else {
                    toolkit.beep();
                }
            }
        });
        toolBar1.add(plus);

        JButton minus = new JButton("-");
        minus.setFont(f);
        minus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ani.gensPerMin - 20 >= 60) {
                    ani.gensPerMin -= 20;
                    ani.setSleep();
                    textField.setText("" + ani.gensPerMin);
                } else {
                    toolkit.beep();
                }
            }
        });
        toolBar1.add(minus);
        win.add(toolBar1, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open...");
        open.addActionListener(new ActionListener() {
            // Opens the file specified by user to read the object's state
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.addChoosableFileFilter(new GolFileFilter());
                fc.setAcceptAllFileFilterUsed(false);
                // returned integer that indicates whether the user selected a file
                int returnVal = fc.showOpenDialog(win);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    try {
                        ani.model.fileOpen(file);
                        ani.view.update(ani.model, null);
                    } catch (IOException exc) {
                        JOptionPane.showMessageDialog(win,"Error reading from the file: " + file.getName());
                    }
                }
            }
        });
        menu.add(open);

        JMenuItem save = new JMenuItem("Save...");
        save.addActionListener(new ActionListener() {
            // Saves the state of this object to the file specified by a user.
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new GolFileFilter());
                fc.setAcceptAllFileFilterUsed(false);
                int returnVal = fc.showSaveDialog(win);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if (!file.getName().endsWith(".gol")) {
                        // adds .gol type to the specified not .gol file
                        file = new File(file.getAbsolutePath() + ".gol");
                    }
                    // verify if the existing file needs to be overridden
                    if (file.exists()) {
                        int confirm  = JOptionPane.showConfirmDialog(win,"Overwrite file: " + file.getName(),
                                    "Confirm File Overwrite", JOptionPane.YES_NO_OPTION);
                        if (confirm != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                    try {
                        ani.model.fileSave(file);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(win,"Error writing to file: " + file.getName());
                    }
                }
            }
        });
        menu.add(save);

        menu.addSeparator();
        JMenuItem clear = new JMenuItem("Clear Board");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ani.running = false;
                animation.setText("Start Animation");
                ani.view.clearBoard();
            }
        });
        menu.add(clear);
        JMenuItem exitGame = new JMenuItem("Exit Game ofLife");
        exitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(exitGame);
        menuBar.add(menu);
        win.setJMenuBar(menuBar);

        win.pack();
        thread.start();
        win.setVisible(true);
    }

    /**
     * Filters files by the type .gol
     */
    static class GolFileFilter extends FileFilter {

        /**
         * Tests whether or not the specified file should be included.
         * @param f the given file to be tested
         * @return true if and only if pathname should be included
         */
        @Override
        public boolean accept(File f) {
            if (f.getName().endsWith(".gol")) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Returns a string for the file format to be saved in
         * @return the string for the file format
         */
        @Override
        public String getDescription() {
            return "Game of Life files(.gol)";
        }
    }

}


