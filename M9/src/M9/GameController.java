package M9;

import java.awt.Image;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import sun.misc.GC;

public class GameController implements KeyListener {
   private ShipEntity ship;
   private GameView gv;
   private boolean gameRunning = true;
   
   Image shipImg = new ImageIcon(getClass().getResource("/ship.png")).getImage();
   
   private HashMap<String, Boolean> keyDown = new HashMap<>(); 

   private ArrayList<Entity > entityList = new ArrayList<>();

   public GameController(GameView gv) {
       this.gv = gv;
       
       
       gv.setKeyListener(this);
       
       keyDown.put("space", false);
       keyDown.put("left", false); 
       keyDown.put("right", false);
       
       loadImages();
   }

   private void loadImages() {
       
       ship = new ShipEntity(shipImg, 300, 300, 30);
       entityList.add(ship);
       entityList.add(new AlienEntity(new ImageIcon(getClass().getResource("/alien.png")).getImage(), 100,40,20));
       entityList.add(new AlienEntity(new ImageIcon(getClass().getResource("/alien.png")).getImage(), 200,40,20));
       entityList.add(new AlienEntity(new ImageIcon(getClass().getResource("/alien.png")).getImage(), 300,40,20));
       entityList.add(new AlienEntity(new ImageIcon(getClass().getResource("/alien.png")).getImage(), 400,40,20));
   }

   private void update(long deltaTime) {
	   
	   if(keyDown.get("right") && ship.getxPos() < gv.getWidth() - shipImg.getWidth(null) ) { 
       	ship.setDirectionX(1); 
   	}else if(keyDown.get("left") && ship.getxPos()> 0) { 
       	ship.setDirectionX(-1); 
   	}else {
       	ship.setDirectionX(0);
   	}
	   if(keyDown.get("space")) {
  		 ship.tryToFire();
  	}   
       for(Entity e : entityList) {
           e.move(deltaTime);
       }
   }
   
   public void render() {
       gv.render(entityList);
   }

   public void run() {
       long lastUpdateTime = System.nanoTime();
       long deltaTime;

       while(gameRunning) {
           /** Tiden som gått sedan senaste uppdateringen */
           deltaTime = System.nanoTime() - lastUpdateTime;
           lastUpdateTime = System.nanoTime();

           update(deltaTime); // gör inget i nuläget
           render();
           try {Thread.currentThread().sleep(5);}catch (Exception e) {} // Paus 10 ms
       }
   }

   @Override
   public void keyTyped(KeyEvent e) {

   }

   @Override
   public void keyPressed(KeyEvent e) {
		
   	int key = e.getKeyCode(); 

   	if(key == KeyEvent.VK_LEFT) 
       	keyDown.put("left", true); 
   	else if(key == KeyEvent.VK_RIGHT) 
       	keyDown.put("right", true);
   	else if(key == KeyEvent.VK_SPACE) 
       	keyDown.put("space", true);
	}

	public void keyReleased(KeyEvent e) {
   	int key = e.getKeyCode(); 

   	if(key == KeyEvent.VK_LEFT) 
       	keyDown.put("left", false); 
   	else if(key == KeyEvent.VK_RIGHT) 
       	keyDown.put("right", false);
   	else if(key == KeyEvent.VK_SPACE) 
       	keyDown.put("space", false);
	}
}

