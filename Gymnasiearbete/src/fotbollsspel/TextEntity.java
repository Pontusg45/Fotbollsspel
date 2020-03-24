package fotbollsspel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class TextEntity extends Entity {
	
	private int xPos, yPos;
	private String text;
	
	private Font font = null;
	private Color color = null;

	public TextEntity(String text, int xPos, int yPos, Font font, Color color) {
		super(text ,xPos, yPos);
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
