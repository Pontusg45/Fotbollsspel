package fotbollsspel;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GameView {
	private int height, width;
	private String title;

	private JFrame jf; // Fönstret
	private Canvas canvas; // ritytan där renderingen sker
	private BufferStrategy backBuffer;
	private Color bgColor = Color.BLACK; // Svart, default som bakgrund
	private Image bgImg = null;
	private Graphics2D g;
   
	public void setKeyListener(KeyListener keyListener) {
		canvas.addKeyListener(keyListener);
	}

	public GameView(int width, int height, String title) {
		this.height = height;
		this.width = width;
		this.title = title;
       
		createWindow();
		// Skapar vår rityta canvas med rätt bredd och höjd  
   }
   public void render(Drawable[] drawArray) {
	   beginRender();
	   
	   for(int i = 0;i < drawArray.length; i++) {
		   drawArray[i].draw(g);    
	   }
	}

	public void render(Collection<? extends Drawable> drawList) {
		beginRender();

	   for(Drawable drawoObj : drawList) {
	       drawoObj.draw(g);
	   }
	}
	
	public void render(Drawable drawObj) {
		beginRender();

		drawObj.draw(g);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	private void createWindow() {
		canvas = new Canvas();
		canvas.setSize(new Dimension(width, height));
		
		// Skapar fönstret.
		jf = new JFrame(title);
		
		// Lägger in ritytan i fönstret.
		jf.add(canvas);
		
		// Lite inställningar
		jf.setResizable(false); // Går ej att ändra storlek på fönster
		jf.pack(); // Packar så att inget tomrum visas
		jf.setLocationRelativeTo(null); // Placeras i mitten på skärmen
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Går att stånga av med x-rutan 
		jf.setIgnoreRepaint(true); // Ritas inte om av JVM.       
		jf.setVisible(true); // Gör allt synligt!
		canvas.requestFocus(); // Ger vår canvas fokus
		
		canvas.createBufferStrategy(2);
		backBuffer = canvas.getBufferStrategy();
	}
	
	void beginRender() {
		g = (Graphics2D)backBuffer.getDrawGraphics();
		
		if(bgImg == null) {
			g.setColor(bgColor);
			g.fillRect(0, 0, width, height);
			}else {
				g.drawImage(bgImg, 0, 0, width, height, null);
			}
	 }
	 
	 public void show() {
		 g.dispose();
		 backBuffer.show();
	}
	
	 public void openRender(ArrayList<Entity> spriteList) {
		 Graphics2D g = (Graphics2D)backBuffer.getDrawGraphics();
		 
		 for(int i = 0;i < spriteList.size(); i++) {
			   spriteList.get(i).draw(g);    
		   }
	 }
	 
	 public void setBackground(String pathToImage) {
			bgImg = new ImageIcon(getClass().getResource(pathToImage)).getImage();
		}
	 
	 public void setMouseListner(MouseListener mouseListener) {
			canvas.addMouseListener(mouseListener);
		}
}

