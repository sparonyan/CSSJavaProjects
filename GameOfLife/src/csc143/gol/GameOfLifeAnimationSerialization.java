package csc143.gol;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class GameOfLifeAnimationSerialization implements Runnable {
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

    public GameOfLifeAnimationSerialization() {
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
     * Drives Game Of Life Animation.
     * @param args command-line arguments
     */
    public static void main(String[] args) {


        JFrame win = new JFrame("Animated Game Of Life");
        win.setLayout(new BorderLayout());
        win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        win.setLocation(25, 25);
        win.setResizable(false); // disables the window resizing

        GameOfLifeAnimationSerialization ani = new GameOfLifeAnimationSerialization();
        Thread thread = new Thread(ani);

        JPanel spacer = new JPanel();
        spacer.add(ani.view);
        win.add(spacer);

        // mae sure thread is terminated after window closed operation
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

        JPanel toolBar2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton startPoint = new JButton("Start Point");
        // Performs an action on csc143.gol.GameOfLifeBoard, when an action occurred by
        // pressing the "Start Point" button, such as initialize the game
        startPoint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ani.running = false;
                animation.setText("Start Animation");
                ani.view.startGame();
            }
        });
        toolBar2.add(startPoint);

        JButton clearBoard = new JButton("Clear Board");
        // Performs an action on csc143.gol.GameOfLifeBoard, when an action occurred by pressing
        // the "Clear Board" button such as set the state of all the cells DEAD
        clearBoard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ani.running = false;
                animation.setText("Start Animation");
                ani.view.clearBoard();
            }
        });
        toolBar2.add(clearBoard);

        JPanel controls = new JPanel(new GridLayout(2,1));
        controls.add(toolBar1);
        controls.add(toolBar2);
        win.add(controls, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open...");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final JFileChooser fc = new JFileChooser();
                    fc.addChoosableFileFilter(new GolFileFilter(false));
                    fc.setAcceptAllFileFilterUsed(false);
                    int returnVal = fc.showOpenDialog(win);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        ani.model.fileOpen(file);
                        ani.view.update(ani.model, null);
                    }

                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(win,"Error reading from the file"); //TODO
                }
            }
        });
        menu.add(open);

        JMenuItem save = new JMenuItem("Save...");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    final JFileChooser fc = new JFileChooser();
                    //fc.addChoosableFileFilter(new GolFileFilter(true));
                    fc.setFileFilter(new GolFileFilter(true));
                    fc.setAcceptAllFileFilterUsed(false);
                    int returnVal = fc.showSaveDialog(win);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        if (!file.getName().endsWith(".gol")) {
                            return;
                        }
                        if (file.exists()) {
                            int confirm  = JOptionPane.showConfirmDialog(win,"Overwrite file: " + file.getName(),
                                    "Confirm File Overwrite", JOptionPane.YES_NO_OPTION);
                            if (confirm != JOptionPane.YES_OPTION) {
                                return;
                            }
                        }
                        ani.model.fileSave(file);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(win,"Error to writing to file"); //TODO
                }
            }
        });
        menu.add(save);

        menu.addSeparator();
        JMenuItem clear = new JMenuItem("Clear Board");
        menu.add(clear);
        JMenuItem exitGame = new JMenuItem("Exit Game ofLife");
        menu.add(exitGame);
        menuBar.add(menu);
        win.setJMenuBar(menuBar);

        win.pack();
        thread.start();
        win.setVisible(true);
    }

    static class GolFileFilter extends FileFilter {
        private boolean acceptDirectory;
        public GolFileFilter(boolean acceptDirectory) {
            this.acceptDirectory = acceptDirectory;
        }

        @Override
        public boolean accept(File f) {
            if (acceptDirectory && f.isDirectory()) {
                return true;
            }
            if (f.getName().endsWith(".gol")) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String getDescription() {
            return "Game of Life files(.gol)";
        }
    }

}


