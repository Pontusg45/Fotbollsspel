package M9;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

public class ShipEntity extends Entity{
	
	public MissileEntity missile = null;
	
 	public ShipEntity (Image image, int xPos, int yPos, int speed){
      	super(image, xPos, yPos, speed);
      	
 	}
 
 	/**
 	 * Ändrar läget i x-led
 	 */
 	public void move(long deltaTime){
    	   if(missile != null && missile.getActive()) {
        	    missile.move(deltaTime);    
        	}
    	   xPos += dx*(deltaTime/1000000000.0)*speed;
 	}
 	
 	public void draw(Graphics2D g){
 	    if(missile != null && missile.getActive()){
 	        missile.draw(g);   
 	    }
 	    super.draw(g);
 	}
 	
 	public boolean tryToFire(){
 	    if(missile == null || !missile.getActive()){
 	        missile = new MissileEntity(new ImageIcon(getClass().getResource("/missile.png")).getImage(), xPos+13, yPos, 130);
 	        missile.setActive(true);
 	        return true;
 	    }else
 	        return false;
 	}
}
