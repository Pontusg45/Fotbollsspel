package model;

import java.awt.Graphics2D;
import java.awt.Image;

import view.Drawable;

/**
 * En klass som ritar alla objekt och implementerar gränssnittet Drawable.
 * 
 * @author Pontus Gustafsson
 */
public class Entity implements Drawable{

	private Image image;

	protected double xPos, yPos;

	/**
	 * Konstruktorn som objectEntity använder
	 * 
	 * @param image bildens instansvariabel
	 * @param xPos objektets x-position
	 * @param yPos objektets y-position
	 */
	public Entity (Image image, double xPos, double yPos){
		this.image = image;   
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)xPos, (int)yPos, null);
	}
	
	/**
	 * @return objektets positon i x-led
	 */
	public double getXPos() {
		return xPos;
	}
	
	/**
	 * @param xPos objektets positon i x-led
	 */
	public void setX(double xPos) {
		this.xPos = xPos;
	}
	public double getYPos() {
		return yPos;
	}
	public void setY(double yPos) {
		this.yPos = yPos;
	}
}
