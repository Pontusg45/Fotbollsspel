package M9;

import java.awt.Color;
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
   
   //private Entity background;
   
   private GameView gv;
   
   public Font font = null;
   
   private boolean gameRunning = true;
   
   private int points;
   
   Random rand = new Random();
   
   Image shipImg = new ImageIcon(getClass().getResource("/ship.png")).getImage();
   
   Image alienImg = new ImageIcon(getClass().getResource("/alien.png")).getImage();
   
   //Image bgImg = new ImageIcon(getClass().getResource("/background.jpg")).getImage();
   
   private HashMap<String, Boolean> keyDown = new HashMap<>(); 

   ArrayList<Entity> spriteList = new ArrayList<>();

   public GameController(GameView gv) {
	   this.gv = gv;
       
       try {
    	   String path = getClass().getResource("/droidlover.ttf").getFile();
    	   path =  URLDecoder.decode(path,"utf-8");
			   
    	   font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
    	   font = font.deriveFont(32f);
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
	   
	   //background = new Entity(0,0, bgImg);
	   //gv.setBackground(background);
	   
	   int shipPosX = gv.getWidth()/2- shipImg.getWidth(null)/2;
	   int shipPosY = gv.getHeight()- 10 - shipImg.getHeight(null);
   	
	   ship = new ShipEntity(shipImg, shipPosX, shipPosY, 100);
       
       spriteList.add(ship);
       spriteList.add(new TextEntity("Space Invader", 10, 32, font, Color.GREEN));
       spriteList.add(new TextEntity("Score: " + getPoints(), 600, 32, font, Color.GREEN));
       spriteList.add(new AlienEntity(alienImg, rand.nextInt(gv.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null),-100,50));
       spriteList.add(new AlienEntity(alienImg, rand.nextInt(gv.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null),-300,50));
       spriteList.add(new AlienEntity(alienImg, rand.nextInt(gv.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null),-500,50));
       spriteList.add(new AlienEntity(alienImg, rand.nextInt(gv.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null),-700,50));
       spriteList.add(new AlienEntity(alienImg, rand.nextInt(gv.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null),-900,50));
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
	   	
	   	spriteList.get(2).setTxt("Score: " + getPoints());
	   	spriteList.get(2).getTxt();
   }
   
   public void alienMove(long deltaTime) {
   	
		for (int i = 3 ; i< spriteList.size(); i++) {
			if (spriteList.get(i).getyPos() < gv.getHeight()) {
				spriteList.get(i).setDirectionX(1);
				spriteList.get(i).move(deltaTime);
			}
			else { 
				spriteList.get(i).setyPos(-1 * alienImg.getHeight(null));
				spriteList.get(i).setxPos(rand.nextInt(gv.getWidth() / alienImg.getWidth(null)) * alienImg.getWidth(null));
			}
		}
	}
   
   public void checkCollisionAndRemove(){
	    ArrayList<Entity> removeList = new ArrayList<Entity>();

	    if(ship.missile != null && ship.missile.getActive()){
	    	for(int i= 3; i < spriteList.size() ; i++) {
	    		if(ship.missile.collision(spriteList.get(i)) || ship.missile.getyPos() < 0) {
	    			ship.missile.setActive(false);
	    			removeList.add(spriteList.get(i));
	    			spriteList.add(new AlienEntity(alienImg, rand.nextInt(gv.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null),- alienImg.getHeight(null),50));
	    			setPoints(getPoints()+ 100);
	    			
	    		}
	    	}
	    }
	    spriteList.removeAll(removeList); 
	}
   
   public void render() {
	   gv.beginRender();
       gv.openRender(spriteList);
       gv.show();
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
	
	 @Override
	   public void keyTyped(KeyEvent e) {

	   }

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	 
}

