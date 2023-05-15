import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;


public class SnakeGame extends JPanel implements ActionListener {
    private final int BOARD_WIDTH = 600;
    private final int BOARD_HEIGHT = 600;
    private final int UNIT_SIZE = 20;
    private final int GAME_UNITS = (BOARD_WIDTH * BOARD_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int DELAY = 100;
    private final int[] x = new int[GAME_UNITS];
    private final int[] y = new int[GAME_UNITS];
    private int bodyParts = 6;
    private int applesEaten = 0;
    private int appleX;
    private int appleY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (BOARD_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = (int) (Math.random() * (BOARD_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = (int) (Math.random() * (BOARD_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
            case 'L':
                x[0] -= UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // check if head collides
    // with body
    for (int i = bodyParts; i > 0; i--) {
        if ((x[0] == x[i]) && (y[0] == y[i])) {
            running = false;
        }
    }

    // check if head touches left border
    if (x[0] < 0) {
        running = false;
    }

    // check if head touches right border
    if (x[0] > BOARD_WIDTH) {
        running = false;
    }

    // check if head touches top border
    if (y[0] < 0) {
        running = false;
    }

    // check if head touches bottom border
    if (y[0] > BOARD_HEIGHT) {
        running = false;
    }

    if (!running) {
        timer.stop();
    }
}

public void gameOver(Graphics g) {
    g.setColor(Color.white);
    g.setFont(new Font("Ink Free", Font.BOLD, 75));
    FontMetrics metrics1 = getFontMetrics(g.getFont());
    g.drawString("Game Over", (BOARD_WIDTH - metrics1.stringWidth("Game Over")) / 2, BOARD_HEIGHT / 2);

    g.setFont(new Font("Ink Free", Font.BOLD, 40));
    FontMetrics metrics2 = getFontMetrics(g.getFont());
    g.drawString("Score: " + applesEaten, (BOARD_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
}

public void actionPerformed(ActionEvent e) {
    if (running) {
        move();
        checkApple();
        checkCollisions();
    }

    repaint();
}

public class MyKeyAdapter extends KeyAdapter {
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
        }
    }
}

public static void main(String[] args) {
    JFrame frame = new JFrame("Snake");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new SnakeGame());
    frame.pack();
    frame.setVisible(true);
}
}
