import org.omg.CORBA.MARSHAL;

/**
 * Created by jagnelli on 11/20/2018.
 */
public class Player extends Drawable {
    boolean isJumping = false;
    double moderatorDistanceToNext = 1.0, moderatorHeightOfNext = 1.0;
    long maxFitness = 0; // total number of ticks survived
    public Player(double xPos, double yPos, double height, double width, double modDist, double modHeight) {
        super(xPos, yPos, height, width);
        this.moderatorDistanceToNext = modDist;
        this.moderatorHeightOfNext = modHeight;
    }

    @Override
    public void moveLeft(){
        // do nothing
    }

    @Override
    public int compareTo(Object o) {
        return (int)(((Player)o).getMaxFitness() - getMaxFitness());
    }

    public boolean isOnGround(){
        return getY() + getHeight() - State.HEIGHT < 1;
    }
    private void jump(){

        isJumping = true;
        new Thread(()->{
            // go up
            for(int i=0;i<50;i++){
                setY(getY() - 2);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // go down
            for(int i=0;i<50;i++){
                setY(getY() + 2);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            isJumping = false;
        }).start();
    }

    public void getOutPut(double distToNext, double heightOfNext){
        if(isOnGround() && !isJumping){
            if( Math.signum(distToNext * moderatorDistanceToNext + heightOfNext * moderatorHeightOfNext ) > 0){
                jump();
            }
        }
    }

    public Player procreate(Player otherParent){
           double modDist = (Math.random() > .05 ? this.moderatorDistanceToNext : otherParent.moderatorDistanceToNext);
           double modHeight = (Math.random() > .05 ? this.moderatorHeightOfNext : otherParent.moderatorHeightOfNext);

           return  new Player(10, State.HEIGHT-20,getHeight(),getWidth(),modDist,modHeight);
    }

    void mutate(double chance){
        if(Math.random() < chance){
           this.moderatorDistanceToNext *= (1 + ((Math.random() -.5) * 3) + (Math.random() -.5));
        }
        if(Math.random() < chance){
           this.moderatorHeightOfNext *= (1 + ((Math.random() -.5) * 3) + (Math.random() -.5));
        }
    }

    void calcMaxFitness(long milisPassed){
        maxFitness = Math.max(maxFitness, milisPassed);
    }

    long getMaxFitness(){
        return maxFitness;
    }

    static Player createRandomPlayer(double xPos, double yPos, double height, double width){
        Player p = new Player(xPos,yPos,height,width,Math.cos(Math.random()* Math.PI),Math.cos(Math.random()* Math.PI));
        return p;
    }

    public String toString(){
        return "distToNext * " + moderatorDistanceToNext +" + heightOfNext * "+ moderatorHeightOfNext;
    }
}
