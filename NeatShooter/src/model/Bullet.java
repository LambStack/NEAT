package model;

import java.awt.*;
import java.util.HashMap;

public class Bullet extends Rectangle {
    double slope, yInt, direction = 1;
    int ownerID = -1;
    private static final int WIDTH = 15, HEIGHT = 15;
    private double deltaX;
    private static final int SPEED = 50; // closer to 0 is faster, I also think this is logarithmic


    public Bullet(double xEnd, double yEnd, double xStart, double yStart, int ownerID){
        super((int)xStart, (int)yStart, WIDTH, HEIGHT);
        this.ownerID = ownerID;
        slope = (yEnd - yStart) / (xEnd - xStart);

        yInt =  xEnd * -1 * slope;

        // tuff maffs
        //deltaX = (Math.sqrt(( yInt * yInt) + (2*slope*slope) + 2) - yInt * slope) / ((slope * slope) +1);
//        deltaX = SPEED / distanceBetween(0,calcY(0),1, calcY(1));
        deltaX = Math.sqrt(x*x + calcY(x) * calcY(x)) / SPEED;

        if(xEnd < xStart){
            deltaX *= -1;
        }
    }

    public void move(){
        double x = getX() + (deltaX );
        double y = calcY(x);
        setLocation((int)x,(int)y);
    }

    public double calcY(double x){
        return slope * x  + yInt;
    }

    public int getOwnerID(){
        return ownerID;
    }

    public double distanceBetween(double x1, double y1, double x2, double y2){
        return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y2 - y1)  * (y2 - y1) ));
    }
}
