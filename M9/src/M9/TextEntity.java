package M9;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
/**
 * Instanser av klassen kan presenteras grafiskt i en instans av klasserna {@link se.egy.graphics.JWin}
 *  och {@link se.egy.graphics.GameScreen}.
 * 
 * @see Drawable
 * @see GameScreen
 * @see JWin
 * 
 * @author Henrik Bygren
 *
 */
public class TextEntity implements Drawable {
	private int x, y;
	private String txt = "";
	private Font font = null;
	private Color color = null;
	
	/**
	 * Konstruktor
	 * @param txt	Texten
	 * @param x		Position i x-led
	 * @param y		Position i y-led
	 * @param font	{@link java.awt.Font}
	 * @param color	{@link java.awt.Color} 
	 */
	public TextEntity(String txt, int x, int y, Font font, Color color) {
		this.txt = txt;
		this.x = x;
		this.y = y;
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
		g.drawString(txt, x, y);
	}
	
	/**
	 * Ã„ndrar x-koordinaten fÃ¶r texten
	 * @param x	x-koordinaten fÃ¶r texten
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Ã„ndrar y-koordinaten fÃ¶r texten
	 * @param y	y-koordinaten fÃ¶r texten
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * x-koordinaten fÃ¶r texten
	 * @return x-koordinaten fÃ¶r texten
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * x-koordinaten fÃ¶r texten
	 * @return x-koordinaten fÃ¶r texten
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Ã„ndrar texten i containern
	 * @param txt texten
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
	 * Ã„ndrar typsnitt och storlek
	 * @param font fonten
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	
	/**
	 * Ã„ndrar textfÃ¤rgen
	 * @param color FÃ¤rgen 
	 * @see java.awt.Color
	 */
	public void setFont(Color color) {
		this.color = color;
	}
	
}
