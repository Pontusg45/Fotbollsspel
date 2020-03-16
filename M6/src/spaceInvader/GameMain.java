package spaceInvader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.ImageIcon;
import se.egy.graphics.GameScreen;
import se.egy.graphics.TxtContainer;
 
public class GameMain implements KeyListener {
 
	private boolean gameRunning = true;
	private long lastUpdateTime;
	
	private TxtContainer msg;
	
	private HashMap<String, Boolean> keyDown = new HashMap<>(); 
	
	Random rand = new Random();
	
	Entity[] spriteList = new Entity[6];
 
	private ShipEntity ship;
	
	public ShipEntity tryToFire;
	
	public Font font = null;
 
	private GameScreen gameScreen = new GameScreen("Game", 640,480, false);
 
	public GameMain(){
		
		try {
			String path = getClass().getResource("/droidlover.ttf").getFile();
			path =  URLDecoder.decode(path,"utf-8");
			   
			font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
			font = font.deriveFont(32f); // Typsnittsstorlek
		} catch (Exception e) {
			e.printStackTrace();
		} 

		msg = new TxtContainer("Space Invader", 100, 32, font, Color.GREEN);
		
    	gameScreen.setKeyListener(this);
    	
    	keyDown.put("space", false);
    	keyDown.put("left", false); 
    	keyDown.put("right", false);
    	
    	loadImages();
    	gameLoop();
	}
	
	Image shipImg = new ImageIcon(getClass().getResource("/ship.png")).getImage();
	
	Image alienImg = new ImageIcon(getClass().getResource("/alien.png")).getImage();
	
	Image missileImg = new ImageIcon(getClass().getResource("/missile.png")).getImage();
	
	public void loadImages(){      
 
		int shipPosX = gameScreen.getWidth()/2- shipImg.getWidth(null)/2;
		int shipPosY = gameScreen.getHeight()- 10 - shipImg.getHeight(null);
    	
    	//double alienPosY = 50;
    	ship = new ShipEntity(shipImg, shipPosX, shipPosY, 100);
    	
    	spriteList[0] = ship;
    	spriteList[1] = new AlienEntity(alienImg, rand.nextInt(gameScreen.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null), -100, 100);
    	spriteList[2] = new AlienEntity(alienImg, rand.nextInt(gameScreen.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null), -300, 100);
    	spriteList[3] = new AlienEntity(alienImg, rand.nextInt(gameScreen.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null), -500, 100);
    	spriteList[4] = new AlienEntity(alienImg, rand.nextInt(gameScreen.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null), -700, 100);
    	spriteList[5] = new AlienEntity(alienImg, rand.nextInt(gameScreen.getWidth() / alienImg.getWidth(null))* alienImg.getWidth(null), -900, 100);		
	}
 
	public void update(long deltaTime){
    	if(keyDown.get("right") && ship.getxPos() < gameScreen.getWidth() - shipImg.getWidth(null) ) { 
        	ship.setDirectionX(1); 
    	}else if(keyDown.get("left") && ship.getxPos()> 0) { 
        	ship.setDirectionX(-1); 
    	}else {
        	ship.setDirectionX(0);
    	}
    	if(keyDown.get("space")) {
    		 ship.tryToFire();
    		 
    	}
 
    	ship.move(deltaTime);
    	 
    	alienMove(deltaTime);
    	checkCollisionAndRemove();
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
	    //spriteList.removeAll(removeList)// Alt namnet på arraylist
	}

 	public void alienMove(long deltaTime) {
    	
 		for (int i = 1 ; i< spriteList.length ; i++) {
 			if (spriteList[i].getyPos() < gameScreen.getHeight()) {
 				spriteList[i].setDirectionX(1);
 			    spriteList[i].move(deltaTime);
 			}
 			else { 
 				spriteList[i].setyPos(-1 * alienImg.getHeight(null));
 				spriteList[i].setxPos(rand.nextInt(gameScreen.getWidth() / alienImg.getWidth(null)) * alienImg.getWidth(null));
 			}
 		}
 	}
 	
 	public void render(){
     	gameScreen.beginRender();
               	
     	gameScreen.openRender(msg);
     	gameScreen.openRender(spriteList);
     	
     	gameScreen.show();
 	}

	public void gameLoop(){
    	
	    int fps = 50;
	    int updateTime = (int)(1.0/fps*1000000000.0);

    	lastUpdateTime = System.nanoTime();
 
    	while(gameRunning){
        	
        	long deltaTime = System.nanoTime() - lastUpdateTime;
 
        	if(deltaTime > updateTime){
                lastUpdateTime = System.nanoTime();
                update(deltaTime);
                render();
        	}
    	}
	}
 
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
 
	public void keyTyped(KeyEvent e) {
	}
 
	public static void main(String[] args) {
    	    new GameMain();
	}
}

