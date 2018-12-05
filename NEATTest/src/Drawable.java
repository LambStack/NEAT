import java.util.Comparator;

/**
 * Created by jagnelli on 11/20/2018.
 */
public abstract class Drawable implements Comparable{
    static int HEIGHT = 50;
    private double xLoc, yLoc, height, width;

    public Drawable(double xPos, double yPos, double height, double width){
        this.xLoc = xPos;
        this.yLoc = yPos;
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return (int)height;
    }

    public int getWidth() {
        return (int)width;
    }

    public int getX() {
        return (int)xLoc;
    }

    public int getY() {
        return (int)yLoc;
    }

    public void moveLeft(){
        xLoc -= 5;
    }

    public void setY(double newY){
        yLoc = newY;
    }
}
