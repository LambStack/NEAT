package view;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JFrame {
   private static final int NUM_PLAYERS = 10;
  public static final int WIDTH = 1600, HEIGHT = 700; // Size of our example
  public static final int SLEEP_TIMER = 1000 / 20;
  int generation = 0;
  Rectangle bonusPoints = new Rectangle(WIDTH/2 - 100, HEIGHT/2 - 100, 200, 200);
  Color intersectingColor = Color.black;
//   Player human;
    private ArrayList<PlayerAI> gunners = new ArrayList<>();
    private ArrayList<PlayerAI> deadGunners = new ArrayList<>();
    private ArrayList<PlayerAI> dodgers = new ArrayList<>();
    private ArrayList<PlayerAI> deadDodgers = new ArrayList<>();



    @Override
    public void paint(Graphics g1) {
    super.paint(g1);
    Graphics2D g = (Graphics2D)g1;

    g.setColor(Color.darkGray);
    g.fillRect(0,0,WIDTH,HEIGHT);
    g.setColor(Color.white);
    g.drawString("Generation: " + String.valueOf(generation),50,50);
    g.drawString("Gunners left: " + String.valueOf(gunners.size()),WIDTH - 100,50);
    g.drawString("Dodgers left: " + String.valueOf(dodgers.size()),WIDTH - 100,100);
    g.setColor(intersectingColor);
    g.draw(bonusPoints);
//    g.setColor(human.getColor());
//    g.fill(human);
//    for(Bullet b: human.getBullets()){
//            g.fill(b);
//        }
    g.setStroke(new BasicStroke(5));
    for(Player p: gunners){
        g.setColor(p.getColor());
        g.draw(p);
        for(Bullet b: p.getBullets()){
            g.fill(b);
        }
    }
    for(Player p: dodgers){
        g.setColor(p.getColor());
        g.fill(p);
    }
  }


  public static void main(String[] args){
    new Board();
  }

  public Board(){
//   human = new Player();
    boolean gameContinues = true;
//    gunners.add(new Player());
    for(int i =0; i<NUM_PLAYERS; i++){
        gunners.add(new Gunner(5, 4, 6));
        dodgers.add(new Dodger(11, 4, 4));
    }
    this.setSize(WIDTH,HEIGHT);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setVisible(true);
//    this.addKeyListener(new PlayerListener(human));
//    addMouseListener(new FireListener(human));

    while(gameContinues){
        cleanUp();
        gameLoop();
        this.repaint();

        if(dodgers.size() == 0 || gunners.size() == 0){
            chooseNewPlayers();
        }

        try {
            Thread.sleep(SLEEP_TIMER);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
  }

  public void gameLoop(){
      ArrayList<PlayerAI> cleanGunners = new ArrayList<>();
      ArrayList<PlayerAI> cleanDodgers = new ArrayList<>();

//      for(Bullet b: human.getBullets()){
//        b.move();
//      }
       boolean aPlayerIsGettingBonusPoints = false;
       for(PlayerAI p: dodgers){
             if(bonusPoints.intersects(p)){
                p.bonusPoints++;
                intersectingColor = p.getColor();
                aPlayerIsGettingBonusPoints = true;
            }

            ArrayList<Double> inputs = new ArrayList<>();
            Bullet bullet = Player.getNearestBulletTo(p.getX(), p.getY(), gunners, p);
            double xPos = bullet != null ? bullet.getX() : 0;
            double yPos = bullet != null ? bullet.getY() : 0;
            inputs.add(p.getBullets().size() * 1.0); // num of bullets 1
            inputs.add(p.xDistanceTo(xPos)); // the nearest bullet x 2
            inputs.add(p.yDistanceTo(yPos)); // nearest bullet y 3
            inputs.add(Player.countBullets(gunners,p) * 1.0); // number of bullets 4
            inputs.add(p.xDistanceTo(100)); // distance from left // 8
            inputs.add(p.yDistanceTo(100)); // distance from top // 9
            inputs.add(p.intersects(bonusPoints) ? 1.0 : 0.0); // getting bonus points 10
            inputs.add(p.xDistanceTo(bonusPoints.getX())); // 11
            inputs.add(p.yDistanceTo(bonusPoints.getY())); //12
            inputs.add(p.xDistanceTo(bonusPoints.getX() + bonusPoints.width)); //13
            inputs.add(p.yDistanceTo(bonusPoints.getY() + bonusPoints.height));  //14

            Double[] inputVals = inputs.toArray(new Double[inputs.size()]);
            p.move(inputVals);

            if(p.isStillAlive()){
                cleanDodgers.add(p);
            }
            if(!aPlayerIsGettingBonusPoints){
                intersectingColor = Color.black;
            }
       }
      for (PlayerAI p: gunners){
//        Bullet bullet = human.getNearestBulletTo(p.getX(),p.getY());

        ArrayList<Double> inputs = new ArrayList<>();
            Player nearestPlayer = Player.getNearestPlayerTo(p.getX(), p.getY(), dodgers, p);
            inputs.add((nearestPlayer.getX() -Player.WIDTH / 2)); // nearestPLayer x  1
            inputs.add(nearestPlayer.getY()-Player.HEIGHT / 2); // nearestPlayer y  2
            inputs.add(dodgers.size() * 1.0); // num enemies  3
            inputs.add(p.xDistanceTo(100)); // distance from left // 4
            inputs.add(p.yDistanceTo(100)); // distance from top // 5

        Double[] inputVals = inputs.toArray(new Double[inputs.size()]);
        p.move(inputVals);
        for(Bullet b: p.getBullets()){
            b.move();
        }
        if(p.isStillAlive()){
            cleanGunners.add(p);
        }
      }
      gunners = cleanGunners;
      dodgers = cleanDodgers;
  }

  public void cleanUp(){
    for(PlayerAI p: gunners){
        ArrayList<Bullet> playersCleanBullets = new ArrayList<>();
        for(Bullet bullet: p.getBullets()){
            double bulletX = bullet.getX(), bulletY = bullet.getY();
            if(bulletX > 0 &&  bulletX < WIDTH && bulletY > 0 && bulletY < HEIGHT){
                // check collision on gunners, we will delete the bullet if it does
                boolean collided = false;
                for(PlayerAI player: dodgers){
                    if(player.isStillAlive() && bullet.intersects(player)){
                        collided = true;
                        System.out.println(player.toString());
                        player.setStillAlive(false);
                        deadDodgers.add(player);
                        p.addKill();
                        System.out.println(p.toString());
                        p.lastKilledTime = System.currentTimeMillis();
                        break;
                    }
                }
                if(!collided)
                    playersCleanBullets.add(bullet);
            }


        }
        p.setCleanBullets(playersCleanBullets);

        if(p.lastKilledTime + 10000 < System.currentTimeMillis()){
            p.setStillAlive(false);
            deadGunners.add(p);
        }
    }
  }

  private void chooseNewPlayers(){
    try{  // try catch just for debugging
        ArrayList<PlayerAI> newGunners = new ArrayList<>();
        ArrayList<PlayerAI> newDodgers = new ArrayList<>();

        ArrayList<PlayerAI> deadAndAliveGunners = (ArrayList<PlayerAI>) deadGunners.clone();
        deadAndAliveGunners.addAll(gunners);

        ArrayList<PlayerAI> deadAndAliveDodgers = (ArrayList<PlayerAI>) deadDodgers.clone();
        deadAndAliveDodgers.addAll(dodgers);

        // find the players who've lived the longest
        newGunners.add(findMax("life",deadAndAliveGunners));
        newDodgers.add(findMax("life",deadAndAliveDodgers));

        //players with most kills/ bonus
        newGunners.add(findMax("kills",deadAndAliveGunners));
        newDodgers.add(findMax("bonus",deadAndAliveDodgers));

        // breed those  ** may often be the same player **
        newGunners.add(PlayerAI.breed(newGunners.get(0), newGunners.get(1)));
        newDodgers.add(PlayerAI.breed(newDodgers.get(0), newDodgers.get(1)));

        // lived longest with random player
        newGunners.add(PlayerAI.breed(newGunners.get(0), deadAndAliveGunners.get((int)(Math.random() * deadAndAliveGunners.size()))));
        newDodgers.add(PlayerAI.breed(newDodgers.get(0), deadAndAliveDodgers.get((int)(Math.random() * deadAndAliveDodgers.size()))));

        // most kills / bonus with random player
        newGunners.add(PlayerAI.breed(newGunners.get(1), deadAndAliveGunners.get((int)(Math.random() * deadAndAliveGunners.size()))));
        newDodgers.add(PlayerAI.breed(newDodgers.get(1), deadAndAliveDodgers.get((int)(Math.random() * deadAndAliveDodgers.size()))));

        // most kills / bonus with random player
        newGunners.add(PlayerAI.breed(newGunners.get(1), deadAndAliveGunners.get((int)(Math.random() * deadAndAliveGunners.size()))));
        newDodgers.add(PlayerAI.breed(newDodgers.get(1), deadAndAliveDodgers.get((int)(Math.random() * deadAndAliveDodgers.size()))));


        // most kills / bonus with random player
        newGunners.add(PlayerAI.breed(newGunners.get(1), deadAndAliveGunners.get((int)(Math.random() * deadAndAliveGunners.size()))));
        newDodgers.add(PlayerAI.breed(newDodgers.get(1), deadAndAliveDodgers.get((int)(Math.random() * deadAndAliveDodgers.size()))));


        // a random player as is
        newGunners.add(deadAndAliveGunners.get((int)(Math.random() * deadAndAliveGunners.size())));
        newDodgers.add(deadAndAliveDodgers.get((int)(Math.random() * deadAndAliveDodgers.size())));

        // some random players to fill it out
        while(newGunners.size() < NUM_PLAYERS){
            newGunners.add(PlayerAI.breed(deadAndAliveGunners.get((int)(Math.random() * deadAndAliveGunners.size())),
                 deadAndAliveGunners.get((int)(Math.random() * deadAndAliveGunners.size()))));

        }
        while(newDodgers.size() < NUM_PLAYERS){
            newDodgers.add(PlayerAI.breed(deadAndAliveDodgers.get((int)(Math.random() * deadAndAliveDodgers.size())),
                 deadAndAliveDodgers.get((int)(Math.random() * deadAndAliveDodgers.size()))));

        }

        while(newGunners.size() > NUM_PLAYERS){
            newGunners.remove((int)(Math.random() * deadAndAliveGunners.size()));

        }
        while(newDodgers.size() > NUM_PLAYERS){
            newDodgers.remove((int)(Math.random() * deadAndAliveDodgers.size()));

        }

        for(int i = 0; i<NUM_PLAYERS; i++){
            newDodgers.get(i).mutate();
            newGunners.get(i).mutate();
        }

        generation++;

        deadGunners.clear();
        deadDodgers.clear();
        gunners = newGunners;
        dodgers = newDodgers;
    }
    catch (Exception e){
        e.printStackTrace();
    }


  }

  private PlayerAI findMax(String attribute, ArrayList<PlayerAI> players){
    PlayerAI playerWithMax = players.get(0);
    for(PlayerAI p : players){
        switch (attribute){
            case "kills":
                if(playerWithMax.getKills() < p.getKills()){
                    playerWithMax = p;
                }
                break;
            case "life":
                if(System.currentTimeMillis() - playerWithMax.birthTime < System.currentTimeMillis() - p.birthTime ){
                    playerWithMax = p;
                }
                break;
            case "bonus":
                if(playerWithMax.bonusPoints < p.bonusPoints ){
                    playerWithMax = p;
                }
                break;
        }

    }

    return playerWithMax;
  }
}
