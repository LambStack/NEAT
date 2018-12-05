import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by jagnelli on 11/20/2018
 */
public class State extends JFrame {
  public static final int WIDTH = 800, HEIGHT = 375; // Size of our example
    private ArrayList<Drawable> drawables = new ArrayList<>();
    private ArrayList<Player> deadPlayers = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    int generation = 0;
  public String getName() {
    return "Paints";
  }

  public int getWidth() {
    return WIDTH;
  }

  public int getHeight() {
    return HEIGHT;
  }

  /** Draw the example */
  public void paint(Graphics g) {
    super.paint(g);
    g.setColor(Color.black);
    g.drawString("Generation: " + generation  , 200, 200);
    g.setColor(Color.RED);
    for(Player p: players){
        g.fillRect(p.getX(),p.getY(), p.getWidth(),p.getHeight());
    }
    g.setColor(Color.BLUE);
    for(Drawable d: drawables){
        if(d.getX() < WIDTH){
           g.fillRect(d.getX(),d.getY(), d.getWidth(),d.getHeight());
        }
    }


  }
  public static void main(String[] a) {
      try {
          new State();
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  }

  private State() throws InterruptedException {
    setBackground(Color.black);
    // create a bunch of random obstacles
    for(int i =0; i<10; i++){
        players.add(Player.createRandomPlayer(10, HEIGHT-20, 20, 10));
    }

    for(int i = 0;i < 300; i+=2){
        double itemHeight = Math.random() * 2.0 * Drawable.HEIGHT;
        drawables.add(new Obstacle((Math.random() * i * 400.0 + 800 ), HEIGHT-itemHeight, itemHeight, 20));
    }

    setSize(getWidth(),getHeight());
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);
    long timeSoFar = 0;
    // game loop
    while(true){
        for(Drawable d: drawables){
            d.moveLeft();
        }

        timeSoFar += 1;

        Drawable nextObs = drawables.get(0);
        for(Player p:players){
            p.getOutPut(nextObs.getX() - p.getX(), nextObs.getHeight());
        }
        cleanDrawables();
        for(Player p:players){
           p.calcMaxFitness(timeSoFar);
        }

        if(drawables.size()<20){
            for(int i = 0;i < 300; i+=2){
                double itemHeight = Math.random() * 2.0 * Drawable.HEIGHT;
                drawables.add(new Obstacle((Math.random() * i * 400.0 + 1000 ), HEIGHT-itemHeight, itemHeight, 20));
            }
        }
        repaint();
        Thread.sleep(30);

        if(players.size() == 0){
           // JOptionPane.showMessageDialog(null, "Round over");
            chooseSurvivors();
            generation ++;
        }
    }
  }

  private void cleanDrawables(){
    ArrayList<Drawable> cleanDrawables = new ArrayList<>();
    ArrayList<Player> cleanPlayers = new ArrayList<>();


    for(Drawable d: drawables){
        if(d.getX() > 0){
            cleanDrawables.add(d);
        }
    }
    cleanDrawables.sort(new Comparator<Drawable>() {
        @Override
        public int compare(Drawable o1, Drawable o2) {
            return o1.compareTo(o2);
        }
    });
    drawables = cleanDrawables;
    Drawable d = drawables.get(0);
    for(Player p : players){
        if(Math.abs(p.getX() - d.getX()) >= 5 || Math.abs(p.getY() - HEIGHT) >= d.getHeight()){
            cleanPlayers.add(p);
        }
        else{
            deadPlayers.add(p);
        }
    }

    players = cleanPlayers;
  }

  private void chooseSurvivors(){
        deadPlayers.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(deadPlayers.get(0));
        players.add(deadPlayers.get(0));
        players.add(deadPlayers.get(1));
        players.add(deadPlayers.get(0).procreate(deadPlayers.get(1)));
        players.add(deadPlayers.get((int)(Math.random() * 4)).procreate(deadPlayers.get((int)(Math.random() * 4))));
        players.add(deadPlayers.get((int)(Math.random() * 4)).procreate(deadPlayers.get((int)(Math.random() * 4))));
        players.add(deadPlayers.get((int)(Math.random() * 4)).procreate(deadPlayers.get((int)(Math.random() * 4))));
        players.add(deadPlayers.get((int)(Math.random() * 4)).procreate(deadPlayers.get((int)(Math.random() * 4))));
        players.add(deadPlayers.get((int)(Math.random() * 4)).procreate(deadPlayers.get((int)(Math.random() * 4))));
        players.add(deadPlayers.get((int)(Math.random() * 4)).procreate(deadPlayers.get((int)(Math.random() * 4))));
        players.add(deadPlayers.get((int)(Math.random() * 4)).procreate(deadPlayers.get((int)(Math.random() * 4))));

        for(Player p:players){
            p.mutate(.3);
        }
  }
}

