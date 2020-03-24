package fotbollsspel;

import java.awt.Graphics2D;
import java.awt.Image;
//import java.awt.Rectangle;

import se.egy.graphics.Drawable;
	
public class Entity implements Drawable{
	
	private Image image;
	   
    protected double xPos, yPos;   // Positionen
   
    private boolean active = true;
	
    public Entity (Image image, double xPos, double yPos){
     	this.image = image;   
     	this.xPos = xPos;
     	this.yPos = yPos;
     	/*rec = new Rectangle((int)xPos, (int)yPos, image.getWidth(null), 
                image.getHeight(null));*/
    }
    
    public Entity (String txt, double xPos, double yPos){ 
     	this.xPos = xPos;
     	this.yPos = yPos;
    }
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)xPos, (int)yPos, null);
	}
	public double getXPos() {
		return xPos;
	}
	public void setX(double xPos) {
		this.xPos = xPos;
	}
	public double getYPos() {
		return yPos;
	}
	public void setY(double yPos) {
		this.yPos = yPos;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
