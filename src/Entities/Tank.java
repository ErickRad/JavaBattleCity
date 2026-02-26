package Entities;

import GameMain.CollisionUtility;
import GameMain.Map;
import GameMain.SoundUtility;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * Tank extends Sprite The Tank represents the player in the game. The tank has
 * an array of bullets and is capable of moving and firing bullets depending on
 * key events (arrow keys for movement and space bar for firing bullets) in four
 * different directions. The tank also can be upgraded in several ways
 * increasing the firing speed movement speed and the ability to break steel
 * blocks
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * @param int lives is the lives which the tank posses
 *
 * 
 */
@SuppressWarnings("this-escape")
public class Tank extends Sprite {

    private final int BOARD_WIDTH = Map.BOARD_WIDTH;
    private final int BOARD_HEIGHT = Map.BOARD_HEIGHT;
    private int dx;
    private int dy;
    private ArrayList<Bullet> bullets;
    public int direction;
    private long lastFired = 0;
    private int health = 3;
    public int starLevel = 0;
    public int lives;
    public boolean shield = false;
    private java.util.List<Integer> keyStack = new java.util.ArrayList<>();

    public void upLives() {
        this.lives += 1;
    }

    public int getHealth() {
        return health;
    }

    public void upStarLevel() {
        starLevel += 1;
    }

    public int getLives() {
        return this.lives;
    }

    public void downHealth() {
        if (shield == false) {
            this.health -= 1;
        }
    }

    public Tank(int x, int y, int lives) {
        super(x, y);
        loadImage("image/playerTank_up.png");
        getImageDimensions();
        bullets = new ArrayList<>();
        direction = 0;
        this.lives = lives;
    }

    public void move() {

        Rectangle theTank = new Rectangle(x + dx, y + dy, width, height);

        if (CollisionUtility.checkCollisionTankBlocks(theTank) == false) {
            x += dx;
            y += dy;
        }

        if (x > BOARD_WIDTH - width) {
            x = BOARD_WIDTH - width;
        }

        if (y > BOARD_HEIGHT - height) {
            y = BOARD_HEIGHT - height;
        }
        if (x < 1) {
            x = 1;
        }

        if (y < 1) {
            y = 1;
        }
    }

    public ArrayList<Bullet> getBullets() {

        return bullets;
    }

    public void fire() {
        Bullet aBullet;
        if (direction == 0) {
            aBullet = new Bullet(x + width / 3, y, 0, false);
        } else if (direction == 1) {
            aBullet = new Bullet(x + width - 3, y + height / 3, 1, false);
        } else if (direction == 2) {
            aBullet = new Bullet(x + width / 3, (y + height) - 3, 2, false);
        } else {
            aBullet = new Bullet(x, y + height / 3, 3, false);
        }
        if (starLevel == 3) {
            aBullet.upgrade();
        }
        bullets.add(aBullet);
        SoundUtility.fireSound();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public void keyPressed(KeyEvent e) {
        int time;
        int key = e.getKeyCode();
        if (starLevel == 0) {
            time = 300;
        } else {
            time = 200;
        }
        if (key == KeyEvent.VK_SPACE && (System.currentTimeMillis() - lastFired) > time) {
            fire();
            lastFired = System.currentTimeMillis();
        }
        // for arrow keys we use a stack so releasing one key resumes the most recently pressed
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            pushKey(key);
            applyMovementFromStack();
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            releaseKey(key);
            applyMovementFromStack();
        }
    }

    private void pushKey(int key) {
        keyStack.remove(Integer.valueOf(key));
        keyStack.add(0, key);
    }

    private void releaseKey(int key) {
        keyStack.remove(Integer.valueOf(key));
    }

    private void applyMovementFromStack() {
        dx = 0;
        dy = 0;
        if (keyStack.isEmpty()) return;
        int key = keyStack.get(0);
        if (key == KeyEvent.VK_LEFT) {
            dx = -2;
            dy = 0;
            if (starLevel > 1) dx = -2;
            ImageIcon ii = new ImageIcon("image/playerTank_left.png");
            image = ii.getImage();
            direction = 3;
        } else if (key == KeyEvent.VK_RIGHT) {
            dx = 2;
            dy = 0;
            if (starLevel > 1) dx = 2;
            ImageIcon ii = new ImageIcon("image/playerTank_right.png");
            image = ii.getImage();
            direction = 1;
        } else if (key == KeyEvent.VK_UP) {
            dy = -2;
            dx = 0;
            if (starLevel > 1) dy = -2;
            ImageIcon ii = new ImageIcon("image/playerTank_up.png");
            image = ii.getImage();
            direction = 0;
        } else if (key == KeyEvent.VK_DOWN) {
            dy = 2;
            dx = 0;
            if (starLevel > 1) dy = 2;
            ImageIcon ii = new ImageIcon("image/playerTank_down.png");
            image = ii.getImage();
            direction = 2;
        }
    }

    public void upHealth() {
        this.health += 1;
    }

    public int getStarLevel() {
        return starLevel;
    }
}
