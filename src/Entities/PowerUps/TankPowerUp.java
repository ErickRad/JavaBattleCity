package Entities.PowerUps;

/**
 * TankPowerUp extends the PowerUp and sets the type as 7
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * 
 */
@SuppressWarnings("this-escape")
public class TankPowerUp extends PowerUp {
    public TankPowerUp(int x, int y) {
        super(x, y);
        loadImage("images/powerup_tank.png");
        getImageDimensions();
        setType(7);
        s = "images/powerup_tank.png";
    }

}
