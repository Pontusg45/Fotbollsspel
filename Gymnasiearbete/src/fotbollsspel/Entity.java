package fotbollsspel;

import java.awt.Graphics2D;
import java.awt.Image;

import se.egy.graphics.Drawable;
	
public class Entity implements Drawable{
	
	private Image image;
	   
    protected double x, y;   // Positionen
   
    private boolean active = true;
	
	public Entity( double  x, double y, Image img) {
		image = img;
	    this.x = x;
	    this.y = y;
	    
		}
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, null);
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
