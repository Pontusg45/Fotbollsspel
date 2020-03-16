package fotbollsspel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.Collection;
import javax.swing.ImageIcon;
import javax.swing.JFrame;


// Skapa javadoc med fÃ¶ljande direktiv fÃ¶r att fÃ¥ rÃ¤tt teckenkodning fÃ¶r Ã¥,Ã¤ och Ã¶. 
// -charset "utf-8" se.egy.graphics
//
// I Eclipse VÃ¤ljer man "Project/Generate Javadoc...". Klicka "NÃ¤sta" tills rutan "Extra Javadoc options" syns.
// Klistra in: -charset "utf-8" se.egy.graphics

/**
 * En utvidgning av {@link javax.swing.JFrame} fÃ¶r att rendera grafik till enklare 2D-spel.
 * 
 * <p>Vanliga upplÃ¶sningar i fullskÃ¤rmslÃ¤ge:
 * <pre>
 *  640x480
 *  800x600
 *  1024x768
 *  1280x800
 *  1440x900
 *  (1680x1050)
 * </pre>
 *  
 * <p>AnvÃ¤nder endast 32 bitars fÃ¤rgupplÃ¶sning.
 * 
 * @see javax.swing.JFrame
 * @see java.awt.DisplayMode
 * 
 * @author Henrik Bygren
 */
public class GameScreen{

	private int height, width;
	private String title;
	private boolean fullScreenMode = false;

	private JFrame jf;

	private GraphicsDevice device;
	private BufferStrategy backBuffer;
	private Graphics2D g;

	private Color bgColor = Color.BLACK; // Svart, default som bakgrund
	private Image bgImg = null;

	private DisplayMode oldDM = null;

	/**
	 * Konstruerar ett fÃ¶nster <code>JFrame</code> med en <code>Canvas</code> fÃ¶r rendering av grafik.
	 * 
	 * @param title 		FÃ¶nstrets titel, visas ej i fullskÃ¤rmslÃ¤gen
	 * @param width			FÃ¶nstrets bredd
	 * @param height		FÃ¶nstrets hÃ¶jd
	 * @param fulScreenMode SÃ¤tt till <code>true</code> fÃ¶r att visa i fullskÃ¤rmslÃ¤ge.
	 */
	public GameScreen(String title, int width, int height, boolean fulScreenMode) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.fullScreenMode = fulScreenMode;

		try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                	createWindow();
                }
            });
        }catch(Exception e) {}
		
		clearScreen();
		
		if(fullScreenMode) { // Ful lÃ¶sning ;-)
			jf.setBounds(0, 0, width, height);
			try{ Thread.sleep(50);}catch(Exception e){}
		}
		
	}

	/**
	 * Konstruerar ett fÃ¶nster <code>JFrame</code> med en <code>Canvas</code> fÃ¶r rendering av grafik.
	 * 
	 * @param title 		FÃ¶nstrets titel
	 * @param width			FÃ¶nstrets bredd
	 * @param height		FÃ¶nstrets hÃ¶jd
	 */
	public GameScreen(String title, int width, int height) {
		this(title, width, height, false);
	}

	// Kontrollera eventuella undantag, slarvig backrundskoll av hur det fungerar med fullskÃ¤rmslÃ¤ge och undantag.
	private void createWindow() {
		jf = new JFrame(title);

		if(fullScreenMode) {
			DisplayMode dm = new DisplayMode(width, height, 32, 0);
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			device = env.getDefaultScreenDevice();

			jf.setUndecorated(true);
			jf.setBounds(0, 0, width, height);
			
			oldDM = device.getDisplayMode();
			
			device.setFullScreenWindow(jf);
			device.setDisplayMode(dm);
		}else {
			jf.setSize(new Dimension(width, height));
			jf.setResizable(false);
			jf.setLocationRelativeTo(null);
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
		jf.setIgnoreRepaint(true);
		jf.setVisible(true);
		jf.requestFocus();

		jf.createBufferStrategy(2);
		backBuffer = jf.getBufferStrategy();
	}

	/**
	 * Ã„ndrar ritytans bakgrund till vald fÃ¤rg. Eventuell backgrundsbild plockas bort.
	 * 
	 * @param color BakgrundsfÃ¤rg
	 */
	public void setBackground(Color color) {
		bgColor = color;
		bgImg = null;
	}

	/**
	 * Ã„ndrar ritytans bakgrund till vald bild.
	 * 
	 * @param pathToImage SÃ¶kvÃ¤gen till bilden
	 */
	public void setBackground(String pathToImage) {
		bgImg = new ImageIcon(getClass().getResource(pathToImage)).getImage();
	}

	/**
	 * Skapar en referens till det objekt som hanterar tanget-hÃ¤ndelser som triggas i spelfÃ¶nstret.
	 * 
	 * @param keyListener	Instans som hanterar tangent-hÃ¤ndelserna. 
	 */
	public void setKeyListener(KeyListener keyListener) {
		jf.addKeyListener(keyListener);
	}

	/**
	 * Skapar en referens till det objekt som hanterar mus-hÃ¤ndelser som triggas i spelfÃ¶nstret.
	 * 
	 * @param mouseListener Instans som hanterar mus-hÃ¤ndelserna. 
	 */
	public void setMouseListner(MouseListener mouseListener) {
		jf.addMouseListener(mouseListener);
	}

	/**
	 * FÃ¶nstrets bredd.
	 * @return bredden pÃ¥ ritytan
	 */
	public int getWidth() {
		return jf.getWidth();
	}

	/**
	 * FÃ¶nstrets hÃ¶jd.
	 * @return hÃ¶jden pÃ¥ ritytan
	 */
	public int getHeight() {
		return jf.getHeight();
	}
	
	/**
	 * Metoden returnerar en grafikbuffert som man kan rendera pÃ¥.
	 * FÃ¶r att gÃ¶ra grafikbufferten synlig skall medoden {@link #show()} anropas.
	 * 
	 * Det Ã¤r dock att fÃ¶redra att anvÃ¤nda render-metoderna vid rendering
	 * 
	 * @return Grafikbuffert att rita pÃ¥
	 */
	public Graphics2D getGraphics2D(){
		return (Graphics2D)backBuffer.getDrawGraphics();
	}

	/**
	 * Renderar en ny skÃ¤rm med objekt i en <code>Collection</code> av <code>Drawable</code>.
	 * Exempelvis kan <code>ArrayList</code> och <code>LinkedList</code> anvÃ¤ndas som parameter till metoden.
	 * 
	 * @param drawableList <code>Collection</code> med objekt som implementerat Drawable
	 *  
	 * @see Drawable 
	 */
	public void render(Collection<? extends Drawable> drawableList) {		
		beginRender();

		synchronized(drawableList) {
			for(Drawable drawObj : drawableList) {
				drawObj.draw(g);
			}
		}

		show();
	}

	/**
	 * Renderar en ny skÃ¤rm med objekt i en primitiv array av <code>Drawable</code>.
	 * 
	 * @param drawableArray Array med objekt som implementerat Drawable
	 */
	public void render(Drawable[]  drawableArray) {	
		beginRender();
		// 	List<Integer> list = Arrays.stream(drawableArray).boxed().collect(Collectors.toList());
		// render(list);
		for(Drawable drawObj : drawableArray) {
			try {
				drawObj.draw(g);
			}catch(NullPointerException e) {}
		}

		show();
	}

	/**
	 * Renderar en enskild Drawable.
	 * Kan exempelvis vara en bild som lagrats i en {@link ImgContainer}.
	 * 
	 * @param drawable 	BildbehÃ¥llaren
	 */
	public void render(Drawable drawable) {
		beginRender();

		drawable.draw(g);

		show();
	}

	/**
	 * Renderar en enskild bild.
	 * 
	 * @param img 	Bilden
	 * @param x		Bildens x-position pÃ¥ skÃ¤rmen
	 * @param y		Bildens y-position pÃ¥ skÃ¤rmen
	 */
	public void render(Image img, int x, int y) {
		beginRender();

		g.drawImage(img, x, y, null);

		show();
	}

	// Renderar bakgrunden
	private void beginRender() {
		g = (Graphics2D)backBuffer.getDrawGraphics();

		if(bgImg == null) {
			g.setColor(bgColor);
			g.fillRect(0, 0, width, height);
		}else {
			g.drawImage(bgImg, 0, 0, width, height, null);
		}
	}

	/**
	 * GÃ¶r grafikbufferten synlig. Anropas efter rendering pÃ¥ grafikobjektet 
	 * som nÃ¥s via metoden {@link #getGraphics2D()}
	 * 
	 */
	public void show() {
		g.dispose();
		backBuffer.show();
	}

	public void clearScreen() {
		beginRender();
		show();
	}

	/**
	 * Statisk metod som returnerar datorns mÃ¶jliga skÃ¤rmlÃ¤gen.
	 * 
	 * @return primitiv array med mÃ¶jliga {@link java.awt.DisplayMode}
	 * 
	 * @see java.awt.DisplayMode
	 */
	public static DisplayMode[] getAvailableDisplayModes() {
		GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return device.getDisplayModes();
	}

	/**
	 * Statisk metod fÃ¶r att kontrollera datorns mÃ¶jliga skÃ¤rmlÃ¤gen.
	 * Utskrift av skÃ¤rmlÃ¤gen gÃ¶rs i consolen.
	 * 
	 * <p>Ett test kan se ut som nedan.
	 * 
	 * <pre><code>
	 * public static void main(String[] args) {
	 *    GameWindow.availableDisplayModes();
	 * }
	 * </code></pre>
	 */
	public static void availableDisplayModes() {

		DisplayMode[] modes = getAvailableDisplayModes();

		for(int i = 0; i < modes.length; i++) {
			DisplayMode DM = modes[i];
			System.out.println(i +") Resolution : " + DM.getWidth() +"x" + DM.getHeight() + " \tColors: " +DM.getBitDepth());
		}
	}

	/**
	 * StÃ¤nger fÃ¶nstret
	 * 
	 * @throws GameCloseException Om det inte lyckas med att stÃ¤nga ner fÃ¶nstret.
	 */
	public void close() throws GameCloseException{

		if (fullScreenMode) {
			try {
				device.setDisplayMode(oldDM);
				device.setFullScreenWindow(null);
				fullScreenMode = false;
			}catch(Exception e) {
				throw new GameCloseException("Kunde inte stÃ¤ng fÃ¶nstret");
			}
		}

		jf.dispose();
	}
}
