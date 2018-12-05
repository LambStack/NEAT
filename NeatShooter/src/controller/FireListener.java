package controller;

import model.Player;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by jagnelli on 11/20/2018.
 */
public class FireListener extends MouseAdapter{
    Player player;
    public FireListener(Player p){
        player = p;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        player.addBullet(e.getX(), e.getY());
    }
}
