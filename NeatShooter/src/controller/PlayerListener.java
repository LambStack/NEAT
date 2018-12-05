package controller;

import model.Player;
import view.Board;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;

/**
 * Created by jagnelli on 11/20/2018.
 */
public class PlayerListener extends KeyAdapter {
    Player player;
    private boolean wHeld = false, sHeld = false, aHeld = false, dHeld = false;

    public PlayerListener(Player p){
        this.player = p;
        new Thread(()->{
            while(true){
                if(wHeld){
                    player.moveUp();
                }
                else if(sHeld){
                    player.moveDown();
                }

                if(aHeld){
                    player.moveLeft();
                }
                else if(dHeld){
                    player.moveRight();
                }
                try {
                    Thread.sleep(Board.SLEEP_TIMER);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);

        switch (e.getKeyCode()){
            case KeyEvent.VK_W : {
                wHeld = true;
                break;
            }
            case KeyEvent.VK_S : {
                sHeld = true;
            }
            break;
            case KeyEvent.VK_A : {
               aHeld = true;
            }
            break;
            case KeyEvent.VK_D : {
               dHeld = true;
            }
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        switch (e.getKeyCode()){
            case KeyEvent.VK_W : {
               wHeld = false;
               break;
            }
            case KeyEvent.VK_S : {
                sHeld = false;
                break;
            }
            case KeyEvent.VK_A : {
                aHeld = false;
                break;
            }
            case KeyEvent.VK_D : {
                dHeld = false;
                break;
            }
        }
    }
}
