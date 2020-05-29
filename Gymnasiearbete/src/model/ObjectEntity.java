package model;

import java.awt.Image;

/**
 * En klass som skapar alla objekt med bild och ärver från klassen Entity.
 * 
 * @author Pontus Gustafsson
 */
public class ObjectEntity extends Entity {

	private int directionY;
	private int directionX;
	private double speedX;
	private double speedY;
	
	/**
	 * Konstruktor för spelets bildobjekt
	 * 
	 * @param xPos position i x-led
	 * @param yPos position i y-led
	 * @param image bildens 
	 * @param speedX hastighet i x-led
	 * @param speedY hastighet i t-led
	 */
	public ObjectEntity( double xPos, double yPos,Image image, double speedX, double speedY) {
		super( image, xPos, yPos);
		directionY = 1;
		directionX = 1;
	}

	/**
	 * En metod som gör att spelobjekten kan röra sig
	 * 
	 * @param deltaTime 
	 */
	public void move(long deltaTime) {
		setY((getYPos() + directionY*(deltaTime/1000000000.0)*speedY));
		setX((getXPos() + directionX*(deltaTime/1000000000.0)*speedX));
	}
	
	/**
	 * En metod som sätter objektes hastighet i x-led och y-led.
	 * 
	 * @param speedX hastighet i x-led
	 * @param speedY hastighet i y-led
	 */
	public void setSpeed(double speedX, double speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
	}
	
	/** 
	 * @param directionX 0 = stilla, 1 = höger, -1 = vänster
	 */
	public void setDirectionX(int directionX){
		this.directionX = directionX;
	}

	/**
	 * @param directionY 0 = stilla, 1 = ner, -1 = upp
	 */
	public void setDirectionY(int directionY){
		this.directionY = directionY;
	}
}