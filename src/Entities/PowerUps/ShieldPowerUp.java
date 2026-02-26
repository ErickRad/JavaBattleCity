package Entities.PowerUps;

/**
 * ShieldPowerUp extends PowerUp and sets the type at 12
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * 
 */
@SuppressWarnings("this-escape")
public class ShieldPowerUp extends PowerUp {
    public ShieldPowerUp(int x, int y) {
        super(x, y);
        loadImage("images/powerup_helmet.png");
        getImageDimensions();
        setType(12);
        s = "images/powerup_helmet.png";
    }

}
