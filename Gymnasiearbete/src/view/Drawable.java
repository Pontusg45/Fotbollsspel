package view;

import java.awt.Graphics2D;

/**
 * Detta gränssnitt skall implemteras av de klasser som skall representera en grafisk bild eller form på ett {@link se.egy.graphics.JWin} objekt.
 * Det viktiga är att klassen som representerar någon form av grafiskt objekt implmenterar metoden <code>draw(Graphics2D g)</code>.
 * 
 * @author Pontus Gustafsson
 */
public interface Drawable {
	/**
	 * Renderar figuren/bilden
	 * @param g grafikobjekt som figuren skall renderas av.
	 */
	public void draw(Graphics2D g);

}
