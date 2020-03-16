package M9;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Game {

	   public static void main(String[] args) {
	       GameView gv = new GameView(800, 600,"Game");

	       Image shipImg = new ImageIcon(Game.class.getResource("/ship.png")).getImage();
	       ShipEntity ship = new ShipEntity(shipImg, 300, 300, 20);

	       gv.render(ship);
	   }
	}

