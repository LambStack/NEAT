package model;

import view.Board;


import java.awt.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;


public class Player extends Rectangle {
    Long lastFired;
    public Long birthTime;
    public Long lastMoved;
    public long bonusPoints = 0;
    public long lastKilledTime;


    public static final int HEIGHT = 30, WIDTH = 30;
    public static int colorIndex = 0;
    public static int playerID = 0;
    private Color[] colors = {new Color(10, 200,0), new Color(178,250,0), new Color(0,255,50), Color.RED, new Color(25,255,137), Color.orange, Color.WHITE, Color.magenta, Color.yellow,
        new Color(106,255,178), new Color(255,255,0)};
    Color color;
    boolean stillAlive = true;
    int numKills = 0, id;
    ArrayList<Bullet> myBullets = new ArrayList<>();

    public Player(){
        super((int)((Board.WIDTH/2)),(int)((Board.HEIGHT /2)),WIDTH,HEIGHT);
        nextColor();


        lastFired = System.currentTimeMillis();
        birthTime = System.currentTimeMillis();
        lastMoved = System.currentTimeMillis();
        lastKilledTime  = System.currentTimeMillis();
        id = playerID++;
    }

    public Color getColor(){
        return this.color;
    }

    public void setLocation(double x, double v) {
        super.setLocation((int)x,(int)y);
    }

    public void moveUp() {
        int xLoc = (int) getX();
        int yLoc = (int)getY();
        if(yLoc > 50){
            this.setLocation(xLoc, yLoc - 10);
            lastMoved = System.currentTimeMillis();
        }
    }

    public void moveDown() {
        int xLoc = (int) getX();
        int yLoc = (int)getY();
        if(yLoc < Board.HEIGHT - 50){
            this.setLocation(xLoc, yLoc + 10);
            lastMoved = System.currentTimeMillis();
        }

    }

    public void moveLeft() {
        int xLoc = (int) getX();
        int yLoc = (int)getY();
        if(xLoc > 15){
            this.setLocation(xLoc - 10, yLoc);
            lastMoved = System.currentTimeMillis();
        }
    }

    public void moveRight() {
        int xLoc = (int) getX();
        int yLoc = (int)getY();
        if(xLoc < Board.WIDTH -50){
            this.setLocation(xLoc + 10, yLoc);
            lastMoved = System.currentTimeMillis();
        }
    }

    public boolean hasMovedRecently(){
        return System.currentTimeMillis() - 5000 < lastMoved;
    }

    public ArrayList<Bullet> getBullets(){
        return myBullets;
    }

    public void addBullet(double xEnd, double yEnd){
        if(System.currentTimeMillis() - lastFired > 400 && System.currentTimeMillis() - birthTime > 3000 && myBullets.size() < 5){
            myBullets.add(new Bullet(xEnd, yEnd, getX() + (WIDTH /2), getY() + (HEIGHT/2), getID()));
            lastFired = System.currentTimeMillis();
        }

    }

    public void setCleanBullets(ArrayList<Bullet> cleanBullets){
        this.myBullets = cleanBullets;
    }

    public boolean isStillAlive() {
        return stillAlive;
    }

    public void setStillAlive(boolean b){
        stillAlive = b;
    }

    public void addKill(){
        numKills ++;
    }

    public int getKills(){
        return numKills;
    }

    public void clearKills(){this.numKills = 0;}

    public int getID(){
        return id;
    }

    @Override
    public String toString(){
        return hashCode() + "("+color+") has " + getKills() + " Kills. " + bonusPoints + " bonus points";
    }

    public Bullet getNearestBulletTo(double x, double y){
        if(myBullets.size() == 0){
            return null;
        }
        Bullet nearestBullet = null;
        double minDistance = 99999;
        for(Bullet b: myBullets){
            double distance =  Math.sqrt(Math.pow(x - b.getX(),2) + Math.pow(y - b.getY(), 2));
            if(minDistance > distance){
                nearestBullet = b;
                minDistance = distance;
            }
        }
        return nearestBullet;
    }

    public double xDistanceTo(double x){
        return getX() - x;
    }

    public double yDistanceTo(double y){
        return getY() - y;
    }
    public static Bullet getNearestBulletTo(double x, double y, ArrayList<PlayerAI> players, Player self){
            Bullet nearestBullet = null;
            for(Player p: players){
                if(!p.equals(self)){
                Bullet playersNearestBullet = p.getNearestBulletTo(x,y);
                    if(nearestBullet == null){
                        nearestBullet = playersNearestBullet;
                    }
                    else if(playersNearestBullet != null &&
                        Math.sqrt(Math.pow(x - playersNearestBullet.getX(),2) + Math.pow(y - playersNearestBullet.getY(), 2)) <
                        Math.sqrt(Math.pow(x - nearestBullet.getX(),2) + Math.pow(y - nearestBullet.getY(), 2))){
                        nearestBullet = playersNearestBullet;
                    }
                }

            }
            return nearestBullet;
    }

    public static Player getNearestPlayerTo(double x, double y, ArrayList<PlayerAI> players, Player self){
            Player nearestPlayer = players.get(0);
            for(Player p: players){
                if(!p.equals(self)){
                     if(
                        Math.sqrt(Math.pow(x - p.getX(),2) + Math.pow(y - p.getY(), 2)) <
                        Math.sqrt(Math.pow(x - nearestPlayer.getX(),2) + Math.pow(y - nearestPlayer.getY(), 2))){
                        nearestPlayer = p;
                    }
                }

            }
            return nearestPlayer;
    }

    public static int countBullets(ArrayList<PlayerAI> players, Player self){
        int numBullets = 0;
        for(Player p: players){
            if(!p.equals(self)){
                numBullets++;
            }

        }
        return numBullets;
    }

    void nextColor(){

        if(colorIndex >= colors.length){
            colorIndex = 0;
        }
        color =  colors[colorIndex++];
    }
}
