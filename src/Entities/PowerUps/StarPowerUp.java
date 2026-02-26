package Entities.PowerUps;

/**
 * StarPowerUp extends PowerUp and sets the type as 8
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * 
 */
@SuppressWarnings("this-escape")
public class StarPowerUp extends PowerUp {
    public StarPowerUp(int x, int y) {
        super(x, y);
        loadImage("images/powerup_star.png");
        getImageDimensions();
        setType(8);
        s = "images/powerup_star.png";
    }

}
