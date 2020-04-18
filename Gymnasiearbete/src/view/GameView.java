package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import model.Entity;
import model.TextEntity;

/**
 * @author Pontus Gustafsson
 */
public class GameView {
	
	private int height, width;
	private String title;
	private JFrame jf;
	private Canvas canvas;
	private BufferStrategy backBuffer;
	private Color bgColor = Color.BLACK;
	private Image bgImg = null;
	private Graphics2D g;
	
	/**
	 * Metod som impleterar tangentbords lyssnare i canvasen.
	 * @param keyListener 
	 */
	public void setKeyListener(KeyListener keyListener) {
		canvas.addKeyListener(keyListener);
	}

	/**
	 * Spelfönstrets konstruktor
	 * 
	 * @param witdh fönstrets bredd i px
	 * @param height fönstrets höjd i px
	 * @param title fönstrets namn
	 */
	public GameView(int width, int height, String title) {
		this.height = height;
		this.width = width;
		this.title = title;

		createWindow(); 
	}
	
	/**
	 * Skapar vår rityta canvas med rätt bredd och höjd 
	 */
	private void createWindow() {
		canvas = new Canvas();
		canvas.setSize(new Dimension(width, height));

		jf = new JFrame(title);

		jf.add(canvas);

		jf.setResizable(false);
		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 

		jf.setIgnoreRepaint(true);   
		jf.setVisible(true);
		canvas.requestFocus();

		canvas.createBufferStrategy(2);
		backBuffer = canvas.getBufferStrategy();
	}
	
	/**
	 * Renderar en Drawable-array
	 * 
	 * @param drawArray
	 */
	public void render(Drawable[] drawArray) {
		beginRender();

		for(int i = 0;i < drawArray.length; i++) {
			drawArray[i].draw(g);    
		}
	}
	
	/**
	 * 
	 */
	public void render(Collection<? extends Drawable> drawList) {
		beginRender();

		for(Drawable drawoObj : drawList) {
			drawoObj.draw(g);
		}
	}
	
	/**
	 * 
	 */
	public void render(TextEntity textEntity) {
		beginRender();

		textEntity.draw(g);

		show();
	}
	
	/**
	 * 
	 */
	public void render(Drawable drawObj) {
		beginRender();

		drawObj.draw(g);
	}
	
	/**
	 * 
	 */
	public void beginRender() {
		g = (Graphics2D)backBuffer.getDrawGraphics();

		if(bgImg == null) {
			g.setColor(bgColor);
			g.fillRect(0, 0, width, height);
		}else {
			g.drawImage(bgImg, 0, 0, width, height, null);
		}
	}
	
	/**
	 * 
	 */
	public void show() {
		g.dispose();
		backBuffer.show();
	}
	
	/**
	 * 
	 */
	public void openRender(ArrayList<Entity> spriteList) {
		Graphics2D g = (Graphics2D)backBuffer.getDrawGraphics();

		for(int i = 0;i < spriteList.size(); i++) {
			spriteList.get(i).draw(g);    
		}
	}
	
	/**
	 * 
	 */
	public void setBackground(String pathToImage) {
		bgImg = new ImageIcon(getClass().getResource(pathToImage)).getImage();
	}

	/**
	 * 
	 */
	public void setMouseListner(MouseListener mouseListener) {
		canvas.addMouseListener(mouseListener);
	}
	
	/**
	 * 
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * 
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * 
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * 
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
}

