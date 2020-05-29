package controller;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Entity;
import model.ObjectEntity;
import model.TextEntity;
import view.GameView;

/**
 * @author Pontus Gustafsson
 */
public class GameController implements MouseListener{

	private GameView gv;
	
	private Random rand = new Random();

	private boolean gameRunning = true;

	Image backgroundImg = new ImageIcon(getClass().getResource("/bakgrund.png")).getImage();
	Image ballImg = new ImageIcon(getClass().getResource("/ball.png")).getImage();
	Image goalkeeperImg = new ImageIcon(getClass().getResource("/målvakt.png")).getImage();

	private String bgImg = "/bakgrund.png";

	private boolean Shot = false;

	private boolean multiplayer = false;
	private boolean singleplayer = false;

	private boolean player1 = true;

	private ObjectEntity ball;
	private ObjectEntity goalkeeper;

	private TextEntity scoreboard;
	private TextEntity announcer;
	
	private String[] endResult;

	private int player1Point, player2Point;

	private int nrOfPlayers;
	private int nrOfRounds;

	private int goals, rounds, miss;

	private int goalkeeperPos;

	private int mousePosX, mousePosY;
	
	private double goalkeeperPosX, goalkeeperPosY;

	private double ballPosX, ballPosY;

	private double velocityX, velocityY; 	

	private double timeForShot = 0.4;

	private String ScoreboardSingleplayer = "Målvakt: "+ getMiss() + " - " + "Mål: "+ getGoals() + " " + "Runda: " + getRounds();

	private String ScoreboardMultiplayer = "Spelare 1: "+ getPlayer1Point() + " - " + "Spelare 2: "+ getPlayer2Point() + " | Runda " + ((getRounds()/2) + 1);

	private String scoreboardContent = "";

	private JFrame parent = new JFrame();

	private HashMap<String, Boolean> mouseDown = new HashMap<>();

	private ArrayList<Entity> spriteList = new ArrayList<>();

	/**
	 * Spelets konstruktor.
	 * 
	 * @param GameView gv
	 */
	public GameController(GameView gv) {
		this.gv = gv;

		gv.setMouseListner((MouseListener) this);

		mouseDown.put("clicked", false);
		loadImages();
	}

	/**
	 * En metod som skapar instanser av bildobjekten och textobjekten.
	 */
	public void loadImages(){

		gv.setBackground(bgImg); 	

		setBallPosX((gv.getWidth()/2)-ballImg.getWidth(null)/2);

		setBallPosY((gv.getHeight()/6) *5);

		setGoalkeeperPosY(gv.getWidth()/4);

		setGoalkeeperPosX((int)(gv.getHeight()/2.06));

		goalkeeper = new ObjectEntity(goalkeeperPosX, goalkeeperPosY, goalkeeperImg,10, 0);

		ball = new ObjectEntity(ballPosX, ballPosY, ballImg,getVelocityX(), getVelocityY());

		announcer = new TextEntity(" ", 400, 100, null, Color.white);

		scoreboard = new TextEntity(scoreboardContent, 100 , 100, null, Color.white);

		spriteList.add(goalkeeper);
		spriteList.add(ball);
		spriteList.add(scoreboard);
		spriteList.add(announcer);
	}

	/**
	 * En metod som gör spelet körbart och anropas i main-metoden
	 */
	public void run(){

		int fps = 50;
		int updateTime = (int)(1.0/fps*1000000000.0);

		long lastUpdateTime = System.nanoTime();

		while(gameRunning){
			long deltaTime = System.nanoTime() - lastUpdateTime;

			if(deltaTime > updateTime){
				lastUpdateTime = System.nanoTime();
				update(deltaTime);
				render();
				
				while(scoreboardContent == "") {
					gameSettings();
				}
				
				turnCounter();
				
				if (nrOfRounds == rounds && nrOfRounds > 0 || multiplayer == true && nrOfRounds*2 == rounds && nrOfRounds > 0){
					endOfGame();
				}
			}
		}
	}
	
	/**
	 * En metod som uppdaterar spelet
	 * 
	 *  @param deltaTime antal nanosekunder sedan förra anropet 
	 */
	public void update(long deltaTime) {
		
		if (Shot) {

			ballMovement(deltaTime, mousePosX, mousePosY);

			goalkeeperMovement(deltaTime);

			if (ball.getYPos() <= (getMousePosY() - (ballImg.getWidth(null)/2))) {
				ball.setSpeed(0, 0);	

				Shot = false;
				
				checkGoal();
				console();

				try {
					Thread.sleep(500);	
				}
				catch (InterruptedException e){}

				reset();
			}
		}
	}

	/**
	 * En metod som rendera objekten i gameview-fönstret
	 */
	public void render() {

		gv.beginRender();
		gv.openRender(spriteList);
		gv.show();
	}

	/**
	 * Flyttar bollens y-och x-position med hjälp av tiden och musens koordinater.
	 * 
	 * @param deltaTime skillnaden i tid mellan uppdateringarna
	 * @param mousePosX musens x-positon
	 * @param mousePosY musens y-position
	 */
	public void ballMovement(long deltaTime, int mousePosX, int mousePosY) {

		setVelocityX((mousePosX - (ballPosX+(ballImg.getWidth(null)/2)))/getTime());
		setVelocityY((mousePosY - (ballPosY+(ballImg.getHeight(null)/2)))/getTime());

		ball.setSpeed(getVelocityX(), getVelocityY());
		ball.move(deltaTime);
		ball.setSpeed(0, 0);
	}

	/**
	 * Flyttar målvaktens position genom att ändra hållet som den flyttar sig.
	 * 
	 * @param deltaTime skillnaden i tid mellan varje fps
	 */
	public void goalkeeperMovement(long deltaTime) {
		
		String[] direction = {"left", "center", "right"};
		
		switch (direction[getGoalkeeperPos()]) {
			case "right":
				goalkeeper.setDirectionX(1);
				break;
			case "center":
				goalkeeper.setDirectionX(0);
				break;
			case "left":
				goalkeeper.setDirectionX(-1);
				break;
		}
		
		goalkeeper.move(deltaTime);
		goalkeeper.setSpeed(400, 0);
	}

	/**
	 * Sätter upp spelets inställningar
	 */
	public void gameSettings() {

		nrOfPlayers = Integer.parseInt(JOptionPane.showInputDialog("1 eller 2 spelare?"));
		nrOfRounds = Integer.parseInt(JOptionPane.showInputDialog("Hur många rundor?"));
		
		if (nrOfPlayers == 1 ) {
			scoreboardContent = ScoreboardSingleplayer;
			singleplayer = true;
			endResult = new String[]{"Du vann", "Målvakten vann", "Det blev lika"};
		}
		else if (nrOfPlayers == 2) {
			scoreboardContent = ScoreboardMultiplayer;
			multiplayer = true;
			endResult =  new String[]{"Spelare 1 vann", "Spelare 2 vann", "Det blev lika"};
		}
		scoreboard.setText(scoreboardContent);
	}

	/**
	 * Returnerar resultatet av skottet och uppdaterar poängtavlan.
	 */
	public void checkGoal() {	

		if (getMousePosX() >= goalkeeper.getXPos() 
				&& getMousePosX() <= goalkeeper.getXPos() + goalkeeperImg.getWidth(null) 
				&& getMousePosY() >= goalkeeper.getYPos() 
				&& getMousePosY() <= goalkeeper.getYPos() + goalkeeperImg.getHeight(null)) {
			announcer.setText("Räddad");
			setMiss(miss + 1);
		}
		else if (getMousePosY() <= 131 ) {
			announcer.setText("För högt");
			setMiss(miss + 1);
		}	
		else if (getMousePosX() <= 70 || getMousePosX() >= 570) {
			announcer.setText("Utanför");
			setMiss(miss + 1);
		}
		else {
			announcer.setText("Mål!!");
			setGoals(goals + 1);

			if(player1 == true) {
				setPlayer1Point(player1Point + 1);
			}
			else if (player1 == false) {
				setPlayer2Point(player2Point + 1);
			}
		}
		setRounds(rounds +1);

		if (singleplayer) {
			scoreboard.setText("Målvakt: "+ getMiss() + " - " + "Mål: "+ getGoals() + " " + " | Runda " + getRounds());
		}
		else if (multiplayer) {
			scoreboard.setText("Spelare 1: "+ getPlayer1Point() + " - " + "Spelare 2: "+ getPlayer2Point() + " | Runda " + (getRounds()/2 +1));
		}
	}

	/**
	 * Loggar resultatet av ett skott i konsolen.
	 */
	public void console() {						
		System.out.println("Runda: " + getRounds());							
		System.out.println("Missar: "+ getMiss() + " - "+"Mål: " + getGoals());
		System.out.println("Spelare 1 antal poäng: "+ getPlayer1Point());
		System.out.println("Spelare 2 antal poäng: "+ getPlayer2Point());
		System.out.println();
	}

	/**
	 * En metod som återställer objektens positioner.
	 */
	public void reset() {						
		goalkeeper.setX(233);
		goalkeeper.setY(160);
		ball.setX((gv.getWidth()/2)-ballImg.getWidth(null)/2);
		ball.setY((gv.getHeight()/6) *5);
	}

	/**
	 * Returnerar vems tur det är.
	 */
	public void turnCounter() {

		if (getRounds()%2 == 0 && multiplayer == true) {
			player1 = true;
		} 
		else {
			player1 = false;
		}	
	}

	/**
	 * Avslutar spelet och avgör vinnaren.
	 */
	public void endOfGame() {
		
		int i;
		
		if(getPlayer1Point() > getPlayer2Point() || getGoals() > getMiss()) {
			i = 0;
		}
		else if (getPlayer1Point() < getPlayer2Point() || getGoals() < getMiss()){
			i = 1;	
		}
		else {
			i = 2;
		}
		
		int endWindow = JOptionPane.showConfirmDialog(parent, getEndResult(i) + "\n" + "Vill du börja ett nytt spel?" , null, JOptionPane.YES_NO_OPTION);
		
		if (endWindow == JOptionPane.YES_OPTION) {
			setPlayer2Point(0);
			setPlayer1Point(0);
			setMiss(0);
			setGoals(0);
			setRounds(0); 
			scoreboard.setText(scoreboardContent);
			return;
		}
		else if (endWindow == JOptionPane.NO_OPTION) {
			System.exit(0);
		}
		else if(endWindow == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
	}

	/**
	 * Kollar om mustangenten trycks ner
	 * 
	 * @param MouseEvent e
	 */
	public void mousePressed(MouseEvent e) {
		int mouse = e.getClickCount();

		if(mouse == MouseEvent.MOUSE_PRESSED) 
			mouseDown.put("clicked", true);
			
		Shot = true;
		
		setGoalkeeperPos(rand.nextInt(3));
		
		setMousePosX(e.getX());
		setMousePosY(e.getY());			
	}

	
	/**
	 * @Override
	 */
	public void mouseReleased(MouseEvent e) {

		int mouse = e.getClickCount();
		
		if(mouse == MouseEvent.MOUSE_RELEASED)
			mouseDown.put("clicked", false);	
	}
									/* Getters och Setters*/
	
	public void setBallPosX(int ballPosX) {
		this.ballPosX = ballPosX;
	}

	public int getMousePosX() {
		return mousePosX;
	}

	public void setMousePosX(int mousePosX) {
		this.mousePosX = mousePosX;
	}

	public int getMousePosY() {
		return mousePosY;
	}

	public void setMousePosY(int mousePosY) {
		this.mousePosY = mousePosY;
	}

	public int getGoals() {
		return goals;
	}

	public void setGoals(int goals) {
		this.goals = goals;
	}

	public int getMiss() {
		return miss;
	}

	public void setMiss(int miss) {
		this.miss = miss;
	}

	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public void setScoreboardSingleplayer(String scoreboardSingleplayer) {
		this.ScoreboardSingleplayer = scoreboardSingleplayer;
	}

	public double getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public double getTime() {
		return timeForShot;
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
	}

	@Override
	public void mouseEntered(MouseEvent e) {	
	}

	@Override
	public void mouseExited(MouseEvent e) {	
	}

	public int getGoalkeeperPos() {
		return goalkeeperPos;
	}

	public void setGoalkeeperPos(int gkPos) {
		this.goalkeeperPos = gkPos;
	}

	public int getPlayer1Point() {
		return player1Point;
	}

	public void setPlayer1Point(int player1Point) {
		this.player1Point = player1Point;
	}

	public int getPlayer2Point() {
		return player2Point;
	}

	public void setPlayer2Point(int player2Point) {
		this.player2Point = player2Point;
	}

	public void setScoreboardContent(String scoreboard) {
		this.scoreboardContent = scoreboard;
	}

	public double getGoalkeeperPosX() {
		return goalkeeperPosX;
	}
	public void setGoalkeeperPosX(int goalkeeperPosX) {
		this.goalkeeperPosX = goalkeeperPosX;
	}

	public double getBallPosY() {
		return ballPosY;
	}
	public void setBallPosY(double ballPosY) {
		this.ballPosY = ballPosY;
	}

	public void setGoalkeeperPosY(int goalkeeperPosY) {
		this.goalkeeperPosY = goalkeeperPosY;
	}
	
	public String getEndResult(int i) {
		return endResult[i];
	}
}