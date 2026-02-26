/**
 * The Base class is a block which represents the player's base. It has an
 * updateAnimation method inorder to show the base when it is destroyed.
 *
 */
package Entities;

@SuppressWarnings("this-escape")
public class Base extends Block {
    public boolean gameOver = false;

    public Base(int x, int y) {
        super(x, y);
        loadImage("images/base.png");
        getImageDimensions();
        setHealth(1);
        setType(3);

    }

    public void updateAnimation() {
        if (gameOver == true) {
            loadImage("images/base_destroyed.png");
            getImageDimensions();

        }
    }

}
