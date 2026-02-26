package GameMain;

import static GameMain.CollisionUtility.loadCollisionUtility;
import static GameMain.CollisionUtility.resetTankPosition;
import static GameMain.Menu.loadFont;
import Entities.Animation;
import Entities.Base;
import Entities.Block;
import Entities.Brick;
import Entities.Bullet;
import Entities.Edge;
import Entities.PowerUps.PowerUp;
import Entities.River;
import Entities.Steel;
import Entities.Tank;
import Entities.TankAI;
import Entities.Tree;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    private Timer timer;
    private transient Tank tank;

    private ArrayList<TankAI> enemy = new ArrayList<>();
    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Animation> animations = new ArrayList<>();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private transient final ImageUtility imageInstance = ImageUtility.getInstance();

    private final int INIT_PLAYER_X = 10 * 16;
    private final int INIT_PLAYER_Y = (Map.level0.length - 3) * 16;
    private final int B_WIDTH = Map.BOARD_WIDTH;
    private final int B_HEIGHT = Map.BOARD_HEIGHT;
    private final int DELAY = 15;
    private final int initX = 29;
    private boolean pause = false;
    public static boolean gameOver = false;
    private int yPos = Map.BOARD_HEIGHT;
    private int direction = -1;
    private final int stopYPos = 250;
    private GameView theView;
    private static int stage = 1;
    private int numAI;
    private static final int goal = 10;
    public static int numEnemies = goal;

    /**
     * Constructor for the Board class
     *
     * @param theView GameView that represents the frame of the game
     */
    @SuppressWarnings("this-escape")
    public Board(GameView theView) {
        this.theView = theView;
        timer = new Timer(DELAY, this);
        timer.start();
        initBoard();
    }

    private void initBoard() {
        stage = 1;
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        numAI = 0;
        tank = new Tank(INIT_PLAYER_X, INIT_PLAYER_Y, 4);

        initBlocks();
        CollisionUtility.loadCollisionUtility(blocks, animations);
        BoardUtility.loadBoardUtility(enemy, blocks, animations, powerUps, tank);
    }

    public void initBlocks() {

        int[][] map = Map.getMap(stage);
        SoundUtility.startStage();
        int type;

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {

                type = map[x][y];
                BlockType bType = BlockType.getTypeFromInt(type);
                switch (bType) {
                    case BRICK:
                        blocks.add(new Brick(y * 16, x * 16));
                        break;
                    case STEEL:
                        blocks.add(new Steel(y * 16, x * 16));
                        break;
                    case BASE:
                        blocks.add(new Base(y * 16, x * 16));
                        break;
                    case RIVER:
                        blocks.add(new River(y * 16, x * 16));
                        break;
                    case TREE:
                        blocks.add(new Tree(y * 16, x * 16));
                        break;
                    case EDGE:
                        blocks.add(new Edge(y * 16, x * 16));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawObjects(g);
        drawEdge(g);
        endGame(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void checkGameOver() {
        if (tank.getHealth() < 0) {
            setEndGame();
        }
    }

    private void drawObjects(Graphics g) {
        for (TankAI tankAI : enemy) {
            if (tankAI.isVisible()) {
                g.drawImage(tankAI.getImage(), tankAI.getX(), tankAI.getY(),
                            this);
            }
        }
        if (tank.isVisible()) {
            g.drawImage(tank.getImage(), tank.getX(), tank.getY(), this);
        }
        ArrayList<Bullet> bullets = new ArrayList<>();
        bullets.addAll(tank.getBullets());
        for (TankAI tankAI : enemy) {
            bullets.addAll(tankAI.getBullets());
        }

        for (Bullet b : bullets) {
            if (b.isVisible()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
        for (Block a : blocks) {
            if (a.isVisible()) {
                g.drawImage(a.getImage(), a.getX(), a.getY(), this);
            }
        }
        for (Animation e : animations) {
            if (e.isVisible()) {
                g.drawImage(e.getImage(), e.getX(), e.getY(), this);
            }
        }
        for (PowerUp p : powerUps) {
            if (p.isVisible()) {
                g.drawImage(p.getImage(), p.getX(), p.getY(), this);
            }
        }
    }

    private void drawEdge(Graphics g) {

        drawEnemies(g, numEnemies);

        String ipText = "IP";
        int lives = tank.getHealth();
        Font font = loadFont();
        g.setFont(font);
        g.drawString(ipText, initX * 16, 17 * 16);

        Image liveIcon = imageInstance.getLives();
        g.drawImage(liveIcon, initX * 16, 17 * 16, this);
        g.drawString(String.valueOf(lives < 0 ? 0 : lives), (initX + 1) * 16,
                     18 * 16);

        Image flagIcon = imageInstance.getFlagIcon();
        g.drawImage(flagIcon, initX * 16, 22 * 16, this);
        g.drawString(String.valueOf(stage), (initX + 1) * 16, 25 * 16);
    }

    private void drawEnemies(Graphics g, int numEnemies) {
        Image enemyIcon = imageInstance.getEnemyIcon();
        int count = 1;
        int initY = 4;
        for (int i = 0; i < numEnemies; i++) {
            switch (count) {
                case 0:
                    count = 1;
                    g.drawImage(enemyIcon, (initX + 1) * 16, initY * 16, this);
                    initY++;
                    break;
                case 1:
                    count--;
                    g.drawImage(enemyIcon, initX * 16, initY * 16, this);
                    break;
                default:
                    break;
            }
        }
    }

    
    public static void decrementEnemies(int num) {
        numEnemies -= num;
    }

    public void updateSprites() {
        spawnTankAI();
        spawnPowerUp();
        updateTank();
        updateTankAI();
        updateBullets();
        updateBlocks();
        updateAnimations();
        updateBlocks();
        updatePowerUps();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (Menu.getMenuStatus() == false && pause == true) {
            return;
        }
        if (gameOver) {
            timer.stop();
            return;
        }
        updateSprites();
        checkCollisions();
        checkGameOver();

        nextLevel();
        repaint();
    }

    public void nextLevel() {
        if (enemy.isEmpty()) {
            if (stage == 35) {
                System.out.println("You win!");
                loadScoreBoard(theView);
            } else {
                stage += 1;
                numAI = 0;
                numEnemies = goal;
                clearBoard();

                initBlocks();
                CollisionUtility.loadCollisionUtility(blocks, animations);
                BoardUtility.loadBoardUtility(enemy, blocks, animations,
                                              powerUps,
                                              tank);
            }
        }
    }


    public void updateAnimations() {
        BoardUtility.updateAnimations();
    }

    private void updateBlocks() {
        BoardUtility.updateBlocks();
    }

    private void updateTank() {
        BoardUtility.updateTank();
    }

    private void updateTankAI() {
        for (TankAI tankAI : enemy) {
            if (tankAI.isVisible()) {
                if (System.currentTimeMillis() - tankAI.frozenStartTime > 5000 && tankAI.frozen) {
                    tankAI.frozen = false;
                }
                if ("easy".equals(tankAI.getDifficulty())) {
                    tankAI.actionEasy();
                } else if ("normal".equals(tankAI.getDifficulty())) {
                    tankAI.actionNormal(this.tank);
                } else if ("hard".equals(tankAI.getDifficulty())) {
                    tankAI.actionHard(this.tank);
                }
            }
        }
        for (int i = 0; i < enemy.size(); i++) {
            if (enemy.get(i).vis == false) {
                enemy.remove(i);
            }
        }
    }

    private void spawnTankAI() {
        while (numAI < goal) {
            if (enemy.size() < 4) {
                boolean powerUp;
                powerUp = (numAI % 4 == 1);

                if (numAI < 6) {
                    BoardUtility.spawnTankAI("easy", powerUp);

                } else if (numAI < 8) {
                    BoardUtility.spawnTankAI("normal", powerUp);

                } else {
                    BoardUtility.spawnTankAI("hard", powerUp);
                }

                numAI++;

            } else {
                return;
            }
        }
    }
    private void updatePowerUps() {
        BoardUtility.updatePowerUps();
    }

    private void spawnPowerUp() {
        BoardUtility.spawnPowerUp();
    }

    private void updateBulletsTank() {
        BoardUtility.updateBulletsTank();

    }

    private void updateBulletsTankAI() {
        BoardUtility.updateBulletsTankAI();
    }

    private void updateBullets() {
        updateBulletsTank();
        updateBulletsTankAI();
    }

    public void checkCollisions() {
        BoardUtility.checkCollisions();
    }

    /**
     * Create end game information on the screen. After the "GAME OVER" label
     * shows on the screen, a score board of the entire game will be displayed
     *
     * @param g Graphics
     */
    public void endGame(Graphics g) {
        if (gameOver) {
            Timer gameOverTimer = new Timer(80, new ActionListener() {
                                        @Override
                                        public void actionPerformed(
                                                ActionEvent e) {
                                            yPos += direction;
                                            if (yPos == stopYPos) {
                                                direction = 0;
                                            } else if (yPos > getHeight()) {
                                                yPos = getHeight();
                                            } else if (yPos < 0) {
                                                yPos = 0;
                                                direction *= -1;
                                            }
                                            repaint();
                                        }
                                    });
            gameOverTimer.setRepeats(true);
            gameOverTimer.setCoalesce(true);
            gameOverTimer.start();
            Font font = loadFont();
            g.setFont(font);
            g.setColor(Color.RED);
            g.drawString("GAME OVER", Map.BOARD_WIDTH / 2 - 85, yPos);

            if (yPos == stopYPos) {
                gameOverTimer.stop();
                Timer sorceBoardTimer = new Timer(3000, new ActionListener() {
                                              @Override
                                              public void actionPerformed(
                                                      ActionEvent e) {
                                                  loadScoreBoard(theView);
                                              }
                                          });
                sorceBoardTimer.setRepeats(false);
                sorceBoardTimer.start();
            }
        }
    }

    /**
     * Load the score board to the game board
     *
     * @param theView GameView that represents the frame of the game
     */
    public void loadScoreBoard(GameView theView) {
        theView.getGamePanel().removeAll();

        ScoreBoard scoreBoard = new ScoreBoard(theView);

        scoreBoard.loadScore();
        scoreBoard.setBackground(Color.BLACK);
        theView.getGamePanel().add(scoreBoard);
        scoreBoard.requestFocusInWindow();
        SoundUtility.statistics();
        theView.setVisible(true);
    }

    public static void setEndGame() {
        SoundUtility.gameOver();
        gameOver = true;
    }

    public static void restartGame() {
        gameOver = false;
    }

    public void clearBoard() {

        animations = new ArrayList<>();
        blocks = new ArrayList<>();
        powerUps = new ArrayList<>();

        updateSprites();
        resetTankPosition(tank, 2);
        loadCollisionUtility(blocks, animations);

    }

    public static int getStage() {
        return stage;
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            tank.keyReleased(e);

        }

        @Override
        public void keyPressed(KeyEvent e) {
            tank.keyPressed(e);
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!pause) {
                    SoundUtility.pause();
                }
                pause = !pause;

            }
        }
    }
}
