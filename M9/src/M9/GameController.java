package M9;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;


public class GameController implements KeyListener {
	
   private ShipEntity ship;
   
   private GameView gv;
   
   public Font font = null;
   
   private boolean gameRunning = true;
   
   Random rand = new Random();
   
   Image shipImg = new ImageIcon(getClass().getResource("/ship.png")).getImage();
   
   Image alienImg = new ImageIcon(getClass().getResource("/alien.png")).getImage();
   
   private HashMap<String, Boolean> keyDown = new HashMap<>(); 

   Entity[] spriteList = new Entity[6];

   public GameController(GameView gv) {
       this.gv = gv;
       
       try {
			String path = getClass().getResource("/droidlover.ttf").getFile();
			path =  URLDecoder.decode(path,"utf-8");
			   
			font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
			font = font.deriveFont(32f); // Typsnittsstorlek
		} catch (Exception e) {
			e.printStackTrace();
		} 
       
       gv.setKeyListener(this);
       
       keyDown.put("space", false);
       keyDown.put("left", false); 
       keyDown.put("right", false);
       
       loadImages();
   }

   private void loadImages() {
       
       ship = new ShipEntity(shipImg, 300, 300, 100);
       
       spriteList[0] = ship;
       spriteList[1] = new AlienEntity(alienImg, rand.nextInt(gv.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null),-100,20);
       spriteList[2] = new AlienEntity(alienImg, rand.nextInt(gv.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null),-300,20);
       spriteList[3] = new AlienEntity(alienImg, rand.nextInt(gv.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null),-500,20);
       spriteList[4] = new AlienEntity(alienImg, rand.nextInt(gv.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null),-700,20);
       spriteList[5] = new AlienEntity(alienImg, rand.nextInt(gv.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null),-900,20);
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
     
	   	alienMove(deltaTime);
	   	
	   	ship.move(deltaTime);
	   	
	   	checkCollisionAndRemove();
   }
   public void alienMove(long deltaTime) {
   	
		for (int i = 1 ; i< spriteList.length ; i++) {
			if (spriteList[i].getyPos() < gv.getHeight()) {
				spriteList[i].setDirectionX(1);
			    spriteList[i].move(deltaTime);
			}
			else { 
				spriteList[i].setyPos(-1 * alienImg.getHeight(null));
				spriteList[i].setxPos(rand.nextInt(gv.getWidth() / alienImg.getWidth(null)) * alienImg.getWidth(null));
			}
		}
	}
   
   public void checkCollisionAndRemove(){
	    ArrayList<Entity> removeList = new ArrayList<>();

	    // alien <-> missile
	    if(ship.missile != null && ship.missile.getActive()){
	    	for(int i= 1; i < spriteList.length ; i++) {
	    		if(ship.missile.collision(spriteList[i])) {
	    			ship.missile.setActive(false);
	    			removeList.add(spriteList[i]);	
	    		}	
	    	}
	    }
	    //spriteList.removeAll(removeList);// Alt namnet på arraylist  
	}
   
   public void render() {
       gv.render(spriteList);
   }

   public void run() {
	   
	   int fps = 50;
	   int updateTime = (int)(1.0/fps*1000000000.0);
	   	
	   long lastUpdateTime = System.nanoTime();
	   
	   while(gameRunning){
		   
		   long deltaTime = System.nanoTime() - lastUpdateTime;
		   
		   if(deltaTime > updateTime){
			   lastUpdateTime = System.nanoTime();
			   update(deltaTime);
			   render();
		   }
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

