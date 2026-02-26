package GameMain;

import Entities.Animation;
import Entities.Base;
import Entities.Block;
import Entities.Bullet;
import Entities.ExplodingTank;
import Entities.Explosion;
import Entities.Tank;
import Entities.TankAI;
import Entities.TankShield;
import java.awt.Rectangle;
import java.util.ArrayList;

public class CollisionUtility {
    private static ArrayList<Block> blocks;
    private static ArrayList<Animation> explosions;

    private static int[] enemyTankNum = {0, 0, 0, 0};
    public static int powerUpX = 0;
    public static int powerUpY = 0;

    static public void loadCollisionUtility(ArrayList<Block> inblocks,
                                            ArrayList<Animation> inexplosion) {
        blocks = inblocks;
        explosions = inexplosion;
    }

    public static void resetScore() {
        enemyTankNum = new int[]{0, 0, 0, 0};
    }

    public static void CollisionBulletsBlocksHelper(Bullet bullet, Block block) {
        BlockType type = BlockType.getTypeFromInt(block.getType());
        if (type.equals(BlockType.BRICK)) {
            block.lowerHealth();
            bullet.setVisible(false);
        }
        if (type.equals(BlockType.STEEL) && bullet.getUpgrade()) {
            block.lowerHealth();
            bullet.setVisible(false);
        }
        if (type.equals(BlockType.STEEL) && bullet.getUpgrade() == false) {
            bullet.setVisible(false);
        }
        if (type.equals(BlockType.BASE) && block.health != 0) {
            bullet.setVisible(false);
            Base b = (Base) block;
            b.gameOver = true;
            explosions.add(new ExplodingTank(block.x, block.y));
            SoundUtility.explosion2();
            Board.setEndGame();
            SoundUtility.gameOver();

        }
        if (block.getHealth() == 0) {
            SoundUtility.explosion2();
            block.vis = false;
            explosions.add(new Explosion(block.x, block.y));

        }
        if (block.getHealth() == 0) {
            block.vis = false;

        }
    }

    public static boolean checkCollisionTankBlocks(Rectangle r3) {
        for (Block block : blocks) {
            Rectangle r2 = block.getBounds();
            BlockType type = BlockType.getTypeFromInt(block.getType());

            if (type.equals(BlockType.TREE)) {

            } else if (r3.intersects(r2)) {
                return true;
            
            }

        }

        return false;
    }

    public static void checkCollisionBulletsBlocks(ArrayList<Bullet> bullets,
                                                   ArrayList<Block> blocks) {

        for (int x = 0; x < bullets.size(); x++) {
            Bullet b = bullets.get(x);
            Rectangle r1 = b.getBounds();

            for (int i = 0; i < blocks.size(); i++) {
                Block aBlock = blocks.get(i);
                Rectangle r2 = aBlock.getBounds();

                if (r1.intersects(r2)) {
                    SoundUtility.BulletHitBrick();
                    CollisionBulletsBlocksHelper(b, aBlock);
                }
            }
        }
    }

    public static void checkCollisionBulletsTank(ArrayList<Bullet> bullets,
                                                 Tank tank) {
        Rectangle r2 = tank.getBounds();
        for (int x = 0; x < bullets.size(); x++) {
            Bullet b = bullets.get(x);
            Rectangle r1 = b.getBounds();
            if (r1.intersects(r2) && b.isEnemy == true) {
                b.vis = false;
                if (tank.shield == false) {
                    SoundUtility.explosion1();
                    explosions.add(new ExplodingTank(tank.x, tank.y));
                    tank.downHealth();
                    resetTankPosition(tank, 1);
                } else {
                    SoundUtility.BulletHitTank();
                }
            }
        }
    }

    public static void checkCollisionBulletsTankAI(ArrayList<Bullet> bullets,
                                                   ArrayList<TankAI> TankAIs) {
        for (int x = 0; x < bullets.size(); x++) {
            Bullet b = bullets.get(x);
            Rectangle r1 = b.getBounds();

            for (int i = 0; i < TankAIs.size(); i++) {
                TankAI tankAI = TankAIs.get(i);
                Rectangle r2 = tankAI.getBounds();

                if (r1.intersects(r2) && b.isEnemy == false) {
                    tankAI.decreaseHP();
                    b.vis = false;
                    SoundUtility.BulletHitTank();
                    if (tankAI.getHealth() < 1) {
                        incrementNum(tankAI);
                        if (tankAI.hasPowerUp()) {
                            powerUpX = tankAI.getX();
                            powerUpY = tankAI.getY();
                        }
                        tankAI.vis = false;
                        Board.decrementEnemies(1);
                        explosions.add(new ExplodingTank(tankAI.x, tankAI.y));
                        SoundUtility.explosion1();
                    }
                }
            }
        }
    }

    public static void incrementNum(TankAI tankAI) {
        String type = tankAI.getType();
        switch (type) {
            case "basic":
                enemyTankNum[0] += 1;
                break;
            case "fast":
                enemyTankNum[1] += 1;
                break;
            case "power":
                enemyTankNum[2] += 1;
                break;
            case "armor":
                enemyTankNum[3] += 1;
                break;
            default:
                break;
        }
    }

    public static int[] getEnemyTankNum() {
        return enemyTankNum;
    }

    public static void resetTankPosition(Tank atank, int type) {
        atank.x = 10 * 16;
        atank.y = (Map.level0.length - 3) * 16;
        atank.shield = true;
        explosions.add(new TankShield(atank, 2));
        if (type == 1) {
            atank.starLevel = 0;
        } else {
            atank.shield = false;
        }

    }

    /**
     * Check collision between the player and enemy tanks
     *
     * @param TankAIs array list for Tank AIs
     * @param atank the player tank
     */
    public static void checkCollisionTankTankAI(ArrayList<TankAI> TankAIs,
                                                Tank atank) {
        Rectangle r1 = atank.getBounds();
        for (int i = 0; i < TankAIs.size(); i++) {
            TankAI tankAI = TankAIs.get(i);
            Rectangle r2 = tankAI.getBounds();
            if (r1.intersects(r2)) {
                if (atank.shield == false) {
                    explosions.add(new ExplodingTank(atank.x, atank.y));
                    atank.downHealth();
                    resetTankPosition(atank, 1);
                } else if (atank.shield == true) {
                    incrementNum(tankAI);
                    Board.decrementEnemies(1);
                    tankAI.vis = false;
                    explosions.add(new ExplodingTank(atank.x, atank.y));
                }
            }
        }
    }
}
