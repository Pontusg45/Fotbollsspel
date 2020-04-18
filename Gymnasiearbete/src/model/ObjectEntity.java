package model;

import java.awt.Image;

/**
 * @author Pontus Gustafsson
 */
public class ObjectEntity extends Entity {
	
	private int directionY = 1;
	private int directionX = 1;
	private double speedX;
	private double speedY;
	private double speed;
	
    public ObjectEntity( double xPos, double yPos,Image image, double speedX, double speedY) {
        super( image, yPos, xPos);
        directionY = 1;
        directionX = 1;
       
        this.setActive(false);
    }
    
    public void setSpeed(double speedX, double speedY) {
    	this.speedX = speedX;
    	this.speedY = speedY;
    }

	public void move(long deltaTime) {
        setY((getYPos() + directionY*(deltaTime/1000000000.0)*speedY));
        setX((getXPos() + directionX*(deltaTime/1000000000.0)*speedX));
    }
	
	public double getSpeed() {
		return speed;
	}
	 /**
     * Vilken riktning i x-led
     * @param dx 0 = stilla, 1 = höger, -1 = vänster
     */
    public void setDirectionX(int directionX){
     	this.directionX = directionX;
    }
   
    /**
     * Vilken riktning i y-led
     * @param dy 0 = stilla, 1 = höger, -1 = vänster
     */
    public void setDirectionY(int directionY){
     	this.directionY = directionY;
    }
	
}
