package Entities.PowerUps;

/**
 * BombPowerUp extends PowerUp
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 */
@SuppressWarnings("this-escape")
public class BombPowerUp extends PowerUp {
    public BombPowerUp(int x, int y) {
        super(x, y);
        loadImage("image/powerup_grenade.png");
        getImageDimensions();
        setType(9);
        s = "image/powerup_grenade.png";
    }

}
