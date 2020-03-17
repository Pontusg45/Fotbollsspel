package M9;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class TextEntity extends Entity {
	private int xPos, yPos;
	private String txt = "";
	private Font font = null;
	private Color color = null;
	

	public TextEntity(String txt, int xPos, int yPos, Font font, Color color) {
		super(xPos, yPos);
		this.txt = txt;
		this.xPos = xPos;
		this.yPos = yPos;
		this.font = font;
		this.color = color;
	}
	@Override
	public void draw(Graphics2D g) {	
		if(font != null) {
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setFont(font);
		}	
		g.setColor(color);
		g.drawString(txt, xPos, yPos);
	}
	
	
	public void setXPos(int xPos) {
		this.xPos = xPos;
	}
	
	/**
	 * Ãndrar y-pos 
	 * @param y-pos 
	 */
	public void setYPos(int yPos) {
		this.yPos = yPos;
	}
	/**
	 * x-koordinaten
	 * @return x-koordinaten 
	 */
	public int getXPos() {
		return xPos;
	}
	
	/**
	 * x-koordinaten
	 * @return x-koordinaten
	 */
	public int getYPos() {
		return yPos;
	}
	
	/**
	 * Ãndrar texten
	 * @param txt 
	 */
	public void setTxt(String txt) {
		this.txt = txt;
	}
	
	/**
	 * Texten i containern
	 * @return texten
	 */
	public String getTxt() {
		return txt;
	}
	
	/**
	 * Ãndrar typsnitt
	 * @param font fonten
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	
	/**
	 * Ãndrar textfärgen
	 * @param color FÃ¤rgen 
	 * @see java.awt.Color
	 */
	public void setFont(Color color) {
		this.color = color;
	}
	@Override
	public void move(long deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
}
