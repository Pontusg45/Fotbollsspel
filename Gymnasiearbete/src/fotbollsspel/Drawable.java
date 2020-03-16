package fotbollsspel;

import java.awt.Graphics2D;

/**
 * Detta grÃ¤nssnitt skall implemteras av de klasser som skall representera en grafisk bild eller form pÃ¥ ett {@link se.egy.graphics.JWin} objekt.
 * Det viktiga Ã¤r att klassen som representerar nÃ¥gon form av grafiskt objekt implmenterar metoden <code>draw(Graphics2D g)</code>.
 * 
 * <p>Exempel pÃ¥ klass som kan rita ut en rektangel:
 * <pre><code>
 * import se.egy.graphics;
 * import java.awt.Color;
 * import java.awt.Graphics;
 * import java.awt.Graphics2D;
 * 
 * public class Rectangle implements Drawable{
 *    private int x, y;
 *    private int width;
 *    private int height;
 *    private Color color;
 * 
 *    public Rectangle(int width, int height, int x, int y, Color color){
 *       this.x = x;
 *       this.y = y;
 *       this.width = width;
 *       this.height = height;
 *       this.color = color;
 *    } 
 * 
 *    &#64;Override
 *    public void draw(Graphics2D g){
 *       g.setColor(color);
 *       g.fillRect(x, y, width, height);
 *    }
 * }
 * </code></pre>
 * 
 * <p>Exempel pÃ¥ klass som kan rita ut en bild:
 * <pre><code>
 * import java.awt.Graphics2D;
 * import java.awt.Image;
 * import javax.swing.ImageIcon;
 * 
 * import se.egy.graphics.Drawable;
 *
 * public class ImgContainer implements Drawable{
 *	private int x,y;
 *	private Image image;
 * 
 *	public ImgContainer(int x, int y, String pathToImage) {
 *		this.x = x;
 *		this.y = y;
 *		this.image = new ImageIcon(pathToImage).getImage();
 *	}
 *
 *	&#64;Override
 *	public void draw(Graphics2D g) {
 *		g.drawImage(image, x, y, null);
 *	}
 * }
 * </code></pre>
 * 
 * @author Henrik Bygren
 *
 */
public interface Drawable {
	/**
	 * Renderar figuren/bilden
	 * @param g grafikobjekt som figuren skall renderas av.
	 */
	public void draw(Graphics2D g);

}
