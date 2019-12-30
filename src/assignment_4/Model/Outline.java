package assignment_4.Model;

/**
 * This class encapsulates outline information of a handwriting digit.
 */
public class Outline {
    private int x;
    private int y;
    private int length;

    private Outline() {
    }

    public Outline(int x, int y, int length) {
        this.x = x;
        this.y = y;
        this.length = length;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLength() {
        return this.length;
    }
}
