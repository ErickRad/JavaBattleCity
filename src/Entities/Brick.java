package Entities;

/**
 * Brick is a block with type 1 and health 1.
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 *
 */
@SuppressWarnings("this-escape")
public class Brick extends Block {
    public Brick(int x, int y) {
        super(x, y);
        loadImage("image/wall_brick.png");
        getImageDimensions();
        setType(1);
        setHealth(1);
    }

}
