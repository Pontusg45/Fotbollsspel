package M9;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class Entity implements Drawable {
    private Image image;
   
    protected double xPos, yPos;   // Positionen
   
    protected int speed;           // Hastighet i px/sekund
   
    protected int dx = 0, dy = 0;  // Rörelseriktning
   
    private boolean active = true; // Gör alla nya objekt aktiva.
    
    private Rectangle rec = null;
   
    /**
     * Konstruktor
     */
    public Entity (Image image, double xPos2, double yPos2, int speed){
     	this.image = image;   
     	this.xPos = xPos2;
     	this.yPos = yPos2;
     	this.speed = speed;
     	rec = new Rectangle((int)xPos2, (int)yPos2, image.getWidth(null), 
                image.getHeight(null));
    }
    public Rectangle getRectangle(){
        rec.setLocation((int)xPos, (int)yPos);
        return rec;
    }
   
    public boolean getActive() {
		return active;
	}
    public boolean collision(Entity entity){
        getRectangle(); // Uppdaterar positionen på den egna rektangeln
        return rec.intersects(entity.getRectangle());
    }

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	/**
     * Ritar bilden på ytan g
     */
    public void draw(Graphics2D g) {
     	g.drawImage(image,(int)xPos,(int)yPos,null);
    }
   
    /**
     * Vilken riktning i x-led
     * @param dx 0 = stilla, 1 = höger, -1 = vänster
     */
    public void setDirectionX(int dx){
     	this.dx = dx;
    }
   
    /**
     * Vilken riktning i y-led
     * @param dy 0 = stilla, 1 = höger, -1 = vänster
     */
    public void setDirectionY(int dy){
     	this.dy = dy;
    }
   
    /**
     * Metod som gör förflyttningen, dvs ändrar xPos och yPos
     * Måste skapas i klasser som ärver entity
     * @param antal nanosekunder sedan förra anropet 
     */
    public abstract void move(long deltaTime);
}


