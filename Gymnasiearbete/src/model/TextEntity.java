package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * @author Pontus Gustafsson
 */
public class TextEntity extends Entity {

	private int xPos, yPos;
	private String text;
	private Font font = null;
	private Color color = null;
	
	/**
	 * Konstruktorn för alla textobjekt
	 * 
	 * @param text
	 * @param xPos
	 * @param yPos
	 * @param font
	 * @param color
	 */
	public TextEntity(String text, int xPos, int yPos, Font font, Color color) {
		super(null ,xPos, yPos);
		this.text = text;
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
		g.drawString(text, xPos, yPos);
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	public void setFont(Color color) {
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
