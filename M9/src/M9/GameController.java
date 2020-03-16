package M9;

import java.awt.Image;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class GameController implements KeyListener {
   private ShipEntity ship;
   private GameView gv;
   private boolean gameRunning = true;

   private ArrayList<Entity > entityList = new ArrayList<>();

   public GameController(GameView gv) {
       this.gv = gv;
       gv.setKeyListener(this);
       loadImages();
   }

   private void loadImages() {
       Image shipImg = new ImageIcon(getClass().getResource("/ship.png")).getImage();

       ship = new ShipEntity(shipImg, 300, 300, 30);
       entityList.add(ship);
       entityList.add(new AlienEntity(new ImageIcon(getClass().getResource("/alien.png")).getImage(), 100,40,20));
   }

   private void update(long deltaTime) {
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
       if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
           ship.setDirectionX(1);
       } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
           ship.setDirectionX(-1);
       }
   }

   @Override
   public void keyReleased(KeyEvent e) {
       if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT) {
           ship.setDirectionX(0);
       }
   }
}

