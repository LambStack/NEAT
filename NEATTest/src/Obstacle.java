/**
 * Created by jagnelli on 11/20/2018.
 */
public class Obstacle extends Drawable  {

    public Obstacle(double xPos, double yPos, double height, double width){
        super(xPos, yPos, height, width);
    }

    @Override
    public int compareTo(Object o) {
        Drawable drawableO = (Drawable)o;

        return getX() - drawableO.getX();
    }
}
