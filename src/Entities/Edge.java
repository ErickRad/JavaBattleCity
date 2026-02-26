package Entities;

/**
 *
 * @param int x represents the starting x location in pixels
 * @param int y represents the starting y location in pixels
 * 
 */
@SuppressWarnings("this-escape")
public class Edge extends Block {
    public Edge(int x, int y) {
        super(x, y);
        loadImage("images/edge.png");
        getImageDimensions();
        setType(6);
        setHealth(1);
    }
}
