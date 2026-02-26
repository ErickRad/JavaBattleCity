package Entities.PowerUps;

/**
 * ClockPowerUp extends PowerUp and sets the type as 10
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 */
@SuppressWarnings("this-escape")
public class ClockPowerUp extends PowerUp {
    public ClockPowerUp(int x, int y) {
        super(x, y);
        loadImage("images/powerup_timer.png");
        getImageDimensions();
        setType(10);
        s = "images/powerup_timer.png";
    }

}
