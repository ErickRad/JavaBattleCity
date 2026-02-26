package Entities.PowerUps;

/**
 * ShovelPowerUp extends PowerUp and sets the type at 11
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * 
 */
@SuppressWarnings("this-escape")
public class ShovelPowerUp extends PowerUp {
    public ShovelPowerUp(int x, int y) {
        super(x, y);
        loadImage("images/powerup_shovel.png");
        getImageDimensions();
        setType(11);
        s = "images/powerup_shovel.png";
    }

}
